package project.group1.commutemate.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.google.transit.realtime.GtfsRealtime.Alert;
import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;
import com.google.transit.realtime.GtfsRealtime.TranslatedString;

import tools.jackson.databind.JsonNode;

import project.group1.commutemate.model.BusArrival;
import project.group1.commutemate.model.ServiceAlert;
import project.group1.commutemate.model.TransitInfo;

/**
 * Fetches real-time transit information from the TransLink Open API:
 * upcoming bus arrivals at a stop (RTTI) and active service alerts
 * (GTFS-realtime). Every call degrades gracefully so a TransLink outage
 * never breaks the dashboard.
 */
@Service
public class TransitService {

    /** RTTI real-time bus estimates for a single stop. {stop} and {key} are filled in per request. */
    private static final String ESTIMATES_URL =
            "https://api.translink.ca/rttiapi/v1/stops/{stop}/estimates?apikey={key}&count=5";

    /** GTFS-realtime service alerts for the whole TransLink network. {key} is filled in per request. */
    private static final String ALERTS_URL =
            "https://gtfsapi.translink.ca/v3/gtfsalerts?apikey={key}";

    /** At most this many alerts on the dashboard card, so it stays readable. */
    private static final int MAX_ALERTS = 5;

    private final RestClient restClient = RestClient.create();
    private final String apiKey;
    private final String stopNo;

    public TransitService(
            @Value("${translink.api.key}") String apiKey,
            @Value("${translink.stop-no}") String stopNo) {
        this.apiKey = apiKey;
        this.stopNo = stopNo;
    }

    /**
     * Bundles everything the dashboard needs. When the arrivals call fails
     * (e.g. the TransLink API is unreachable) apiAvailable is false, so the
     * view can show an error while the rest of the page keeps working.
     */
    public TransitInfo getTransitInfo() {
        List<BusArrival> arrivals = fetchArrivals();   // null signals the API is unreachable
        boolean apiAvailable = (arrivals != null);
        List<ServiceAlert> alerts = fetchAlerts();

        return new TransitInfo(
                apiAvailable,
                apiAvailable ? arrivals : List.of(),
                alerts);
    }

    /**
     * Calls the TransLink RTTI API for the configured stop.
     * Returns the list of upcoming buses (possibly empty when the stop has no
     * service right now), or null when the API call itself fails.
     */
    private List<BusArrival> fetchArrivals() {
        try {
            JsonNode root = restClient.get()
                    .uri(ESTIMATES_URL, stopNo, apiKey)
                    .accept(MediaType.APPLICATION_JSON)   // TransLink returns XML unless we ask for JSON
                    .retrieve()
                    // A 4xx (e.g. "no stops found") means "no upcoming buses", not an outage,
                    // so swallow it here and let parseArrivals return an empty list below.
                    .onStatus(status -> status.is4xxClientError(), (request, response) -> { })
                    .body(JsonNode.class);

            return parseArrivals(root);
        } catch (Exception e) {
            return null;   // network error / 5xx / unparseable payload -> API unavailable
        }
    }

    /**
     * Turns a raw RTTI estimates payload into a flat list of upcoming buses.
     * A successful response is a JSON array of routes, each holding a list of
     * schedules; anything else (an error object, empty body) yields an empty list.
     */
    static List<BusArrival> parseArrivals(JsonNode root) {
        List<BusArrival> arrivals = new ArrayList<>();
        if (root == null || !root.isArray()) {
            return arrivals;
        }
        for (JsonNode route : root) {
            String routeNo = route.path("RouteNo").asString("").trim();
            for (JsonNode schedule : route.path("Schedules")) {
                arrivals.add(new BusArrival(
                        routeNo,
                        schedule.path("Destination").asString("").trim(),
                        schedule.path("ExpectedCountdown").asInt(0),
                        schedule.path("ExpectedLeaveTime").asString("").trim()));
            }
        }
        return arrivals;
    }

    /**
     * Active service alerts from the TransLink GTFS-realtime feed (protobuf).
     * Returns an empty list when there are no alerts or the feed is unreachable,
     * which the dashboard renders as "No active service alerts."
     */
    private List<ServiceAlert> fetchAlerts() {
        try {
            byte[] feed = restClient.get()
                    .uri(ALERTS_URL, apiKey)
                    .retrieve()
                    .body(byte[].class);
            if (feed == null) {
                return List.of();
            }

            FeedMessage message = FeedMessage.parseFrom(feed);
            List<ServiceAlert> alerts = new ArrayList<>();
            for (FeedEntity entity : message.getEntityList()) {
                if (!entity.hasAlert()) {
                    continue;
                }
                Alert alert = entity.getAlert();
                String title = firstTranslation(alert.getHeaderText());
                if (title.isBlank()) {
                    continue;
                }
                alerts.add(new ServiceAlert(title, firstTranslation(alert.getDescriptionText())));
                if (alerts.size() >= MAX_ALERTS) {
                    break;
                }
            }
            return alerts;
        } catch (Exception e) {
            return List.of();
        }
    }

    /** First available translation of a GTFS-realtime text field, or "" when there is none. */
    private static String firstTranslation(TranslatedString text) {
        if (text == null || text.getTranslationCount() == 0) {
            return "";
        }
        return text.getTranslation(0).getText().trim();
    }
}

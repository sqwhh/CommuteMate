package project.group1.commutemate.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import project.group1.commutemate.model.Ride;

/**
 * In-memory store of carpool rides.
 *
 * <p>This is deliberately mock data (ported from the prototype's {@code mock-rides.ts})
 * so the UI is fully browsable while the real persistence layer and TransLink/weather
 * APIs are built out in later agile iterations.</p>
 */
@Service
public class RideService {

    private final List<Ride> rides = new ArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public RideService() {
        seed();
    }

    private void seed() {
        LocalDate today = LocalDate.of(2026, 7, 6);
        add(new Ride(nextId(), "Priya S.", "PS", "Metrotown Station", "SFU Burnaby — AQ",
                LocalDateTime.of(today, LocalTime.of(8, 15)), 4, 2, 4, 25, 82,
                "Toyota Corolla · Silver", 4.9, "Leaving from Kiss & Ride, quiet drive."));
        add(new Ride(nextId(), "Marcus L.", "ML", "Production Way–University", "SFU Burnaby — West Mall",
                LocalDateTime.of(today, LocalTime.of(8, 45)), 3, 1, 3, 20, 74,
                "Honda Civic · Blue", 4.8, null));
        add(new Ride(nextId(), "Emily T.", "ET", "Lougheed Town Centre", "SFU Residence",
                LocalDateTime.of(today, LocalTime.of(9, 10)), 4, 3, 5, 30, 91,
                "Hyundai Kona EV · White", 5.0, "EV — chill music, no eating in car please."));
        add(new Ride(nextId(), "Daniel K.", "DK", "Coquitlam Central", "SFU Burnaby — Convocation Mall",
                LocalDateTime.of(today, LocalTime.of(7, 50)), 3, 0, 6, 35, 68,
                "Mazda 3 · Red", 4.7, null));
    }

    private String nextId() {
        return "r" + nextId.getAndIncrement();
    }

    private void add(Ride ride) {
        rides.add(ride);
    }

    /** All rides, in insertion order. */
    public List<Ride> findAll() {
        return new ArrayList<>(rides);
    }

    public Ride first() {
        return rides.get(0);
    }

    /**
     * Filter by a free-text query (pickup, destination, or driver) and sort.
     * Mirrors the search + sort behaviour of the prototype's available-rides page.
     *
     * @param query optional search text (may be null/blank)
     * @param sort  one of "Departure", "Price", "Eco-Score", "Rating"
     */
    public List<Ride> search(String query, String sort) {
        String q = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);

        List<Ride> result = new ArrayList<>();
        for (Ride r : rides) {
            String haystack = (r.getFrom() + " " + r.getTo() + " " + r.getDriver()).toLowerCase(Locale.ROOT);
            if (haystack.contains(q)) {
                result.add(r);
            }
        }
        result.sort(comparatorFor(sort));
        return result;
    }

    private Comparator<Ride> comparatorFor(String sort) {
        if (sort == null) {
            sort = "Departure";
        }
        return switch (sort) {
            case "Price" -> Comparator.comparingInt(Ride::getPrice);
            case "Eco-Score" -> Comparator.comparingInt(Ride::getEcoScore).reversed();
            case "Rating" -> Comparator.comparingDouble(Ride::getRating).reversed();
            default -> Comparator.comparing(Ride::getDepartAt);
        };
    }

    /** Publish a new ride offered by a driver; returns the created ride. */
    public Ride create(String driver, String from, String to, LocalDateTime departAt,
                       int seats, int price, String notes) {
        String initials = initialsOf(driver);
        int points = seats * 8 + 5;
        int ecoScore = Math.min(95, 55 + seats * 8);
        Ride ride = new Ride(nextId(), driver, initials, from, to, departAt,
                seats, 0, price, points, ecoScore, "Your vehicle", 5.0,
                (notes == null || notes.isBlank()) ? null : notes.trim());
        add(ride);
        return ride;
    }

    private String initialsOf(String name) {
        if (name == null || name.isBlank()) {
            return "You";
        }
        StringBuilder sb = new StringBuilder();
        for (String part : name.trim().split("\\s+")) {
            if (!part.isEmpty() && sb.length() < 2) {
                sb.append(Character.toUpperCase(part.charAt(0)));
            }
        }
        return sb.toString();
    }
}

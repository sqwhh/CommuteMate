package project.group1.commutemate.trips.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Trip{
    private Long id; // unique
    private String driverId;
    private String startLocation;
    private String destination;
    private String departureTime;
    private int seatsAvailable;
    private TripStatus status;
    private final List<String> riderIds = new ArrayList<>();

    /**
     * Empty constructor for frameworks
     */
    public Trip(){ }

    /**
     * Full constructor used for creating a trip
     */
    public Trip(
            Long id,
            String driverId,
            String startLocation,
            String destination,
            String departureTime,
            int seatsAvailable,
            TripStatus status
        ){
        this.id = id;
        this.driverId = driverId;
        this.startLocation = startLocation;
        this.destination = destination;
        this.departureTime = departureTime;
        this.seatsAvailable = seatsAvailable;
        this.status = status;
    }

    public Long getId(){
        return id;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getStartLocation(){
        return startLocation;
    }

    public String getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getFormattedDepartureTime() {
        if (departureTime == null || departureTime.isBlank()) {
            return "";
        }
        LocalDateTime dateTime = LocalDateTime.parse(departureTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy 'at' h:mm a");

        return dateTime.format(formatter);
    }

    public int getSeatsAvailable(){
        return seatsAvailable;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public List<String> getRiderIds() {
        return Collections.unmodifiableList(riderIds);
    }

    public boolean hasRider(String riderId) {
        return riderIds.contains(riderId);
    }

    public void addRider(String riderId) {
        riderIds.add(riderId);
        seatsAvailable--;
    }

    /**
     * states of a trip
     */
    public enum TripStatus{
        CREATED,
        CONFIRMED,
        COMPLETED,
        CANCELLED
    }
}
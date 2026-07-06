package project.group1.commutemate.trips.model;

public class Trip{
    private Long id; // unique
    private String startLocation;
    private String destination;
    private String departureTime;
    private int seatsAvailable;
    private TripStatus status;

    /**
     * Empty constructor for frameworks
     */
    public Trip(){ }

    /**
     * Full constructor used for creating a trip
     */
    public Trip(
            Long id,
            String startLocation,
            String destination,
            String departureTime,
            int seatsAvailable,
            TripStatus status
        ){
        this.id = id;
        this.startLocation = startLocation;
        this.destination = destination;
        this.departureTime = departureTime;
        this.seatsAvailable = seatsAvailable;
        this.status = status;
    }

    public Long getId(){
        return id;
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

    public int getSeatsAvailable(){
        return seatsAvailable;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    /**
     * states of a trip
     */
    public enum TripStatus{
        CREATED,
        CONFIRMED,
        CANCELLED
    }
}
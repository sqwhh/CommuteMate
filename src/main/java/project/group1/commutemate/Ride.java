package project.group1.commutemate;

import project.group1.commutemate.trips.model.Trip.TripStatus;

/**
 * TEMPORARY STUB for Epic 4 development
 */
public class Ride {

    private String id;
    private String driverId;
    private String riderId;
    private TripStatus status;

    public Ride(String id, String driverId, String riderId) {
        this.id = id;
        this.driverId = driverId;
        this.riderId = riderId;
        this.status = TripStatus.CREATED;
    }

    public String getId() {
        return id;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getRiderId() {
        return riderId;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }
}
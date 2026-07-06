package project.group1.commutemate;

/**
 * !! TEMPORARY STUB for Epic 4 development !!
 * [swapping this out for the real entity straightforward later]
 */
public class Ride {
    public enum Status {
        PENDING,
        CONFIRMED,
        COMPLETED
    }

    private String id;
    private String driverId;
    private String riderId;
    private Status status;

    public Ride(String id, String driverId, String riderId) {
        this.id = id;
        this.driverId = driverId;
        this.riderId = riderId;
        this.status = Status.PENDING;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
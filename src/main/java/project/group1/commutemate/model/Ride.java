package project.group1.commutemate.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * A carpool ride offered by a driver.
 * Ported from the prototype's {@code mock-rides.ts} data shape.
 */
public class Ride {

    private static final DateTimeFormatter TIME_FMT =
            DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

    private String id;
    private String driver;
    private String driverInitials;
    private String from;
    private String to;
    private LocalDateTime departAt;
    private int seats;
    private int seatsTaken;
    private int price;
    private int points;
    private int ecoScore;
    private String car;
    private double rating;
    private String notes;

    public Ride() {
    }

    public Ride(String id, String driver, String driverInitials, String from, String to,
                LocalDateTime departAt, int seats, int seatsTaken, int price, int points,
                int ecoScore, String car, double rating, String notes) {
        this.id = id;
        this.driver = driver;
        this.driverInitials = driverInitials;
        this.from = from;
        this.to = to;
        this.departAt = departAt;
        this.seats = seats;
        this.seatsTaken = seatsTaken;
        this.price = price;
        this.points = points;
        this.ecoScore = ecoScore;
        this.car = car;
        this.rating = rating;
        this.notes = notes;
    }

    /** Seats still open for booking. */
    public int getSeatsLeft() {
        return Math.max(0, seats - seatsTaken);
    }

    public boolean isFull() {
        return getSeatsLeft() == 0;
    }

    /** Departure time formatted like "8:15 AM" for display. */
    public String getDepartTime() {
        return departAt == null ? "" : departAt.format(TIME_FMT);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDriverInitials() {
        return driverInitials;
    }

    public void setDriverInitials(String driverInitials) {
        this.driverInitials = driverInitials;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public LocalDateTime getDepartAt() {
        return departAt;
    }

    public void setDepartAt(LocalDateTime departAt) {
        this.departAt = departAt;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getSeatsTaken() {
        return seatsTaken;
    }

    public void setSeatsTaken(int seatsTaken) {
        this.seatsTaken = seatsTaken;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getEcoScore() {
        return ecoScore;
    }

    public void setEcoScore(int ecoScore) {
        this.ecoScore = ecoScore;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

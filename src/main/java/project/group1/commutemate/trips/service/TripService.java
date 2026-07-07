package project.group1.commutemate.trips.service;

import org.springframework.stereotype.Service;
import project.group1.commutemate.trips.dto.CreateTripRequest;
import project.group1.commutemate.trips.model.Trip;
import project.group1.commutemate.trips.model.Trip.TripStatus;
import project.group1.commutemate.trips.repository.TripRepository;
import java.time.format.DateTimeParseException;
import project.group1.commutemate.RewardService;

import java.time.LocalDateTime;
import java.util.List;


/**
 * TripService
 * 
 * Contraller calls this class and it talks to rep/db
 */
@Service
public class TripService {
    private final TripRepository tripRepository;
    private final RewardService rewardService; 

    // constructor enjection
    public TripService(TripRepository tripRepository, RewardService rewardService){
        this.tripRepository = tripRepository;
        this.rewardService = rewardService;
    }

    // returns all trips
    public List<Trip> getAllTrips(){
        return tripRepository.findAll();
    }

    // returns trip by id 
    public Trip getTrip(Long id){
        return tripRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Trip not found"));
    }

    // creates a new trip from the form data
    public Trip createTrip(CreateTripRequest request){
        validateCreateTripRequest(request);

        Trip trip = new Trip(
                null,
                request.getDriverId().trim(),
                request.getStartLocation(),
                request.getDestination(),
                request.getDepartureTime(),
                request.getSeatsAvailable(),
                TripStatus.CREATED
        );

        return tripRepository.save(trip);
    }

    // validets if it's possible to join a trip
    public void joinTrip(Long id, String riderId) {
        Trip trip = getTrip(id);
        validateJoinTripRequest(trip, riderId);

        trip.addRider(riderId.trim());
        trip.setStatus(TripStatus.CONFIRMED);
    }

    // completes a trip by id
    public void completeTrip(Long id, String currentUser) {
        Trip trip = getTrip(id);
        validateDriverAction(trip, currentUser);

        if (trip.getStatus() == TripStatus.CANCELLED) {
            throw new IllegalArgumentException("Cancelled trips cannot be completed.");
        }

        if (trip.getStatus() == TripStatus.COMPLETED) {
            throw new IllegalArgumentException("Trip is already completed.");
        }

        if (trip.getStatus() != TripStatus.CONFIRMED) {
            throw new IllegalArgumentException("Only confirmed trips can be completed.");
        }

        if (trip.getRiderIds().isEmpty()) {
            throw new IllegalArgumentException("Trip must have at least one rider before it can be completed.");
        }

        trip.setStatus(TripStatus.COMPLETED);
        rewardService.awardPointsForCompletedRide(trip.getDriverId());
    }           

    // updates a trip to confirmed
    public void confirmTrip(Long id, String currentUser) {
        Trip trip = getTrip(id);
        validateDriverAction(trip, currentUser);

        if (trip.getStatus() == TripStatus.CANCELLED) {
            throw new IllegalArgumentException("Cancelled trips cannot be confirmed.");
        }

        if (trip.getStatus() == TripStatus.COMPLETED) {
            throw new IllegalArgumentException("Completed trips cannot be confirmed.");
        }

        if (trip.getRiderIds().isEmpty()) {
            throw new IllegalArgumentException("Trip must have at least one rider before it can be confirmed.");
        }

        trip.setStatus(TripStatus.CONFIRMED);
    }

    // updates a trip to cancelled 
    public void cancelTrip(Long id, String currentUser) {
        Trip trip = getTrip(id);
        validateDriverAction(trip, currentUser);

        if (trip.getStatus() == TripStatus.COMPLETED) {
            throw new IllegalArgumentException("Completed trips cannot be cancelled.");
        }

        trip.setStatus(TripStatus.CANCELLED);
    }

    private void validateDriverAction(Trip trip, String currentUser) {
        if (currentUser == null || !currentUser.equals(trip.getDriverId())) {
            throw new IllegalArgumentException("Only the driver can do this action.");
        }
    }

    public List<Trip> getTripsForUser(String userId) {
        return tripRepository.findAll()
                .stream()
                .filter(trip -> trip.getDriverId().equals(userId)
                        || trip.getRiderIds().contains(userId))
                .toList();
    }

    // validators
    // validates if it's possble to create a trip
    private void validateCreateTripRequest(CreateTripRequest request) {
        if (request.getDriverId() == null || request.getDriverId().isBlank()) {
            throw new IllegalArgumentException("Driver ID is required.");
        }

        if (request.getStartLocation() == null || request.getStartLocation().isBlank()) {
            throw new IllegalArgumentException("Start location is required.");
        }

        if (request.getDestination() == null || request.getDestination().isBlank()) {
            throw new IllegalArgumentException("Destination is required.");
        }

        if (request.getDepartureTime() == null || request.getDepartureTime().isBlank()) {
            throw new IllegalArgumentException("Departure time is required.");
        }

        validateDepartureTime(request.getDepartureTime());

        if (request.getSeatsAvailable() < 1) {
            throw new IllegalArgumentException("Seats available must be at least 1.");
        }
    }

    private void validateDepartureTime(String departureTime) {
        try {
            LocalDateTime parsedDepartureTime = LocalDateTime.parse(departureTime);

            if (parsedDepartureTime.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Departure time must be in the future.");
            }
        } catch (DateTimeParseException exception) {
            throw new IllegalArgumentException("Departure time format is invalid.");
        }
    }

    private void validateJoinTripRequest(Trip trip, String riderId) {
        if (riderId == null || riderId.isBlank()) {
            throw new IllegalArgumentException("Rider ID is required.");
        }

        String cleanRiderId = riderId.trim();

        if (trip.getStatus() == TripStatus.CANCELLED) {
            throw new IllegalArgumentException("Cancelled trips cannot be joined.");
        }

        if (trip.getStatus() == TripStatus.COMPLETED) {
            throw new IllegalArgumentException("Completed trips cannot be joined.");
        }

        if (trip.getSeatsAvailable() < 1) {
            throw new IllegalArgumentException("No seats available.");
        }

        if (cleanRiderId.equals(trip.getDriverId())) {
            throw new IllegalArgumentException("Driver cannot join their own trip.");
        }

        if (trip.hasRider(cleanRiderId)) {
            throw new IllegalArgumentException("This rider already joined the trip.");
        }
    }
}
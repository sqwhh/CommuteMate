package project.group1.commutemate.trips.service;

import org.springframework.stereotype.Service;
import project.group1.commutemate.trips.dto.CreateTripRequest;
import project.group1.commutemate.trips.model.Trip;
import project.group1.commutemate.trips.model.Trip.TripStatus;
import project.group1.commutemate.trips.repository.TripRepository;
import java.util.List;


/**
 * TripService
 * 
 * Contraller calls this class and it talks to rep/db
 */
@Service
public class TripService {
    private final TripRepository tripRepository;

    // constructor enjection
    public TripService(TripRepository tripRepository){
        this.tripRepository = tripRepository;
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
                request.getStartLocation(),
                request.getDestination(),
                request.getDepartureTime(),
                request.getSeatsAvailable(),
                TripStatus.CREATED
        );

        return tripRepository.save(trip);
    }

    // validator
    private void validateCreateTripRequest(CreateTripRequest request) {
        if (request.getStartLocation() == null || request.getStartLocation().isBlank()) {
            throw new IllegalArgumentException("Start location is required.");
        }

        if (request.getDestination() == null || request.getDestination().isBlank()) {
            throw new IllegalArgumentException("Destination is required.");
        }

        if (request.getDepartureTime() == null || request.getDepartureTime().isBlank()) {
            throw new IllegalArgumentException("Departure time is required.");
        }

        if (request.getSeatsAvailable() < 1) {
            throw new IllegalArgumentException("Seats available must be at least 1.");
        }
    }

    // updates a trip to confirmed
    public void confirmTrip(Long id){
        Trip trip = getTrip(id);
        trip.setStatus(TripStatus.CONFIRMED);
    }

    // updates a trip to cancelled 
    public void cancelTrip(Long id){
        Trip trip = getTrip(id);
        trip.setStatus(TripStatus.CANCELLED);
    }
}
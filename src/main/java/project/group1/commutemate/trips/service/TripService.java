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
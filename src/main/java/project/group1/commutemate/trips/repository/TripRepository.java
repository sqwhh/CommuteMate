package project.group1.commutemate.trips.repository;

import org.springframework.stereotype.Repository;
import project.group1.commutemate.trips.model.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * TripRepository
 * 
 * Temporary simple storage of trips. 
 * Will be changed with database in the future
 */
@Repository
public class TripRepository {

    // list of all trips
    private final List<Trip> trips = new ArrayList<>();

    // id counter
    private Long nextId = 1L;

    // reutrns all trips that are stored in memeory
    public List<Trip> findAll(){
        return trips;
    }

    // finds trip by id
    public Optional<Trip> findById(Long id){
        return trips.stream().filter(trip -> trip.getId().equals(id)).findFirst();
    }

    //saves trip with unique id
    public Trip save(Trip trip){
        Trip savedTrip = new Trip(
                nextId,
                trip.getDriverId(),
                trip.getStartLocation(),
                trip.getDestination(),
                trip.getDepartureTime(),
                trip.getSeatsAvailable(),
                trip.getStatus()
        );
        trips.add(savedTrip);
        nextId++;

        return savedTrip;
    }
}
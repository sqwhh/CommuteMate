package project.group1.commutemate;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TEMPORARY in-memory store
 */
@Component
public class RideStore {

    private final Map<String, Ride> rides = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public Ride save(String driverId, String riderId) {
        String id = String.valueOf(idCounter.getAndIncrement());
        Ride ride = new Ride(id, driverId, riderId);
        rides.put(id, ride);
        return ride;
    }

    public Ride findById(String id) {
        return rides.get(id);
    }
}
package project.group1.commutemate;

import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Epic 4: Incentives & Rewards
 * Uses the RideStore stub for now. Once Epic 3 has a real persisted
 * Ride entity with a status field, point this controller at that
 * instead of RideStore.
 */
@RestController
public class RewardController {

    private final RideStore rideStore;
    private final Map<String, Integer> pointsByDriver = new ConcurrentHashMap<>();

    public RewardController(RideStore rideStore) {
        this.rideStore = rideStore;
    }

    // Helper endpoint just for testing — creates a ride in PENDING state.
    // Epic 3's real "create ride" flow should replace the need for this
    @PostMapping("/test/rides")
    public Ride createTestRide(@RequestParam String driverId, @RequestParam String riderId) {
        return rideStore.save(driverId, riderId);
    }

    @PostMapping("/rides/{id}/complete")
    public String completeRide(@PathVariable String id) {
        Ride ride = rideStore.findById(id);
        if (ride == null) {
            return "Ride not found";
        }
        if (ride.getStatus() == Ride.Status.COMPLETED) {
            return "Ride already completed — no points awarded";
        }

        ride.setStatus(Ride.Status.COMPLETED);

        int pointsToAward = 10; // flat rate for now; *can evolve into EcoScore formula later
        pointsByDriver.merge(ride.getDriverId(), pointsToAward, Integer::sum);

        return "Ride completed. " + pointsToAward + " points awarded to driver " + ride.getDriverId();
    }

    @GetMapping("/users/{driverId}/points")
    public int getPoints(@PathVariable String driverId) {
        return pointsByDriver.getOrDefault(driverId, 0);
    }
}
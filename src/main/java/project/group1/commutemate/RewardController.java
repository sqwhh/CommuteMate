package project.group1.commutemate;

import org.springframework.web.bind.annotation.*;

/**
 * Epic 4: Incentives & Rewards
 *
 * Thin controller now — all reward logic lives in RewardService
 *
 * !! NOTE: /rides/{id}/complete below is a TEMPORARY manual trigger for test. 
 */
@RestController
public class RewardController {
    private final RideStore rideStore;
    private final RewardService rewardService;

    public RewardController(RideStore rideStore, RewardService rewardService) {
        this.rideStore = rideStore;
        this.rewardService = rewardService;
    }

    // Helper endpoint just for testing - creates a ride in CREATED state
    @PostMapping("/test/rides")
    public Ride createTestRide(@RequestParam String driverId, @RequestParam String riderId) {
        return rideStore.save(driverId, riderId);
    }

    // TEMPORARY manual trigger — see note above. Will be removed once
    // TripService.completeTrip() calls RewardService directly.
    @PostMapping("/rides/{id}/complete")
    public String completeRide(@PathVariable String id) {
        Ride ride = rideStore.findById(id);
        if (ride == null) {
            return "Ride not found";
        }
        if (ride.getStatus() == project.group1.commutemate.trips.model.Trip.TripStatus.COMPLETED) {
            return "Ride already completed — no points awarded";
        }

        ride.setStatus(project.group1.commutemate.trips.model.Trip.TripStatus.COMPLETED);
        int awarded = rewardService.awardPointsForCompletedRide(ride.getDriverId());

        return "Ride completed. " + awarded + " points awarded to driver " + ride.getDriverId();
    }

    @GetMapping("/users/{driverId}/points")
    public int getPoints(@PathVariable String driverId) {
        return rewardService.getPoints(driverId);
    }
}
package project.group1.commutemate;

import org.springframework.web.bind.annotation.*;

/**
 * Epic 4 — Incentives & Rewards.
 *
 * Read-only now. Points are awarded via RewardService, called directly
 * from TripService.completeTrip() when a trip's status changes to
 * COMPLETED (see proposed change in TripService.java).
 *
 * The previous /rides/{id}/complete manual trigger and the temporary
 * Ride/RideStore stub have been removed now that Trip/TripRepository
 * are the real thing.
 */
@RestController
public class RewardController {

    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/users/{driverId}/points")
    public int getPoints(@PathVariable String driverId) {
        return rewardService.getPoints(driverId);
    }
}
package project.group1.commutemate;

import java.util.List;

import org.springframework.stereotype.Service;

import project.group1.commutemate.model.Ride;
import project.group1.commutemate.service.RideService;

/**
 * Epic 4 — Incentives & Rewards.
 *
 * ============================== DRAFT / TENTATIVE ==============================
 * This is a draft re-implementation adapted to the current mock Ride/RideService
 * model (points and ecoScore computed by formula at ride creation, no trip
 * completion event). The original Epic 4 implementation awarded a flat 10
 * points when a real Trip transitioned to COMPLETED — that logic still exists
 * on the feature/epic-4-rewards branch and the backup/master-epic4-5-trip
 * branch, built against the real Trip/TripService model.
 *
 * TEAM DECISION NEEDED: are we keeping the mock Ride model (this file) or
 * bringing back the real Trip/TripStatus/completeTrip() flow for Iteration 2?
 * See meeting notes — flagged as unresolved as of this commit.
 * ================================================================================
 *
 * For now, this aggregates a driver's total points/eco-score across all their
 * published rides, since Profile.points / Profile.ecoScore were previously
 * hardcoded to 0 in CurrentUserService with no real calculation behind them.
 */
@Service
public class RewardService {

    private final RideService rideService;

    public RewardService(RideService rideService) {
        this.rideService = rideService;
    }

    /** Sum of points across all rides this person has published as a driver. */
    public int totalPointsForDriver(String driverFullName) {
        List<Ride> rides = rideService.findByDriver(driverFullName);
        int total = 0;
        for (Ride ride : rides) {
            total += ride.getPoints();
        }
        return total;
    }

    /**
     * Average eco-score across this driver's published rides, rounded down.
     * Returns 0 if the driver has no rides yet.
     */
    public int averageEcoScoreForDriver(String driverFullName) {
        List<Ride> rides = rideService.findByDriver(driverFullName);
        if (rides.isEmpty()) {
            return 0;
        }
        int sum = 0;
        for (Ride ride : rides) {
            sum += ride.getEcoScore();
        }
        return sum / rides.size();
    }
}

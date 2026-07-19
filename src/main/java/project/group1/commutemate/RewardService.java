package project.group1.commutemate;

import java.util.List;

import org.springframework.stereotype.Service;

import project.group1.commutemate.model.Ride;
import project.group1.commutemate.service.RideService;

/**
 * Epic 4 — Incentives & Rewards.
 *
 * ============================== DRAFT / TENTATIVE ==============================
 * Adapted to the Ride model as it evolves. Points and ecoScore are computed
 * by formula at ride creation, not by a trip-completion event. See meeting
 * notes for the ongoing team decision on the reward-triggering model.
 * ================================================================================
 *
 * NOTE (as of Epic 5 PR #10 merge): RideService.findByDriver(String) was
 * removed. The only driver-scoped lookup available now is
 * findUpcomingByDriverEmail(String), which only returns rides that have NOT
 * yet departed. That means completed/past rides currently do NOT count
 * toward a driver's totals below — this is a known limitation, not a bug.
 * A proper fix would add a RideRepository.findByDriverEmailIgnoreCase(...)
 * method (all rides, any time) — flagged for Roman/Epic 5 to add, since
 * RideRepository is owned by that epic and under active development.
 */
@Service
public class RewardService {

    private final RideService rideService;

    public RewardService(RideService rideService) {
        this.rideService = rideService;
    }

    /** Sum of points across this driver's upcoming published rides. */
    public int totalPointsForDriver(String driverEmail) {
        List<Ride> rides = rideService.findUpcomingByDriverEmail(driverEmail);
        int total = 0;
        for (Ride ride : rides) {
            total += ride.getPoints();
        }
        return total;
    }

    /**
     * Average eco-score across this driver's upcoming published rides,
     * rounded down. Returns 0 if the driver has no rides yet.
     */
    public int averageEcoScoreForDriver(String driverEmail) {
        List<Ride> rides = rideService.findUpcomingByDriverEmail(driverEmail);
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
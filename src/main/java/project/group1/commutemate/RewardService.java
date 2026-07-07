package project.group1.commutemate;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RewardService
 *
 * Owns all reward/points logic. Moved out of RewardController per review
 * feedback, so it can eventually be called directly from TripService
 * when a trip's status changes to COMPLETED — instead of going through
 * a separate /rides/{id}/complete endpoint.
 */
@Service
public class RewardService {

    private final Map<String, Integer> pointsByDriver = new ConcurrentHashMap<>();

    /**
     * Awards the standard point amount to a driver.
     * Intended to be called once, at the moment a trip is marked completed
     * (e.g. from TripService.completeTrip())
     */
    public int awardPointsForCompletedRide(String driverId) {
        pointsByDriver.merge(driverId, RewardConst.POINTS_PER_COMPLETED_RIDE, Integer::sum);
        return RewardConst.POINTS_PER_COMPLETED_RIDE;
    }

    public int getPoints(String driverId) {
        return pointsByDriver.getOrDefault(driverId, 0);
    }
}
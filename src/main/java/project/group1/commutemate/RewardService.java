package project.group1.commutemate;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RewardService: owns all reward/points logic
 */
@Service
public class RewardService {
    private final Map<String, Integer> pointsByDriver = new ConcurrentHashMap<>();

    /**
     * Awards the standard point amount to a driver.
     * Intended to be called once, at the moment a trip is marked completed
     */
    public int awardPointsForCompletedRide(String driverId) {
        pointsByDriver.merge(driverId, RewardConst.POINTS_PER_COMPLETED_RIDE, Integer::sum);
        return RewardConst.POINTS_PER_COMPLETED_RIDE;
    }

    public int getPoints(String driverId) {
        return pointsByDriver.getOrDefault(driverId, 0);
    }
}
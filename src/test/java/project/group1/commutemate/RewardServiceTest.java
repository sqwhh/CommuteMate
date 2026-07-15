package project.group1.commutemate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import project.group1.commutemate.model.Ride;
import project.group1.commutemate.service.RideService;

/**
 * Unit tests for {@link RewardService}.
 *
 * These test the draft mock-Ride-based implementation of Epic 4.
 * RideService is mocked so these tests don't depend on its seeded demo data
 * or on a running Spring context.
 */
@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    private RideService rideService;

    private RewardService rewardService;

    @BeforeEach
    void setUp() {
        rewardService = new RewardService(rideService);
    }

    private Ride ride(String driver, int points, int ecoScore) {
        return new Ride(
                "r1", driver, "XX",
                "Somewhere", "SFU Burnaby",
                LocalDateTime.now(), 3, 0, 4,
                points, ecoScore,
                "Test Car", 5.0, null
        );
    }

    @Test
    void totalPointsForDriver_sumsPointsAcrossAllTheirRides() {
        when(rideService.findByDriver("Alex Chen")).thenReturn(List.of(
                ride("Alex Chen", 20, 80),
                ride("Alex Chen", 15, 70)
        ));

        int total = rewardService.totalPointsForDriver("Alex Chen");

        assertEquals(35, total);
    }

    @Test
    void totalPointsForDriver_returnsZero_whenDriverHasNoRides() {
        when(rideService.findByDriver("Nobody")).thenReturn(List.of());

        int total = rewardService.totalPointsForDriver("Nobody");

        assertEquals(0, total);
    }

    @Test
    void averageEcoScoreForDriver_averagesAcrossRides() {
        when(rideService.findByDriver("Priya S.")).thenReturn(List.of(
                ride("Priya S.", 10, 80),
                ride("Priya S.", 10, 90)
        ));

        int avg = rewardService.averageEcoScoreForDriver("Priya S.");

        assertEquals(85, avg);
    }

    @Test
    void averageEcoScoreForDriver_returnsZero_whenDriverHasNoRides() {
        when(rideService.findByDriver("Nobody")).thenReturn(List.of());

        int avg = rewardService.averageEcoScoreForDriver("Nobody");

        assertEquals(0, avg);
    }

    @Test
    void averageEcoScoreForDriver_roundsDown_onUnevenDivision() {
        // (80 + 81) / 2 = 80.5 -> integer division rounds down to 80
        when(rideService.findByDriver("Marcus L.")).thenReturn(List.of(
                ride("Marcus L.", 10, 80),
                ride("Marcus L.", 10, 81)
        ));

        int avg = rewardService.averageEcoScoreForDriver("Marcus L.");

        assertEquals(80, avg);
    }
}

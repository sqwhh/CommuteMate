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
 * RideService is mocked so these tests don't depend on its seeded demo data
 * or a running Spring context. Keyed by driver email, matching
 * RideService.findUpcomingByDriverEmail (see Epic 5 PR #10).
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

    private Ride ride(String driverEmail, int points, int ecoScore) {
        return new Ride(
                driverEmail, "Test Driver", "TD",
                "Somewhere", "SFU Burnaby",
                LocalDateTime.now().plusDays(1), 3, 0, 4,
                points, ecoScore,
                "Test Car", 5.0, null
        );
    }

    @Test
    void totalPointsForDriver_sumsPointsAcrossAllTheirRides() {
        when(rideService.findUpcomingByDriverEmail("alex@sfu.ca")).thenReturn(List.of(
                ride("alex@sfu.ca", 20, 80),
                ride("alex@sfu.ca", 15, 70)
        ));

        int total = rewardService.totalPointsForDriver("alex@sfu.ca");

        assertEquals(35, total);
    }

    @Test
    void totalPointsForDriver_returnsZero_whenDriverHasNoRides() {
        when(rideService.findUpcomingByDriverEmail("nobody@sfu.ca")).thenReturn(List.of());

        int total = rewardService.totalPointsForDriver("nobody@sfu.ca");

        assertEquals(0, total);
    }

    @Test
    void averageEcoScoreForDriver_averagesAcrossRides() {
        when(rideService.findUpcomingByDriverEmail("priya@sfu.ca")).thenReturn(List.of(
                ride("priya@sfu.ca", 10, 80),
                ride("priya@sfu.ca", 10, 90)
        ));

        int avg = rewardService.averageEcoScoreForDriver("priya@sfu.ca");

        assertEquals(85, avg);
    }

    @Test
    void averageEcoScoreForDriver_returnsZero_whenDriverHasNoRides() {
        when(rideService.findUpcomingByDriverEmail("nobody@sfu.ca")).thenReturn(List.of());

        int avg = rewardService.averageEcoScoreForDriver("nobody@sfu.ca");

        assertEquals(0, avg);
    }

    @Test
    void averageEcoScoreForDriver_roundsDown_onUnevenDivision() {
        // (80 + 81) / 2 = 80.5 -> integer division rounds down to 80
        when(rideService.findUpcomingByDriverEmail("marcus@sfu.ca")).thenReturn(List.of(
                ride("marcus@sfu.ca", 10, 80),
                ride("marcus@sfu.ca", 10, 81)
        ));

        int avg = rewardService.averageEcoScoreForDriver("marcus@sfu.ca");

        assertEquals(80, avg);
    }
}
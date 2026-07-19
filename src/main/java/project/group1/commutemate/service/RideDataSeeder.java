package project.group1.commutemate.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import project.group1.commutemate.model.Ride;
import project.group1.commutemate.repository.RideRepository;

/** Optional local/demo ride data. Disabled unless app.seed-demo-data is true. */
@Component
@ConditionalOnProperty(name = "app.seed-demo-data", havingValue = "true")
public class RideDataSeeder implements CommandLineRunner {

    private final RideRepository rideRepository;
    private final Clock clock;

    public RideDataSeeder(RideRepository rideRepository, Clock clock) {
        this.rideRepository = rideRepository;
        this.clock = clock;
    }

    @Override
    public void run(String... args) {
        LocalDateTime now = LocalDateTime.now(clock);
        if (rideRepository.existsByDepartAtAfter(now)) {
            return;
        }

        LocalDate day = LocalDate.now(clock).plusDays(1);
        rideRepository.saveAll(List.of(
                ride("priya@sfu.ca", "Priya S.", "PS", "Metrotown Station", "SFU Burnaby — AQ",
                        day, 8, 15, 4, 4, 25, 82, "Toyota Corolla · Silver", 4.9,
                        "Leaving from Kiss & Ride, quiet drive."),
                ride("marcus@sfu.ca", "Marcus L.", "ML", "Production Way–University", "SFU Burnaby — West Mall",
                        day, 8, 45, 3, 3, 20, 74, "Honda Civic · Blue", 4.8, null),
                ride("emily@sfu.ca", "Emily T.", "ET", "Lougheed Town Centre", "SFU Residence",
                        day, 9, 10, 4, 5, 30, 91, "Hyundai Kona EV · White", 5.0,
                        "EV — chill music, no eating in car please."),
                ride("daniel@sfu.ca", "Daniel K.", "DK", "Coquitlam Central", "SFU Burnaby — Convocation Mall",
                        day, 7, 50, 3, 6, 35, 68, "Mazda 3 · Red", 4.7, null),
                ride("driver@sfu.ca", "Demo Driver", "DD", "Production Way–University", "SFU Burnaby — AQ",
                        day, 10, 0, 4, 4, 25, 78, "Kia Soul · Grey", 4.8,
                        "Grading demo ride.")));
    }

    private Ride ride(String email, String name, String initials, String from, String to,
                      LocalDate day, int hour, int minute, int seats, int price,
                      int points, int eco, String car, double rating, String notes) {
        return new Ride(email, name, initials, from, to,
                LocalDateTime.of(day, LocalTime.of(hour, minute)), seats, 0,
                price, points, eco, car, rating, notes);
    }
}

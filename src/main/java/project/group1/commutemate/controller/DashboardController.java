package project.group1.commutemate.controller;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import project.group1.commutemate.User.CurrentUserService;
import project.group1.commutemate.model.Profile;
import project.group1.commutemate.model.RequestStatus;
import project.group1.commutemate.model.Ride;
import project.group1.commutemate.model.RideRequest;
import project.group1.commutemate.service.RideCoordinationService;
import project.group1.commutemate.service.RideService;
import project.group1.commutemate.service.TransitService;

/** Rider and driver dashboards. */
@Controller
public class DashboardController extends AuthenticatedController {

    private final RideService rideService;
    private final RideCoordinationService coordinationService;
    private final Clock clock;
    private final TransitService transitService;

    public DashboardController(RideService rideService,
                               RideCoordinationService coordinationService,
                               CurrentUserService currentUserService,
                               Clock clock,
                               TransitService transitService) {
        super(currentUserService);
        this.rideService = rideService;
        this.coordinationService = coordinationService;
        this.clock = clock;
        this.transitService = transitService;
    }

    // rider
    @GetMapping("/dashboard/rider")
    public String riderDashboard(@ModelAttribute("profile") Profile profile, Model model) {
        LocalDateTime now = LocalDateTime.now(clock);
        List<RideRequest> requests = coordinationService.findRequestsForRider(profile.getEmail());
        Ride nextConfirmedRide = requests.stream()
                .filter(request -> request.getStatus() == RequestStatus.CONFIRMED)
                .map(RideRequest::getRide)
                .filter(ride -> ride.getDepartAt() != null && ride.getDepartAt().isAfter(now))
                .min((first, second) -> first.getDepartAt().compareTo(second.getDepartAt()))
                .orElse(null);

        List<Ride> upcoming = rideService.findAllUpcoming();
        List<Ride> suggested = upcoming.stream()
                .filter(ride -> !ride.isFull())
                .filter(ride -> !ride.getDriverEmail().equalsIgnoreCase(profile.getEmail()))
                .limit(2)
                .toList();

        model.addAttribute("now", now);
        model.addAttribute("nextRide", nextConfirmedRide);
        model.addAttribute("riderRequests", requests);
        model.addAttribute("suggested", suggested);
        model.addAttribute("availableRideCount",
                upcoming.stream().filter(ride -> !ride.isFull()).count());
        model.addAttribute("transit", transitService.getTransitInfo());
        return "dashboard-rider";
    }

    // driver
    @GetMapping("/dashboard/driver")
    public String driverDashboard(@ModelAttribute("profile") Profile profile, Model model) {
        LocalDateTime now = LocalDateTime.now(clock);
        List<Ride> myRides = rideService.findUpcomingByDriverEmail(profile.getEmail());
        List<RideRequest> requests = coordinationService.findRequestsForDriver(profile.getEmail());

        model.addAttribute("now", now);
        model.addAttribute("myRides", myRides);
        model.addAttribute("driverRequests", requests);
        model.addAttribute("upcomingRideCount", myRides.size());
        model.addAttribute("confirmedRiderCount", requests.stream()
                .filter(request -> request.getStatus() == RequestStatus.CONFIRMED)
                .count());
        model.addAttribute("transit", transitService.getTransitInfo());
        return "dashboard-driver";
    }
}

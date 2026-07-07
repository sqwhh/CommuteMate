package project.group1.commutemate.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import project.group1.commutemate.User.CurrentUserService;
import project.group1.commutemate.model.Profile;
import project.group1.commutemate.model.Ride;
import project.group1.commutemate.service.RideService;

/**
 * Rider and driver dashboards (Epic 2).
 */
@Controller
public class DashboardController extends AuthenticatedController {

    private final RideService rideService;

    public DashboardController(RideService rideService, CurrentUserService currentUserService) {
        super(currentUserService);
        this.rideService = rideService;
    }

    @GetMapping("/dashboard/rider")
    public String riderDashboard(Model model) {
        List<Ride> all = rideService.findAll();
        model.addAttribute("nextRide", all.get(0));
        model.addAttribute("suggested", all.subList(1, Math.min(3, all.size())));
        return "dashboard-rider";
    }

    @GetMapping("/dashboard/driver")
    public String driverDashboard(@ModelAttribute("profile") Profile profile, Model model) {
        model.addAttribute("myRides", rideService.findByDriver(profile.getFullName()));
        return "dashboard-driver";
    }

    /** Legacy path from the original scaffold — keep it working. */
    @GetMapping("/rider/dashboard")
    public String legacyRiderDashboard() {
        return "redirect:/dashboard/rider";
    }
}

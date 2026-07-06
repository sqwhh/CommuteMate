package project.group1.commutemate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RideRequestController {

    private static final List<String> rideRequests = new ArrayList<>();

    @GetMapping("/ride-request/new")
    public String newRideRequest() {
        return "ride-request";
    }

    @PostMapping("/ride-request")
    public String createRideRequest(
            @RequestParam String pickupLocation,
            @RequestParam String destination,
            @RequestParam String date,
            @RequestParam String time,
            @RequestParam String seats,
            @RequestParam String notes) {

        String request = pickupLocation + " to " + destination
                + " on " + date
                + " at " + time
                + " | Seats needed: " + seats
                + " | Notes: " + notes;

        rideRequests.add(request);

        return "redirect:/rides";
    }

    @GetMapping("/rides")
    public String viewRides(Model model) {
        model.addAttribute("rideRequests", rideRequests);
        return "rides";
    }
}

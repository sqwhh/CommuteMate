package project.group1.commutemate.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import project.group1.commutemate.User.CurrentUserService;
import project.group1.commutemate.model.Profile;
import project.group1.commutemate.service.RideService;

/**
 * Browse available rides and publish new ones (Epics 3 & 5).
 */
@Controller
public class RidesController extends AuthenticatedController {

    private static final String[] SORT_OPTIONS = {"Departure", "Price", "Eco-Score", "Rating"};

    private final RideService rideService;

    public RidesController(RideService rideService, CurrentUserService currentUserService) {
        super(currentUserService);
        this.rideService = rideService;
    }

    @GetMapping("/rides/available")
    public String available(@RequestParam(name = "q", required = false) String query,
                            @RequestParam(required = false, defaultValue = "Departure") String sort,
                            Model model) {
        model.addAttribute("rides", rideService.search(query, sort));
        model.addAttribute("query", query == null ? "" : query);
        model.addAttribute("sort", sort);
        model.addAttribute("sortOptions", SORT_OPTIONS);
        return "rides-available";
    }

    /** Legacy link target from the original scaffold. */
    @GetMapping("/rides")
    public String legacyRides() {
        return "redirect:/rides/available";
    }

    @GetMapping("/rides/create")
    public String createForm(Model model) {
        return "rides-create";
    }

    /** Legacy link target from the original scaffold. */
    @GetMapping("/ride-request/new")
    public String legacyCreate() {
        return "redirect:/rides/create";
    }

    @PostMapping("/rides/create")
    public String create(@ModelAttribute("profile") Profile profile,
                         @RequestParam String from,
                         @RequestParam String to,
                         @RequestParam String date,
                         @RequestParam String time,
                         @RequestParam(defaultValue = "3") int seats,
                         @RequestParam(defaultValue = "4") int price,
                         @RequestParam(required = false) String notes) {

        LocalDateTime departAt = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
        rideService.create(profile.getFullName(), from, to, departAt, seats, price, notes);
        return "redirect:/dashboard/driver";
    }
}

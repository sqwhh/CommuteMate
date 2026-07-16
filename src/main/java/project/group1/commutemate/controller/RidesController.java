package project.group1.commutemate.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.group1.commutemate.User.CurrentUserService;
import project.group1.commutemate.exception.RideOperationException;
import project.group1.commutemate.model.Profile;
import project.group1.commutemate.model.Ride;
import project.group1.commutemate.service.RideCoordinationService;
import project.group1.commutemate.service.RideService;

/** Browse, inspect, and publish persistent rides. */
@Controller
public class RidesController extends AuthenticatedController {

    private static final String[] SORT_OPTIONS = {"Departure", "Price", "Eco-Score", "Rating"};

    private final RideService rideService;
    private final RideCoordinationService coordinationService;

    public RidesController(RideService rideService,
                           RideCoordinationService coordinationService,
                           CurrentUserService currentUserService) {
        super(currentUserService);
        this.rideService = rideService;
        this.coordinationService = coordinationService;
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

    @GetMapping("/rides/{rideId}")
    public String details(@PathVariable Long rideId,
                          @ModelAttribute("profile") Profile profile,
                          Model model,
                          RedirectAttributes redirect) {
        try {
            Ride ride = rideService.findById(rideId);
            model.addAttribute("ride", ride);
            model.addAttribute("myRequest",
                    coordinationService.findRequestForRider(rideId, profile.getEmail()).orElse(null));
            model.addAttribute("owner", ride.getDriverEmail().equalsIgnoreCase(profile.getEmail()));
            return "ride-details";
        } catch (RideOperationException ex) {
            redirect.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/rides/available";
        }
    }

    @GetMapping("/rides")
    public String legacyRides() {
        return "redirect:/rides/available";
    }

    @GetMapping("/rides/create")
    public String createForm(Model model) {
        model.addAttribute("minimumDate", LocalDate.now().toString());
        return "rides-create";
    }

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
                         @RequestParam(required = false) String notes,
                         RedirectAttributes redirect) {
        try {
            LocalDateTime departAt = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
            rideService.create(profile.getEmail(), profile.getFullName(), from, to,
                    departAt, seats, price, notes);
            redirect.addFlashAttribute("successMessage", "Ride published successfully.");
            return "redirect:/dashboard/driver";
        } catch (DateTimeParseException ex) {
            redirect.addFlashAttribute("errorMessage", "Enter a valid departure date and time.");
            return "redirect:/rides/create";
        } catch (RideOperationException ex) {
            redirect.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/rides/create";
        }
    }
}

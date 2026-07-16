package project.group1.commutemate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.group1.commutemate.User.CurrentUserService;
import project.group1.commutemate.exception.RideOperationException;
import project.group1.commutemate.model.Profile;
import project.group1.commutemate.service.RideCoordinationService;

/** HTTP actions for Epic 5 ride requests. */
@Controller
public class RideRequestController extends AuthenticatedController {

    private final RideCoordinationService coordinationService;

    public RideRequestController(RideCoordinationService coordinationService,
                                 CurrentUserService currentUserService) {
        super(currentUserService);
        this.coordinationService = coordinationService;
    }

    @PostMapping("/rides/{rideId}/requests")
    public String requestSeat(@PathVariable Long rideId,
                              @ModelAttribute("profile") Profile profile,
                              RedirectAttributes redirect) {
        try {
            coordinationService.requestSeat(rideId, profile);
            redirect.addFlashAttribute("successMessage",
                    "Seat request sent. The driver must confirm it before a seat is reserved.");
        } catch (RideOperationException ex) {
            redirect.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/rides/" + rideId;
    }
}

package project.group1.commutemate.trips.controller;

import java.security.Principal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.group1.commutemate.trips.dto.CreateTripRequest;
import project.group1.commutemate.trips.service.TripService;
/**
 * TripController
 * 
 * Handels requests related to trips. 
 * Sends work to TripService and returns Thymeleaf templates.
 */
@Controller
@RequestMapping("/trips")
public class TripController {
    private final TripService tripService;

    /**
     * TripService is injected through the constructor
     */
    public TripController(TripService tripService){
        this.tripService = tripService;
    }

    /**
     * Shows list of all trips that were created.
     * 
     * @param modle stores your trips for the page 
     * @return page that displays the user's trips
     */
    @GetMapping("/my")
    public String showMyTrips(Principal principal, Model model){
        model.addAttribute("trips", tripService.getTripsForUser(principal.getName()));
        model.addAttribute("pageTitle", "My Trips");
        return "trips/my-trips";
    }

    /**
     * Shows the form for creation of a new trip.
     * 
     * @param model stores data for the page.
     * @return new trip form page
     */
    @GetMapping("/new")
    public String showCreateTripForm(Model model){
        model.addAttribute("tripRequest", new CreateTripRequest());
        return "trips/new-trip";
    }

    /**
     * Creates a new trip from the submitted data
     * 
     * @param tripRequest contains thip information form 
     * @return redirect to a just creadeted trip
     */
    @PostMapping
    public String createTrip(
            @ModelAttribute CreateTripRequest tripRequest,
            Principal principal,
            Model model
    ) {
        try {
            tripRequest.setDriverId(principal.getName());
            tripService.createTrip(tripRequest);
            return "redirect:/trips/my";
        } catch (IllegalArgumentException exception) {
            model.addAttribute("tripRequest", tripRequest);
            model.addAttribute("errorMessage", exception.getMessage());
            return "trips/new-trip";
        }
    }

    /**
     * Shows details for one trip by id.
     * 
     * @param model stores the trip data
     * @return the trip detail page
     */
    @GetMapping("/{id}")
    public String showTripDetails(
            @PathVariable Long id,
            Principal principal,
            Model model
    ) {
        model.addAttribute("trip", tripService.getTrip(id));
        model.addAttribute("currentUser", principal.getName());
        return "trips/trip-details";
    }
    

    /**
     * Joins a trip by id and riderId.
     */
    @PostMapping("/{id}/join")
    public String joinTrip(
            @PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            tripService.joinTrip(id, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "You joined this trip successfully.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/trips/" + id;
    }
    
    /**
     * Confirms a trip.
     */
    @PostMapping("/{id}/confirm")
    public String confirmTrip(
            @PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            tripService.confirmTrip(id, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Trip confirmed.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/trips/" + id;
    }

    /**
     * Completes a trip.
     */
    @PostMapping("/{id}/complete")
    public String completeTrip(
            @PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            tripService.completeTrip(id, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Trip completed.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/trips/" + id;
    }

    /**
     * Cancels a trip.
     */
    @PostMapping("/{id}/cancel")
    public String cancelTrip(
            @PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        try {
            tripService.cancelTrip(id, principal.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Trip cancelled.");
        } catch (IllegalArgumentException exception) {
            redirectAttributes.addFlashAttribute("errorMessage", exception.getMessage());
        }

        return "redirect:/trips/" + id;
    }
}
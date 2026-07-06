package project.group1.commutemate.trips.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public String showMyTrips(Model model){
        model.addAttribute("trips", tripService.getAllTrips());
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
    public String createTrip(@ModelAttribute CreateTripRequest tripRequest, Model model){
        try{
            tripService.createTrip(tripRequest);
            return "redirect:/trips/my";
        } 

        catch (IllegalArgumentException exception){
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
    public String showTripDetails(@PathVariable Long id, Model model){
        model.addAttribute("trip", tripService.getTrip(id));
        return "trips/trip-details";
    }
    
    /**
     * Confirms a trip.
     */
    @PostMapping("/{id}/confirm")
    public String confirmTrip(@PathVariable Long id){
        tripService.confirmTrip(id);
        return "redirect:/trips/" + id;
    }
    /**
     * Cancels a trip.
     */
    @PostMapping("/{id}/cancel")
    public String cancelTrip(@PathVariable Long id){
        tripService.cancelTrip(id);
        return "redirect:/trips/" + id;
    }
}
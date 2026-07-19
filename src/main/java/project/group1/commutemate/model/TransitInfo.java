package project.group1.commutemate.model;

import java.util.List;

/** All transit info shown on the dashboard. */
public record TransitInfo(
        boolean apiAvailable,           
        List<BusArrival> arrivals,      
        List<ServiceAlert> alerts) {   
}
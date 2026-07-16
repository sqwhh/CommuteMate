package project.group1.commutemate.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import project.group1.commutemate.exception.RideOperationException;
import project.group1.commutemate.model.Profile;
import project.group1.commutemate.model.RequestStatus;
import project.group1.commutemate.model.Ride;
import project.group1.commutemate.model.RideRequest;
import project.group1.commutemate.repository.RideRepository;
import project.group1.commutemate.repository.RideRequestRepository;

/** Business rules for rider requests and driver decisions. */
@Service
public class RideCoordinationService {

    private final RideRepository rideRepository;
    private final RideRequestRepository requestRepository;

    public RideCoordinationService(RideRepository rideRepository,
                                   RideRequestRepository requestRepository) {
        this.rideRepository = rideRepository;
        this.requestRepository = requestRepository;
    }

    @Transactional
    public RideRequest requestSeat(Long rideId, Profile rider) {
        if (rider == null || !rider.isRiderCapable()) {
            throw new RideOperationException("Your account cannot request rides.");
        }

        Ride ride = rideRepository.findByIdForUpdate(rideId)
                .orElseThrow(() -> new RideOperationException("Ride not found."));

        if (!ride.getDepartAt().isAfter(LocalDateTime.now())) {
            throw new RideOperationException("This ride has already departed.");
        }
        if (ride.getDriverEmail().equalsIgnoreCase(rider.getEmail())) {
            throw new RideOperationException("You cannot request your own ride.");
        }
        if (ride.isFull()) {
            throw new RideOperationException("This ride is full.");
        }

        Optional<RideRequest> existing = requestRepository
                .findByRideIdAndRiderEmailIgnoreCase(rideId, rider.getEmail());
        if (existing.isPresent()) {
            RideRequest request = existing.get();
            if (request.getStatus() == RequestStatus.CANCELLED) {
                request.setStatus(RequestStatus.PENDING);
                request.setRiderName(rider.getFullName());
                return requestRepository.save(request);
            }
            throw new RideOperationException("You already requested this ride.");
        }

        return requestRepository.save(new RideRequest(
                ride, rider.getEmail().trim().toLowerCase(), rider.getFullName().trim()));
    }

    public Optional<RideRequest> findRequestForRider(Long rideId, String riderEmail) {
        if (riderEmail == null || riderEmail.isBlank()) {
            return Optional.empty();
        }
        return requestRepository.findByRideIdAndRiderEmailIgnoreCase(rideId, riderEmail);
    }
}

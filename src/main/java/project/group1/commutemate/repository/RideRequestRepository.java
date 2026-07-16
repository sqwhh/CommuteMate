package project.group1.commutemate.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import project.group1.commutemate.model.RideRequest;

public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {

    Optional<RideRequest> findByRideIdAndRiderEmailIgnoreCase(Long rideId, String riderEmail);

    List<RideRequest> findByRiderEmailIgnoreCaseOrderByCreatedAtDesc(String riderEmail);

    List<RideRequest> findByRideDriverEmailIgnoreCaseOrderByCreatedAtDesc(String driverEmail);
}

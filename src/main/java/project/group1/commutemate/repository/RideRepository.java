package project.group1.commutemate.repository;

import java.util.List;
import java.util.Optional;

import jakarta.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import project.group1.commutemate.model.Ride;

public interface RideRepository extends JpaRepository<Ride, Long> {

    List<Ride> findAllByOrderByDepartAtAsc();

    List<Ride> findByDriverEmailIgnoreCaseOrderByDepartAtAsc(String driverEmail);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from Ride r where r.id = :id")
    Optional<Ride> findByIdForUpdate(@Param("id") Long id);
}

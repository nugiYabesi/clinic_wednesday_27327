package auca.example.clinicSystem.repository;

import auca.example.clinicSystem.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

public interface OfficeRepository extends JpaRepository<Office, UUID> {
    Optional<Office> findByRoomNumberAndLocation(String roomNumber, String location);
    Optional<Office> findByDoctor_Id(UUID doctorId);
    // C3
    @Query("""
            SELECT o.name, o.roomNumber, COUNT(a)
            FROM Appointment a
            JOIN a.doctor d
            JOIN d.office o
            GROUP BY o.id, o.name, o.roomNumber
            ORDER BY COUNT(a) DESC
            """)
    List<Object[]> findBusiestOffice(Pageable pageable);
}

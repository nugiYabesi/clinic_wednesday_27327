package auca.example.clinicSystem.repository;

import auca.example.clinicSystem.model.Office;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface OfficeRepository extends JpaRepository<Office, UUID> {
    Optional<Office> findByRoomNumberAndLocation(String roomNumber, String location);
    Optional<Office> findByDoctor_Id(UUID doctorId);

    @Query("""
    SELECT officeObj.location, officeObj.roomNumber, COUNT(appt)
    FROM Appointment appt
    JOIN appt.doctor doc
    JOIN doc.office officeObj
    GROUP BY objectObj.id, officeObj.location, officeObj.roomNumber
    ORDER BY COUNT(appt) DESC
    """)
    List<Object[]> findBusiestOffice(Pageable pageable);

    @Query("SELECT off FROM Office off WHERE NOT EXISTS (SELECT doc FROM Doc WHERE doc.office = off) ORDER BY off.roomNumber ASC")
    List<Office> retrieveUnoccupiedOffices();

    Optional<Office> findByRoomNumber(String roomNumber);
}

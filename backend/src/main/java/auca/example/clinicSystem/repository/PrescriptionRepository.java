package auca.example.clinicSystem.repository;

import auca.example.clinicSystem.model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription,UUID> {
    List<Prescription> findByAppointmentId(UUID appointmentId);


    @Query("SELECT p FROM Prescription p WHERE p.appointment.patient.ssNumber = :ssNumber ORDER BY p.prescriptionDate DESC")
    List<Prescription> findByPrescriptionId(@Param("ssNumber") String ssNumber);
}

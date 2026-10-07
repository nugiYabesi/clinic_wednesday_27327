package auca.example.clinicSystem.repository;

import auca.example.clinicSystem.model.Appointment;
import auca.example.clinicSystem.model.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    List<Appointment> findByStatusOrderByScheduledAtAsc(
            AppointmentStatus status
    );

    // A3
    List<Appointment> findByScheduledAtBetweenOrderByScheduledAtAsc(
            LocalDateTime start,
            LocalDateTime end
    );

    // A4
    boolean existsByDoctorAndScheduledAtBetweenAndStatusNot(
            auca.example.clinicSystem.model.Doctor doctor,
            LocalDateTime start,
            LocalDateTime end,
            AppointmentStatus status
    );

    // C1
    @Query("""
            SELECT a.status, COUNT(a)
            FROM Appointment a
            GROUP BY a.status
            """)
    List<Object[]> countAppointmentsByStatus();

    // C4
    @Modifying
    @Query("""
            UPDATE Appointment a
            SET a.status = auca.example.clinicSystem.model.enums.AppointmentStatus.CANCELLED
            WHERE a.doctor.id = :doctorId
              AND a.scheduledAt >= :start
              AND a.scheduledAt < :end
              AND a.status <> auca.example.clinicSystem.model.enums.AppointmentStatus.COMPLETED
            """)
    int cancelAppointmentsForDoctorOnDate(
            @Param("doctorId") UUID doctorId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

    // Bonus
    @Modifying
    @Query("""
            DELETE FROM Appointment a
            WHERE a.status = auca.example.clinicSystem.model.enums.AppointmentStatus.CANCELLED
              AND a.scheduledAt < :before
            """)
    int deleteCancelledBefore(
            @Param("before") LocalDateTime before
    );
}

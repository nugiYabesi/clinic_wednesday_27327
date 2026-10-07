package auca.example.clinicSystem.service;

import auca.example.clinicSystem.model.Appointment;
import auca.example.clinicSystem.model.Doctor;
import auca.example.clinicSystem.model.enums.AppointmentStatus;
import auca.example.clinicSystem.repository.AppointmentRepository;
import auca.example.clinicSystem.repository.DoctorRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointments;
    private final DoctorRepository doctors;

    public AppointmentService(
            AppointmentRepository appointments,
            DoctorRepository doctors
    ) {
        this.appointments = appointments;
        this.doctors = doctors;
    }

    // A4 / existing save endpoint
    public Appointment save(Appointment appointment) {

        Doctor doctor = appointment.getDoctor();

        LocalDate date = appointment.getScheduledAt().toLocalDate();

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        boolean alreadyBooked =
                appointments.existsByDoctorAndScheduledAtBetweenAndStatusNot(
                        doctor,
                        start,
                        end,
                        AppointmentStatus.CANCELLED
                );

        if (alreadyBooked) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Doctor is already booked on that date"
            );
        }

        return appointments.save(appointment);
    }

    // A2
    @Transactional(readOnly = true)
    public List<Appointment> getByStatus(
            AppointmentStatus status
    ) {

        return appointments.findByStatusOrderByScheduledAtAsc(status);
    }

    // A3
    @Transactional(readOnly = true)
    public List<Appointment> getBetween(
            LocalDate start,
            LocalDate end
    ) {

        LocalDateTime startDateTime = start.atStartOfDay();

        LocalDateTime endDateTime =
                end.plusDays(1).atStartOfDay().minusNanos(1);

        return appointments.findByScheduledAtBetweenOrderByScheduledAtAsc(
                startDateTime,
                endDateTime
        );
    }

    // C1
    @Transactional(readOnly = true)
    public List<Object[]> getStatsByStatus() {

        return appointments.countAppointmentsByStatus();
    }

    // C4
    public String cancelDay(
            UUID doctorId,
            LocalDate date
    ) {

        if (!doctors.existsById(doctorId)) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "The doctor with that id does not exist"
            );
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        int count = appointments.cancelAppointmentsForDoctorOnDate(
                doctorId,
                start,
                end
        );

        return count + " appointments cancelled";
    }

    // Bonus
    public String deleteCancelledBefore(LocalDate date) {

        LocalDateTime before = date.atStartOfDay();

        int count = appointments.deleteCancelledBefore(before);

        return count + " appointments deleted";
    }
}
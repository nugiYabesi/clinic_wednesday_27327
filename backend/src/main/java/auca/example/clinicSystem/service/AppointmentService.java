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

    public Appointment save(Appointment appointment) {

        LocalDate apptDate = appointment.getScheduledAt().toLocalDate();
        if (apptDate.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Appointment date can not be in the past"
            );
        }

        appointment.setStatus(AppointmentStatus.SCHEDULED);

        Doctor doctor = appointment.getDoctor();
        if (doctor == null) || doctor.getId() == null) {
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor is required");
        }

        LocalDateTime start = apptDate.atStartOfDay();
        LocalDateTime end = apptDate.plusDays(1).atStartOfDay();

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


    public String updateStatus(UUID id, AppointmentStatus newStatus) {
        Appointment appt = appointments.findBy(id)
                .orElseThrow(() -> new  ResponseStatusException(HttpStatus.NOT_FOUND, " The appointment with that id does not exist"));

        AppointmentStatus currentStatus = appt.getStatus();

        boolean valid = false;
        if (currentStatus == AppointmentStatus.SCHEDULED) {
            if (newStatus == AppointmentStatus.CONFIRMED || newStatus == AppointmentStatus.CANCELLED) {
                valid = true;
            }

        } else if (currentStatus == AppointmentStatus.CONFIRMED) {
            if (newStatus == AppointmentStatus.COMPLETED || newStatus == AppointmentStatus.CANCELLED) {
                valid = true;
            }

        } else if (currentStatus == AppointmentStatus.COMPLETED) {
            if (newStatus == AppointmentStatus.CANCELLED) {
                valid = true;
            }

        }
        if (!valid) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot change status from " + currentStatus + " to " + newStatus
            );
        }

        appt.setStatus(newStatus);
        appointments.save(appt);
        return "Appointment status updated successfully";
    }


    public String reschedule(UUID id, LocalDate newDate) {
        Appointment appt = appointments.findById(id)
                .orElseThrow(() -> new  ResponseStatusException(HttpStatus.NOT_FOUND, " The appointment with that id does not exist"));

        if (appt.getStatus() == AppointmentStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A COMPLETED appointment cannot be rescheduled");
        }
        if (appt.getStatus() == AppointmentStatus.CANCELLED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A CANCELLED appointment cannot be rescheduled");
        }
        if (newDate.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment date cannot be in the past");
        }

        Doctor doctor = appt.getDoctor();
        LocalDateTime start = newDate.atStartOfDay();
        LocalDateTime end = newDate.plusDays(1).atStartOfDay();

        boolean alreadyBooked =
                appointments.existsByDoctorAndScheduledAtBetweenAndStatusNot(
                        doctor,
                        start,
                        end,
                        AppointmentStatus.CANCELLED
                );

        if (alreadyBooked) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Doctor is already booked on that date");
        }

        appt.setScheduledAt(newDate.atStartOfDay());
        appt.setStatus(AppointmentStatus.SCHEDULED);
        appointments.save(appt);

        return "Appointment rescheduled successfully";
    }


    @Transactional(readOnly = true)
    public List<Appointment> getByStatus(
            AppointmentStatus status
    ) {

        return appointments.findByStatusOrderByScheduledAtAsc(status);
    }



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


    @Transactional(readOnly = true)
    public List<Object[]> getStatsByStatus() {

        return appointments.countAppointmentsByStatus();
    }


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
    public String deleteCancelledBefore(LocalDate date) {

        LocalDateTime before = date.atStartOfDay();

        int count = appointments.deleteCancelledBefore(before);

        return count + " appointments deleted";
    }
}
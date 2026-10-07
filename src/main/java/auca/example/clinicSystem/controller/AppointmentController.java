package auca.example.clinicSystem.controller;

import auca.example.clinicSystem.model.Appointment;
import auca.example.clinicSystem.model.enums.AppointmentStatus;
import auca.example.clinicSystem.service.AppointmentService;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private final AppointmentService appointments;

    public AppointmentController(AppointmentService appointments) {
        this.appointments = appointments;
    }

    // A4
    @PostMapping("/save")
    public Appointment save(
            @RequestBody Appointment appointment
    ) {
        return appointments.save(appointment);
    }

    // A2
    @GetMapping("/by-status")
    public List<Appointment> getByStatus(
            @RequestParam AppointmentStatus status
    ) {
        return appointments.getByStatus(status);
    }

    // A3
    @GetMapping("/between")
    public List<Appointment> getBetween(
            @RequestParam String start,
            @RequestParam String end
    ) {

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        return appointments.getBetween(startDate, endDate);
    }

    // C1
    @GetMapping("/stats/by-status")
    public List<Object[]> getStatsByStatus() {

        return appointments.getStatsByStatus();
    }

    // C4
    @PatchMapping("/cancel-day")
    public String cancelDay(
            @RequestParam UUID doctorId,
            @RequestParam String date
    ) {

        LocalDate localDate = LocalDate.parse(date);

        return appointments.cancelDay(
                doctorId,
                localDate
        );
    }

    // Bonus
    @DeleteMapping("/cancelled-before")
    public String deleteCancelledBefore(
            @RequestParam String date
    ) {

        LocalDate localDate = LocalDate.parse(date);

        return appointments.deleteCancelledBefore(localDate);
    }
}
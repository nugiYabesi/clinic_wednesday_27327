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


    @PostMapping("/save")
    public Appointment save(
            @RequestBody Appointment appointment
    ) {
        return appointments.save(appointment);
    }


    @PatchMapping("/{id}/status")
    public String updateStatus(
            @PathVariable UUID id,
            @RequestParam AppointmentStatus Status
    ) {
        return appointments.updateStatus(id, Status);
    }

    @PatchMapping("/{id}/reschedule")
    public String rescheduleAppointment(
            @PathVariable UUID id,
            @RequestParam String newDate
    ){
        LocalDate parseDate = LocalDate.parse(newDate);
        return appointments.reschedule(id, parsedDate);
    }


    @GetMapping("/by-status")
    public List<Appointment> getByStatus(
            @RequestParam AppointmentStatus status
    ) {
        return appointments.getByStatus(status);
    }


    @GetMapping("/between")
    public List<Appointment> getBetween(
            @RequestParam String start,
            @RequestParam String end
    ) {

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        return appointments.getBetween(startDate, endDate);
    }


    @GetMapping("/stats/by-status")
    public List<Object[]> getStatsByStatus() {

        return appointments.getStatsByStatus();
    }


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
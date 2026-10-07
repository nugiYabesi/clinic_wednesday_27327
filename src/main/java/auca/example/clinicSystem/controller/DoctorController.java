package auca.example.clinicSystem.controller;

import auca.example.clinicSystem.model.Doctor;
import auca.example.clinicSystem.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    private final DoctorService doctors;
    public DoctorController(DoctorService doctors) { this.doctors = doctors; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor create(@RequestBody Doctor doctor) { return doctors.create(doctor); }

    @GetMapping
    public List<Doctor> findAll() { return doctors.findAll(); }

    @GetMapping("/{id}")
    public Doctor findById(@PathVariable UUID id) { return doctors.findById(id); }

    @PutMapping("/{id}")
    public Doctor update(@PathVariable UUID id, @RequestBody Doctor details) {
        return doctors.update(id, details);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) { doctors.delete(id); }

    @GetMapping("/by-specialization")
    public List<Doctor> getBySpecialization(
            @RequestParam String name
    ) {
        return doctors.getBySpecialization(name);
    }

    @GetMapping("/without-office")
    public List<Doctor> getDoctorsWithoutOffice() {
        return doctors.getDoctorsWithoutOffice();
    }
}

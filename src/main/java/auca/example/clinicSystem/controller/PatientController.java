package auca.example.clinicSystem.controller;

import auca.example.clinicSystem.model.Patient;
import auca.example.clinicSystem.service.PatientService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patients;

    public PatientController(PatientService patients) {
        this.patients = patients;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Patient create(@RequestBody Patient patient) {
        return patients.create(patient);
    }

    @GetMapping
    public List<Patient> findAll() {
        return patients.findAll();
    }

    @GetMapping("/{id}")
    public Patient findById(@PathVariable UUID id) {
        return patients.findById(id);
    }

    @PutMapping("/{id}")
    public Patient update(
            @PathVariable UUID id,
            @RequestBody Patient details
    ) {
        return patients.update(id, details);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        patients.delete(id);
    }

    // A1
    @GetMapping("/by-last-name")
    public List<Patient> getPatientByLastName(
            @RequestParam String lastName
    ) {
        return patients.getByLastName(lastName);
    }

    // B4
    @GetMapping("/of-doctor/{doctorId}")
    public List<Patient> getPatientsOfDoctor(
            @PathVariable UUID doctorId
    ) {
        return patients.getPatientsOfDoctor(doctorId);
    }
    @GetMapping("/frequent")
    public List<Patient> getFrequentPatients(
            @RequestParam long min
    ) {
        return patients.getFrequentPatients(min);
    }
}
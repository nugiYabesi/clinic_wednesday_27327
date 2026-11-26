package auca.example.clinicSystem.controller;

import auca.example.clinicSystem.model.Doctor;
import auca.example.clinicSystem.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/{doctorId}/specializations/{specId}")
    public ResponseEntity<String>  assignSpecializationToDoctor(
            @PathVariable UUID doctorId,
            @PathVariable UUID specId) {

        String outcome = doctors.addSpecialization(doctorId, specId);

        if (outcome.equals("The doctor with that id does not exist") ||
                outcome.equals("The specialization  does not exist")) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(outcome);
        }
        if (outcome.equals("The doctor already has that specialization")) {
            return  ResponseEntity.status(HttpStatus.CONFLICT).body(outcome);
        }
        return ResponseEntity.status(HttpStatus.OK).body(outcome);
    }

    @DeleteMapping("/{doctorId}/specializations/{specId}")
    public ResponseEntity<String> removeSpecializationFromDoctor(
            @PathVariable UUID doctorId,
            @PathVariable UUID specId) {

        String outcome = doctors.removeSpecialization(doctorId, specId);

        if (outcome.equals("The doctor with that id does not exist") ||
                outcome.equals("That specialization  does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(outcome);
        }
        if (outcome.equals("The doctor does not have that specialization")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(outcome);
        }
        return ResponseEntity.status(HttpStatus.OK).body(outcome);
    }


                @PatchMapping("/{doctorId}/office")
                public ResponseEntity<String> relocateDoctorOffice(
                        @PathVariable UUID doctorId,
                        @RequestParam int officeNumber) {

                    String outcome = doctors.moveDoctorOffice(doctorId, officeNumber);

                    if (outcome.equals("The doctor with that id does not exist") ||
                            outcome.equals("The office with office number does not exist")) {
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(outcome);
                    }

                    if (outcome.equals("That office is taken by other doctor")) {
                        return ResponseEntity.status(HttpStatus.CONFLICT).body(outcome);
                    }
                    return ResponseEntity.status(HttpStatus.OK).body(outcome);
        }

            }



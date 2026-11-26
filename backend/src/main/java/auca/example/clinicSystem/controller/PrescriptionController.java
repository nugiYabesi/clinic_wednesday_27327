package auca.example.clinicSystem.controller;

import auca.example.clinicSystem.model.Prescription;
import auca.example.clinicSystem.service.PrescriptionService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController 
@RequestMapping("/api/prescriptions")
public class PrescriptionController {
    
    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @PostMapping("/save")
    @ResponseStatus (HttpStatus.CREATED)
    public Prescription savePrescription(
        @RequestParam UUID appointmentId, 
        @RequestBody Prescription prescription
    ) {
        return prescriptionService.savePrescription(appointmentId, prescription);
    }

    @GetMapping("/appointment/{appointmentId}")
    public List<Prescription> getPrescriptionsByAppointment(
        @PathVariable UUID appointmentId
    ) {
        return prescriptionService.getByAppointmentId(appointmentId);
    }

    @PutMapping ("/update/{id}")
    public Prescription updatePrescription(
        @PathVariable UUID id, 
        @RequestBody Prescription details
    ) {
        return prescriptionService.updatePrescription(id, details);
    }
    
    @DeleteMapping ("/delete/{id}")
    @ResponseStatus (HttpStatus.OK)
    public String deletePrescription(
        @PathVariable UUID id
    ) {
        prescriptionService.deletePrescription(id);
        return "Prescription has been deleted successfully.";
    }
}

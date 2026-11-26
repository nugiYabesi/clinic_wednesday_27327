package auca.example.clinicSystem.service;


import auca.example.clinicSystem.model.Appointment;
import auca.example.clinicSystem.model.Prescription;
import auca.example.clinicSystem.model.enums.AppointmentStatus;
import auca.example.clinicSystem.repository.AppointmentRepository;
import auca.example.clinicSystem.repository.PrescriptionRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional 
public class PrescriptionService {

    private final PrescriptionRepository prescriptions;
    private final AppointmentRepository appointments;

    public PrescriptionService(PrescriptionRepository prescriptions, AppointmentRepository appointments) {
        this.prescriptions = prescriptions;
        this.appointments = appointments;
    }

    public Prescription savePrescription(UUID appointmentId, Prescription prescription) {
        Appointment appt = appointments.findById(appointmentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The appointment with that ID does not exist"));

        if (appt.getStatus() != AppointmentStatus.COMPLETED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Prescriptions are only for Completed appointments");
        }

        prescription.setAppointment(appt);
        prescription.setPrescriptionDate(LocalDateTime.now());
        return prescriptions.save(prescription);
    }

    @Transactional(readOnly = true)
    public List<Prescription> getByAppointmentId(UUID appointmentId) {
        if (!appointments.existsById(appointmentId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The appointment with that ID does not exist");
        }
        return prescriptions.findByAppointmentId(appointmentId);
    }

    @Transactional (readOnly = true)
    public List<Prescription> getByPatientSsNumber(String patientSsNumber) {
        return prescriptions.findByPatientSsNumber(ssNumber);
    }

    public Prescription updatePrescription(UUID id, Prescription details) {
        Prescription prescription = prescriptions.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The prescription with that ID does not exist"));

        prescription.setDrugName(details.getDrugName());
        prescription.setDose(details.getDose());
        prescription.setNumberOfDays(details.getNumberOfDays());
       

        return prescriptions.save(prescription);
    }

    public void deletePrescription(UUID id) {
        Prescription prescription = prescriptions.findById(id)
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "THE PRESCIPTION WITH THAT ID DOES NOT EXIST"));
        prescriptions.delete(prescription);
    }


}

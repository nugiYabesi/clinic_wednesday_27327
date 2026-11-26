package auca.example.clinicSystem.service;

import auca.example.clinicSystem.model.Patient;
import auca.example.clinicSystem.repository.PatientRepository;
import auca.example.clinicSystem.repository.DoctorRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PatientService {

    private final PatientRepository patients;
    private final DoctorRepository doctors;

    public PatientService(
            PatientRepository patients,
            DoctorRepository doctors
    ) {
        this.patients = patients;
        this.doctors = doctors;
    }

    public Patient create(Patient patient) {
        return patients.save(patient);
    }

    @Transactional(readOnly = true)
    public List<Patient> findAll() {
        return patients.findAll();
    }

    @Transactional(readOnly = true)
    public Patient findById(UUID id) {
        return patients.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Patient not found: " + id
                        ));
    }

    public Patient update(UUID id, Patient details) {

        Patient patient = findById(id);

        patient.setFirstName(details.getFirstName());
        patient.setLastName(details.getLastName());
        patient.setPhoneNumber(details.getPhoneNumber());

        return patients.save(patient);
    }

    public void delete(UUID id) {

        Patient patient = findById(id);

        if (!patient.getAppointments().isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Cannot delete a patient with existing appointments"
            );
        }

        patients.delete(patient);
    }

    // A1
    @Transactional(readOnly = true)
    public List<Patient> getByLastName(String lastName) {

        return patients.findByLastNameIgnoreCaseOrderByFirstNameAsc(
                lastName
        );
    }

    // B4
    @Transactional(readOnly = true)
    public List<Patient> getPatientsOfDoctor(UUID doctorId) {

        if (!doctors.existsById(doctorId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "The doctor with that id does not exist"
            );
        }

        return doctors.findPatientsOfDoctor(doctorId);
    }
    @Transactional(readOnly = true)
    public List<Patient> getFrequentPatients(long min) {

        return patients.findFrequentPatients(min);
    }
}
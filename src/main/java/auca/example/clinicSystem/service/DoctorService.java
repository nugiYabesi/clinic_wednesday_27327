package auca.example.clinicSystem.service;

import auca.example.clinicSystem.model.Doctor;
import auca.example.clinicSystem.repository.DoctorRepository;
import auca.example.clinicSystem.repository.OfficeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DoctorService {
    private final DoctorRepository doctors;
    private final OfficeRepository offices;

    public DoctorService(DoctorRepository doctors, OfficeRepository offices) {
        this.doctors = doctors;
        this.offices = offices;
    }

    public Doctor create(Doctor doctor) {
        String name = normalizeName(doctor.getName());
        if (doctors.existsByNameIgnoreCase(name)) {
            throw duplicateName();
        }
        doctor.setName(name);
        return doctors.save(doctor);
    }
    @Transactional(readOnly = true)
    public List<Doctor> findAll() { return doctors.findAll(); }
    @Transactional(readOnly = true)
    public Doctor findById(UUID id) {
        return doctors.findById(id).orElseThrow(() -> notFound(id));
    }
    public Doctor update(UUID id, Doctor details) {
        Doctor doctor = findById(id);
        String name = normalizeName(details.getName());
        if (doctors.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw duplicateName();
        }
        doctor.setName(name);
        doctor.setPhoneNumber(details.getPhoneNumber());
        return doctors.save(doctor);
    }
    public void delete(UUID id) {
        Doctor doctor = findById(id);
        if (offices.findByDoctor_Id(id).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Delete or unassign the doctor's office before deleting the doctor");
        }
        if (!doctor.getAppointments().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Cannot delete a doctor with existing appointments");
        }
        doctors.delete(doctor);
    }

    private ResponseStatusException notFound(UUID id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found: " + id);
    }

    private String normalizeName(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Doctor name is required");
        }
        return name.trim();
    }

    @Transactional(readOnly = true)
    public List<Doctor> getBySpecialization(String name) {

        return doctors.findBySpecialtyName(name);
    }

    // B2
    @Transactional(readOnly = true)
    public List<Doctor> getDoctorsWithoutOffice() {

        return doctors.findDoctorsWithoutOffice();
    }

    private ResponseStatusException duplicateName() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Doctor already exists");
    }
}

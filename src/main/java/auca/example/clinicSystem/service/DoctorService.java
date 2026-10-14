package auca.example.clinicSystem.service;

import auca.example.clinicSystem.model.Doctor;
import auca.example.clinicSystem.model.Office;
import auca.example.clinicSystem.model.Specialty;
import auca.example.clinicSystem.repository.DoctorRepository;
import auca.example.clinicSystem.repository.OfficeRepository;
import auca.example.clinicSystem.repository.SpecialityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

@Service
@Transactional
public class DoctorService {
    private final DoctorRepository doctors;
    private final OfficeRepository offices;
    private final SpecialityRepository specialties;

    public DoctorService(DoctorRepository doctors, OfficeRepository offices, SpecialityRepository specialties) {
        this.doctors = doctors;
        this.offices = offices;
        this.specialties = specialties;
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
    public List<Doctor> findAll() {
        return doctors.findAll();
    }
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

    public String addSpecialization(UUID doctorId, UUID specId) {
        Optional<Doctor> doctorOpt = doctors.findById(doctorId);
        if (doctorOpt.isEmpty()) {
            return "The doctor with that id does not exist";
        }

        Optional<Specialty> targetSpec = specialties.findById(specId);
        if (targetSpec.isEmpty()) {
            return "That Specialization does not exist";
        }
        Doctor doctor = doctorOpt.get();
        Specialty specialty = targetSpec.get();

        if (doctor.getSpecializations().contains(specialty)) {
            return "The doctor already has that specialization";
        }
       doctor.getSpecializations().add(specialty);
        doctors.save(doctor);
        return "Specialization added to the Doctor";
    }

    public String removeSpecialization(UUID doctorId, UUID specId) {
        Optional<Doctor> foundDoc = doctors.findById(doctorId);
        if (foundDoc.isEmpty()) {
            return "The doctor with that id does not exist";
        }

        Optional<Specialty> targetSpec = specialties.findById(specId);
        if (targetSpec.isEmpty()) {
            return "That Specialization does not exist";
        }

        Doctor doctor = foundDoc.get();
        Specialty specialty = targetSpec.get();

        if (!doctor.getSpecializations().contains(specialty)) {
            return "The doctor does not have that specialization";
        }

        doctor.getSpecializations().remove(specialty);
        doctors.save(doctor);
        return "Specialization removed from the Doctor";
    }

    public String moveDoctorOffice(UUID doctorId, int officeNumber) {
        Optional<Doctor> foundDoc = doctors.findById(doctorId);
        if (foundDoc.isEmpty()) {
            return "The doctor with that id does not exist";
        }

        Optional<Office> matchedOffice = offices.findByOfficeNumber(officeNumber);
        if (matchedOffice.isEmpty()) {
            return "The Office with that office number does not exist";
        }

        Office newOffice = matchedOffice.get();

        List<Doctor> allDoctors = doctors.findAll();
        for (Doctor item : allDoctors) {
            if (item.getOffice() != null && item.getOffice().getOfficeNumber() == officeNumber && !item.getId().equals(doctorId)) {
                return "That office is taken by other doctor";
            }
        }
        Doctor doctor = foundDoc.get();
        doctor.setOffice(newOffice);
        doctors.save(doctor);
        return "Office changed successfully";
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


    @Transactional(readOnly = true)
    public List<Doctor> getDoctorsWithoutOffice() {

        return doctors.findDoctorsWithoutOffice();
    }

    private ResponseStatusException duplicateName() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Doctor already exists");
    }
}

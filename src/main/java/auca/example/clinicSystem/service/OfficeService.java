package auca.example.clinicSystem.service;

import auca.example.clinicSystem.dto.OfficeRequest;
import auca.example.clinicSystem.model.Doctor;
import auca.example.clinicSystem.model.Office;
import auca.example.clinicSystem.repository.DoctorRepository;
import auca.example.clinicSystem.repository.OfficeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OfficeService {
    private final OfficeRepository offices;
    private final DoctorRepository doctors;

    public OfficeService(OfficeRepository offices, DoctorRepository doctors) {
        this.offices = offices;
        this.doctors = doctors;
    }

    public Office create(OfficeRequest request) {
        ensureRoomIsAvailable(request.roomNumber(), request.location(), null);
        Office office = new Office(request.roomNumber(), request.location());
        office.setDoctor(findDoctorAndCheckAvailability(request.doctorId(), null));
        return offices.save(office);
    }

    @Transactional(readOnly = true)
    public List<Office> findAll() { return offices.findAll(); }

    @Transactional(readOnly = true)
    public Office findById(UUID id) { return offices.findById(id).orElseThrow(() -> notFound(id)); }

    public Office update(UUID id, OfficeRequest request) {
        Office office = offices.findById(id).orElseThrow(() -> notFound(id));
        ensureRoomIsAvailable(request.roomNumber(), request.location(), id);

        Doctor requestedDoctor = findDoctorAndCheckAvailability(request.doctorId(), id);
        if (office.getDoctor() != null && requestedDoctor != null
                && !office.getDoctor().getId().equals(requestedDoctor.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This office is already assigned to another doctor");
        }

        office.setRoomNumber(request.roomNumber());
        office.setLocation(request.location());
        office.setDoctor(requestedDoctor);
        return offices.save(office);
    }

    public void delete(UUID id) {
        Office office = offices.findById(id).orElseThrow(() -> notFound(id));
        office.setDoctor(null);
        offices.delete(office);
    }

    private void ensureRoomIsAvailable(String roomNumber, String location, UUID currentOfficeId) {
        offices.findByRoomNumberAndLocation(roomNumber, location).ifPresent(existing -> {
            if (!existing.getId().equals(currentOfficeId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "An office with this room number and location already exists");
            }
        });
    }

    private Doctor findDoctorAndCheckAvailability(UUID doctorId, UUID currentOfficeId) {
        if (doctorId == null) return null;
        Doctor doctor = doctors.findById(doctorId).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Doctor not found: " + doctorId));
        offices.findByDoctor_Id(doctorId).ifPresent(existing -> {
            if (!existing.getId().equals(currentOfficeId)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "This doctor is already assigned to another office");
            }
        });
        return doctor;
    }

    private ResponseStatusException notFound(UUID id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Office not found: " + id);
    }
    @Transactional(readOnly = true)
    public String getBusiestOffice() {

        List<Object[]> result =
                offices.findBusiestOffice(
                        (Pageable) PageRequest.of(0, 1)
                );

        if (result.isEmpty()) {
            return "No appointments yet";
        }

        Object[] row = result.get(0);

        String name = (String) row[0];
        String roomNumber = (String) row[1];
        Long count = (Long) row[2];

        return name + ", " + roomNumber + ", " + count
                + " appointments";
    }
}

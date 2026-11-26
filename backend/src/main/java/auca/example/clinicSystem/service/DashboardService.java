package auca.example.clinicSystem.service;

import auca.example.clinicSystem.model.enums.AppointmentStatus;
import auca.example.clinicSystem.repository.AppointmentRepository;
import auca.example.clinicSystem.repository.DoctorRepository;
import auca.example.clinicSystem.repository.OfficeRepository;
import auca.example.clinicSystem.repository.PatientRepository;
import auca.example.clinicSystem.repository.PrescriptionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service 
@Transactional

public class DashboardService {

    private final DoctorRepository doctorRepo;
    private final PatientRepository patientRepo;
    private final AppointmentRepository appointmentRepo;
    private final PrescriptionRepository prescriptionRepo;
    private final OfficeRepository officeRepo;

    public DashboardService(
            DoctorRepository doctorRepo,
            PatientRepository patientRepo,
            AppointmentRepository appointmentRepo,
            PrescriptionRepository prescriptionRepo,
            OfficeRepository officeRepo
    ) {
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.appointmentRepo = appointmentRepo;
        this.prescriptionRepo = prescriptionRepo;
        this.officeRepo = officeRepo;
    }

    public Map<String, Long> getDashboardSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("doctors", doctorRepo.count());
        summary.put("patients", patientRepo.count());
        summary.put("freeoffices", (long) officeRepo.retrieveUnoccupiedOffices().size());

        
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().plusDays(1).atStartOfDay();
        summary.put("AppointmentsToday", (long) appointmentRepo.findByScheduledAtBetweenOrderByScheduledAtAsc(startOfDay, endOfDay.minusNanos(1)).size());

        summary.put("scheduled", appointmentRepo.countByStatus(AppointmentStatus.SCHEDULED));
        summary.put("prescriptions", prescriptionRepo.count());

        return summary;
    }
    
    
}

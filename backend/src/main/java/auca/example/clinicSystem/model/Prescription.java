package auca.example.clinicSystem.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "prescription")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String drugName;
    private String dose;
    private int numberOfDays;
    private LocalDate prescriptionDate;

    @ManyToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    public UUID getId() { return id; }
    public void setId(UUID  id) { this.id = id; }
    public String getDrugName() { return drugName; }
    public void setDrugName(String drugName) { this.drugName = drugName; }
    public String getDose() { return dose; }
    public void setDose(String dosage) { this.dose = dose; }
    public int getNumberOfDays() { return numberOfDays; }
    public void setNumberOfDays(int numberOfDays) {this.numberOfDays = numberOfDays; }
    public LocalDate getPrescriptionDate() { return prescriptionDate; }

    public void setPrescriptionDate(LocalDate prescriptionDateDate) {
        this.prescriptionDate = prescriptionDateDate;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }
}

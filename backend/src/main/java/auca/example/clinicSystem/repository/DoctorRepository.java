package auca.example.clinicSystem.repository;

import auca.example.clinicSystem.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface DoctorRepository extends JpaRepository<Doctor, UUID> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCaseAndIdNot(String name, UUID id);

    @Query("""
            SELECT DISTINCT d
            FROM Doctor d
            JOIN d.specialties s
            WHERE LOWER(s.name) = LOWER(:name)
            """)
    List<Doctor> findBySpecialtyName(
            @Param("name") String name
    );

    
    @Query("""
            SELECT d
            FROM Doctor d
            WHERE d.office IS NULL
            ORDER BY d.lastName ASC
            """)
    List<Doctor> findDoctorsWithoutOffice();


    @Query("""
            SELECT DISTINCT a.patient
            FROM Appointment a
            WHERE a.doctor.id = :doctorId
            """)
    List<auca.example.clinicSystem.model.Patient> findPatientsOfDoctor(
            @Param("doctorId") UUID doctorId
    );


    @Query("SELECT d FROM Doctor d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY d.name ASC")
        List<Doctor> searchByNameIgnorecase(@Param("name") String name);
}

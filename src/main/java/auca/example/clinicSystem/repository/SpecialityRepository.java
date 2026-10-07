package auca.example.clinicSystem.repository;

import auca.example.clinicSystem.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SpecialityRepository extends JpaRepository<Specialty, UUID> {
    @Query("""
            SELECT s
            FROM Specialty s
            WHERE s.doctors IS EMPTY
            """)
    List<Specialty> findUnusedSpecialties();
}

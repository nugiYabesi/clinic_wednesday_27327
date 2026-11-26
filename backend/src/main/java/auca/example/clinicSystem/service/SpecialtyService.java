package auca.example.clinicSystem.service;

import auca.example.clinicSystem.model.Specialty;


import auca.example.clinicSystem.repository.SpecialityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SpecialtyService {

    private final SpecialityRepository specialties;

    public SpecialtyService(SpecialityRepository specialties) {
        this.specialties = specialties;
    }

    // B3
    @Transactional(readOnly = true)
    public List<Specialty> getUnusedSpecialties() {

        return specialties.findUnusedSpecialties();
    }
}
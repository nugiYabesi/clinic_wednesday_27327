package auca.example.clinicSystem.controller;

import auca.example.clinicSystem.model.Specialty;
import auca.example.clinicSystem.service.SpecialtyService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specializations")
public class SpecialtyController {

    private final SpecialtyService specialties;

    public SpecialtyController(SpecialtyService specialties) {
        this.specialties = specialties;
    }

    
    @GetMapping("/unused")
    public List<Specialty> getUnusedSpecialties() {

        return specialties.getUnusedSpecialties();
    }
}
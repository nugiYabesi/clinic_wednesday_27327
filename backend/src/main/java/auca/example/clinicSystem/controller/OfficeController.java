package auca.example.clinicSystem.controller;

import auca.example.clinicSystem.dto.OfficeRequest;
import auca.example.clinicSystem.model.Office;
import auca.example.clinicSystem.service.OfficeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/offices")
public class OfficeController {
    private final OfficeService offices;
    public OfficeController(OfficeService offices) { this.offices = offices; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Office create(@RequestBody OfficeRequest request) { return offices.create(request); }

    @GetMapping
    public List<Office> findAll() { return offices.findAll(); }

    @GetMapping("/{id}")
    public Office findById(@PathVariable UUID id) { return offices.findById(id); }

    @PutMapping("/{id}")
    public Office update(@PathVariable UUID id, @RequestBody OfficeRequest request) {
        return offices.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) { offices.delete(id); }

    @GetMapping("/busiest")
    public String getBusiestOffice() {

        return offices.getBusiestOffice();
    }
}

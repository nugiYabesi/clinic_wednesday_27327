package auca.example.clinicSystem.dto;

import java.util.UUID;

public record OfficeRequest(String roomNumber, String location, UUID doctorId) { }

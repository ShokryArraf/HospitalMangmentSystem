package com.example.HospitalMangmentSystem.controller;

import com.example.HospitalMangmentSystem.dto.PatientCreateDto;
import com.example.HospitalMangmentSystem.dto.PatientDto;
import com.example.HospitalMangmentSystem.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;
// dependency injection
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }
    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@Valid @RequestBody PatientCreateDto dto) {
        return ResponseEntity.ok(patientService.createPatient(dto));
    }

    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable Long id, @Valid @RequestBody PatientCreateDto dto) {
        return ResponseEntity.ok(patientService.updatePatient(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();

    }

    // GET /patients/age-range?min=&max=
    @GetMapping("/age-range")
    public ResponseEntity<List<PatientDto>> getPatientsByAgeRange(
            @RequestParam int min,
            @RequestParam int max
    ) {
        return ResponseEntity.ok(patientService.getPatientsByAgeRange(min, max));
    }

    // GET /patients/age-statistics
    @GetMapping("/age-statistics")
    public ResponseEntity<Map<String, Object>> getAgeStatistics() {
        return ResponseEntity.ok(patientService.getPatientAgeStatistics());
    }

    // GET /patients/grouped-by-age
    @GetMapping("/grouped-by-age")
    public ResponseEntity<Map<String, List<PatientDto>>> getPatientsGroupedByAgeGroup() {
        return ResponseEntity.ok(patientService.getPatientsGroupedByAgeGroup());
    }

    // GET /patients/most-appointments?limit=
    @GetMapping("/most-appointments")
    public ResponseEntity<List<PatientDto>> getPatientsWithMostAppointments(@RequestParam int limit) {
        return ResponseEntity.ok(patientService.getPatientsWithMostAppointments(limit));
    }

    // GET /patients/search?keyword=john
    @GetMapping("/search")
    public ResponseEntity<List<PatientDto>> searchPatients(@RequestParam String keyword) {
        return ResponseEntity.ok(patientService.searchPatientsByName(keyword));
    }
}
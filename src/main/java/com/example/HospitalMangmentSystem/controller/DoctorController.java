package com.example.HospitalMangmentSystem.controller;

import com.example.HospitalMangmentSystem.dto.DoctorCreateDto;
import com.example.HospitalMangmentSystem.dto.DoctorDto;
import com.example.HospitalMangmentSystem.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/doctors")
public class DoctorController {
    private final DoctorService doctorService;

    //dependency injection
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }
//    @PostMapping
//    public ResponseEntity<DoctorDto> createDoctor(@Valid @RequestBody DoctorCreateDto dto) {
//        return ResponseEntity.ok(doctorService.createDoctor(dto));
//    }
    @PostMapping
    public ResponseEntity<?> createDoctor(@Valid @RequestBody DoctorCreateDto dto, BindingResult result) {
        if (result.hasErrors()) {
             // Return readable validation error messages
            return ResponseEntity.badRequest().body(
                   result.getAllErrors().stream()
                            .map(DefaultMessageSourceResolvable::getDefaultMessage)
                            .toList()
           );
        }
        DoctorDto createdDoctor = doctorService.createDoctor(dto);
        return ResponseEntity.ok(createdDoctor);
    }


    @GetMapping
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable Long id, @Valid @RequestBody DoctorCreateDto dto) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-specialization-map")
    public ResponseEntity<Map<String, List<DoctorDto>>> getDoctorsBySpecializationMap() {
        return ResponseEntity.ok(doctorService.getDoctorsBySpecialization());
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<DoctorDto>> getTopRatedDoctors(@RequestParam int limit) {
        return ResponseEntity.ok(doctorService.getTopRatedDoctors(limit));
    }

    @GetMapping("/experience-range")
    public ResponseEntity<List<DoctorDto>> getDoctorsByExperienceRange(
            @RequestParam int min,
            @RequestParam int max
    ) {
        return ResponseEntity.ok(doctorService.getDoctorsByExperienceRange(min, max));
    }

    @GetMapping("/average-rating-by-spec")
    public ResponseEntity<Map<String, Double>> getAverageRatingBySpecialization() {
        return ResponseEntity.ok(doctorService.getAverageRatingBySpecialization());
    }

    @GetMapping("/most-appointments")
    public ResponseEntity<List<DoctorDto>> getDoctorsWithMostAppointments(@RequestParam int limit) {
        return ResponseEntity.ok(doctorService.getDoctorsWithMostAppointments(limit));
    }
}

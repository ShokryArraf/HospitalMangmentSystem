package com.example.HospitalMangmentSystem.controller;

import com.example.HospitalMangmentSystem.dto.AppointmentCreateDto;
import com.example.HospitalMangmentSystem.dto.AppointmentDto;
import com.example.HospitalMangmentSystem.service.AppointmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }
    @PostMapping
    public ResponseEntity<AppointmentDto> createAppointment(@Valid @RequestBody AppointmentCreateDto appointment) {
        return ResponseEntity.ok(appointmentService.createAppointment(appointment));
    }

    // Get all appointments
    @GetMapping
    public ResponseEntity<List<AppointmentDto>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    // Get appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    // Update appointment
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentDto> updateAppointment(@PathVariable Long id, @Valid @RequestBody AppointmentCreateDto appointment) {
        return ResponseEntity.ok(appointmentService.updateAppointment(id, appointment));
    }

    // Delete appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    // Cancel appointment
    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentDto> cancelAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(id,"CANCELLED"));
    }

    // Complete appointment
    @PutMapping("/{id}/complete")
    public ResponseEntity<AppointmentDto> completeAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.updateAppointmentStatus(id,"COMPLETED"));
    }


    // Get appointments by priority
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByPriority(@PathVariable String priority) {
        return ResponseEntity.ok(appointmentService.getAppointmentByPriority(priority));
    }

    // Get upcoming appointments within 'days'
    @GetMapping("/upcoming")
    public ResponseEntity<List<AppointmentDto>> getUpcomingAppointments(@RequestParam int days) {
        return ResponseEntity.ok(appointmentService.getUpcomingAppointments(days));
    }

    // Get statistics by status
    @GetMapping("/statistics-by-status")
    public ResponseEntity<Map<String, Integer>> getStatisticsByStatus() {
        return ResponseEntity.ok(appointmentService.getAppointmentStatisticsByStatus());
    }

    // Get appointments within a date range
    @GetMapping("/date-range")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDateRange(
            @RequestParam String start,
            @RequestParam String end) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDateRange(start, end));
    }

    // Get daily schedule for doctor
    @GetMapping("/daily-schedule/{doctorId}")
    public ResponseEntity<List<AppointmentDto>> getDailySchedule(
            @PathVariable Long doctorId,
            @RequestParam String date) {
        return ResponseEntity.ok(appointmentService.getDailySchedule(doctorId, date));
    }

    // Get available slots for a doctor on a date
    @GetMapping("/available-slots/{doctorId}")
    public ResponseEntity<List<String>> getAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam String date) {
        return ResponseEntity.ok(appointmentService.getAvailableTimeSlots(doctorId, date));
    }

    // Get most busy day
    @GetMapping("/most-busy-day")
    public ResponseEntity<String> getMostBusyDay() {
        return ResponseEntity.ok(appointmentService.getMostBusyDay());
    }

    // Get appointments filtered by duration range
    @GetMapping("/duration-range")
    public ResponseEntity<List<AppointmentDto>> getAppointmentsByDurationRange(
            @RequestParam int min,
            @RequestParam int max) {
        return ResponseEntity.ok(appointmentService.getAppointmentsByDurationRange(min, max));
    }

    // Get cancelled appointments report
    @GetMapping("/cancelled-report")
    public ResponseEntity<Map<String,Object>> getCancelledReport() {
        return ResponseEntity.ok(appointmentService.getCancelledAppointmentsReport());
    }
}

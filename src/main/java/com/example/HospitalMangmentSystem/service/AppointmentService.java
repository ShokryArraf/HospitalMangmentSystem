package com.example.HospitalMangmentSystem.service;

import com.example.HospitalMangmentSystem.dto.AppointmentCreateDto;
import com.example.HospitalMangmentSystem.dto.AppointmentDto;
import com.example.HospitalMangmentSystem.exception.ResourceNotFoundException;
import com.example.HospitalMangmentSystem.exception.TimeSlotNotAvailableException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {

    private final List<AppointmentDto> appointments = new ArrayList<>();
    private Long nextId = 1L;

    private final DoctorService doctorService;
    private final PatientService patientService;

    public AppointmentService(DoctorService doctorService, PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    //          CRUD
    // Create new appointment
    public AppointmentDto createAppointment(AppointmentCreateDto dto) {

        // Validate doctor and patient existence
        doctorService.getDoctorById(dto.getDoctorId());
        patientService.getPatientById(dto.getPatientId());

        // Check if a time slot is already taken for the doctor
        boolean slotTaken = appointments.stream()
                .anyMatch(a -> a.getDoctorId().equals(dto.getDoctorId())
                        && a.getAppointmentDate().equals(dto.getAppointmentDate())
                        && a.getAppointmentTime().equals(dto.getAppointmentTime()));

        if (slotTaken) {
            throw new TimeSlotNotAvailableException(
                    "Doctor already has an appointment at " + dto.getAppointmentDate() + " " + dto.getAppointmentTime());
        }

        // Create and add appointment
        AppointmentDto appointment = new AppointmentDto();
        appointment.setId(nextId++);
        toDto(dto, appointment);
        appointments.add(appointment);
        return appointment;
    }

    private void toDto(AppointmentCreateDto dto, AppointmentDto appointment) {
        appointment.setDoctorId(dto.getDoctorId());
        appointment.setPatientId(dto.getPatientId());
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setStatus(dto.getStatus());
        appointment.setDuration(dto.getDuration());
        appointment.setPriority(dto.getPriority());
        appointment.setNotes(dto.getNotes());
    }

    // Get all appointments
    public List<AppointmentDto> getAllAppointments() {
        return new ArrayList<>(appointments); // Return a copy to avoid external modifications
    }

    // Get appointment by ID
    public AppointmentDto getAppointmentById(Long id) {
        return appointments.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with id " + id + " not found"));
    }

    // Update appointment
    public AppointmentDto updateAppointment(Long id, AppointmentCreateDto dto) {
        AppointmentDto existing = getAppointmentById(id);

        // Validate doctor & patient exist
        doctorService.getDoctorById(dto.getDoctorId());
        patientService.getPatientById(dto.getPatientId());

        toDto(dto, existing);

        return existing;
    }

    // Delete appointment
    public void deleteAppointment(Long id) {
        boolean removed = appointments.removeIf(a -> a.getId().equals(id));
        if (!removed) {
            throw new ResourceNotFoundException("Appointment with id " + id + " not found");
        }
    }
}

package com.example.HospitalMangmentSystem.service;

import com.example.HospitalMangmentSystem.dto.DoctorCreateDto;
import com.example.HospitalMangmentSystem.dto.DoctorDto;
import com.example.HospitalMangmentSystem.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {

    private final List<DoctorDto> doctors = new ArrayList<>();
    private Long nextId = 1L;

    public DoctorDto createDoctor(DoctorCreateDto dto) {
        DoctorDto doctor =  new DoctorDto();
        String email = dto.getEmail();
        for (DoctorDto d : doctors) {
            if (email.equals(d.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
        }
        toDto(doctor,dto);
        doctor.setId(this.nextId++);
        this.doctors.add(doctor);
        return doctor;

    }
    public List<DoctorDto> getAllDoctors() {
        return this.doctors;
    }
    public DoctorDto getDoctorById(Long id) {
        for (DoctorDto d : this.doctors) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        throw new ResourceNotFoundException("Doctor with id " + id + " not found");
    }
    public DoctorDto updateDoctor(Long id, DoctorCreateDto dto) {
        for (DoctorDto d : this.doctors) {
            if (d.getId().equals(id)) {
                return toDto(d,dto);
            }
        }
        throw new ResourceNotFoundException("Doctor with id " + id + " not found");
    }
    public void deleteDoctor(Long id) {
        boolean removed = this.doctors.removeIf(d -> d.getId().equals(id));
        if (!removed) {
            throw new ResourceNotFoundException("Doctor with id " + id + " not found");
        }
    }
    private DoctorDto toDto (DoctorDto dto,DoctorCreateDto doctorCreateDto) {
        dto.setFirstName(doctorCreateDto.getFirstName());
        dto.setLastName(doctorCreateDto.getLastName());
        dto.setEmail(doctorCreateDto.getEmail());
        dto.setYearsOfExperience(doctorCreateDto.getYearsOfExperience());
        dto.setAppointmentIds(doctorCreateDto.getAppointmentIds());
        dto.setPhoneNumber(doctorCreateDto.getPhoneNumber());
        dto.setRating(doctorCreateDto.getRating());
        dto.setSpecialization(doctorCreateDto.getSpecialization());
        return dto;
    }
}

package com.example.HospitalMangmentSystem.service;

import com.example.HospitalMangmentSystem.dto.DoctorCreateDto;
import com.example.HospitalMangmentSystem.dto.DoctorDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorService {

    private List<DoctorDto> doctors = new ArrayList<>();
    private Long nextId = 1L;

    public DoctorDto createDoctor(DoctorCreateDto dto) {
        DoctorDto doctor =  new DoctorDto();
        String email = dto.getEmail();
        for (DoctorDto d : doctors) {
            if (email.equals(d.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
        }
        doctor.setFirstName(dto.getFirstName());
        doctor.setLastName(dto.getLastName());
        doctor.setEmail(email);
        doctor.setYearsOfExperience(dto.getYearsOfExperience());
        doctor.setAppointmentIds(dto.getAppointmentIds());
        doctor.setPhoneNumber(String.valueOf(dto.getPhoneNumber()));
        doctor.setRating(dto.getRating());
        doctor.setSpecialization(String.valueOf(dto.getSpecialization()));
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
        return null;
    }
    public DoctorDto updateDoctor(Long id, DoctorCreateDto dto) { ... }
    public void del
}

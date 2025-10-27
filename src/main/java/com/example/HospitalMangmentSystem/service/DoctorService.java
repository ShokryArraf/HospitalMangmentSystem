package com.example.HospitalMangmentSystem.service;

import com.example.HospitalMangmentSystem.dto.DoctorCreateDto;
import com.example.HospitalMangmentSystem.dto.DoctorDto;
import com.example.HospitalMangmentSystem.exception.DuplicateResourceException;
import com.example.HospitalMangmentSystem.exception.InvalidOperationException;
import com.example.HospitalMangmentSystem.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    private final List<DoctorDto> doctors = new ArrayList<>();
    private Long nextId = 1L;

    public DoctorDto createDoctor(DoctorCreateDto dto) {
        DoctorDto doctor =  new DoctorDto();
        String email = dto.getEmail();
        for (DoctorDto d : doctors) {
            if (email.equals(d.getEmail())) {
                throw new DuplicateResourceException("Email already exists");
            }
        }
        toDto(doctor,dto);
        doctor.setId(this.nextId++);
        this.doctors.add(doctor);
        return doctor;

    }
    public List<DoctorDto> getAllDoctors() {
        return new ArrayList<>(doctors); // Return a copy to avoid external modifications

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

    // getDoctorsBySpecializationMap() → Map<String, List<DoctorDto>>
    public Map<String,List<DoctorDto>> getDoctorsBySpecialization() {
        if (doctors.isEmpty()) {
            throw new ResourceNotFoundException("No doctors found");
        }
        return doctors.stream()
                .collect(Collectors.groupingBy(DoctorDto::getSpecialization));
    }
    //getTopRatedDoctors(int limit) → List<DoctorDto>
    public List<DoctorDto> getTopRatedDoctors(int limit) {
        if (doctors.isEmpty()) {
            throw new ResourceNotFoundException("No doctors found");
        }

        if (limit <= 0) {
            throw new InvalidOperationException("Limit must be greater than 0");
        }

        return doctors.stream()
                .sorted(Comparator.comparingDouble(DoctorDto::getRating).reversed()) // sort by rating descending
                .limit(limit) // take top N
                .collect(Collectors.toList());
    }

    //. getDoctorsByExperienceRange(int minYears, int maxYears) →
    //List<DoctorDto>
    public List<DoctorDto> getDoctorsByExperienceRange(int minYears, int maxYears) {
        if (doctors.isEmpty()) {
            throw new ResourceNotFoundException("No doctors found");
        }
        if (minYears > maxYears) {
            throw new InvalidOperationException("Min years must be greater than max years");
        }
        List<DoctorDto> doctorByExperienceRange = new ArrayList<>();
        for (DoctorDto d : doctors) {
            if (d.getYearsOfExperience() >= minYears && d.getYearsOfExperience() <= maxYears) {
                doctorByExperienceRange.add(d);
            }
        }
        doctorByExperienceRange.sort(Comparator.comparingInt(DoctorDto::getYearsOfExperience).reversed());
        return doctorByExperienceRange;
    }
    // getAverageRatingBySpecialization() → Map<String, Double>
    public Map<String,Double> getAverageRatingBySpecialization() {
        if (doctors.isEmpty()) {
            throw new ResourceNotFoundException("No doctors found");
        }
        return doctors.stream().collect(Collectors.groupingBy(DoctorDto::getSpecialization,Collectors.averagingDouble(DoctorDto::getRating)));
    }
    //getDoctorsWithMostAppointments(int limit) → List<DoctorDto>
    public List<DoctorDto> getDoctorsWithMostAppointments(int limit) {
        if (doctors.isEmpty()) {
            throw new ResourceNotFoundException("No doctors found");
        }
        if (limit <= 0) {
            throw new InvalidOperationException("Limit must be greater than 0");
        }
        List<DoctorDto> sortedDoctors = new ArrayList<>(doctors);
        sortedDoctors.sort((d1,d2) -> Integer.compare(d2.getAppointmentIds().size(), d1.getAppointmentIds().size()));
        // Take top limit
        List<DoctorDto> topDoctors = new ArrayList<>();
        for (int i = 0; i < limit && i < sortedDoctors.size(); i++) {
            topDoctors.add(sortedDoctors.get(i));
        }
        return topDoctors;
    }
}

package com.example.HospitalMangmentSystem.service;

import com.example.HospitalMangmentSystem.dto.PatientCreateDto;
import com.example.HospitalMangmentSystem.dto.PatientDto;
import com.example.HospitalMangmentSystem.exception.DuplicateResourceException;
import com.example.HospitalMangmentSystem.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    private final List<PatientDto> patients = new ArrayList<>();
    private Long nextId = 1L;

    private Integer calculateAge(String dateOfBirth) {
        LocalDate dob = LocalDate.parse(dateOfBirth); // convert String to LocalDate
        return Period.between(dob, LocalDate.now()).getYears();
    }

    public PatientDto createPatient(PatientCreateDto dto) {
        PatientDto patient = new PatientDto();
        String email = dto.getEmail();
        for(PatientDto p : patients){
            if(p.getEmail().equals(email)){
                throw new DuplicateResourceException("Email already exists");
            }
        }
        patient.setId(nextId++);
        patient.setFirstName(dto.getFirstName());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setAge(calculateAge(dto.getDateOfBirth())); // automatically calculate age
        patient.setEmail(email);
        patient.setLastName(dto.getLastName());
        patient.setPhoneNumber(String.valueOf(dto.getPhoneNumber()));
        patients.add(patient);
        return patient;
    }
    public List<PatientDto> getAllPatients() {
        return new ArrayList<>(patients); // Return a copy to avoid external modifications

    }
    public PatientDto getPatientById(Long id) {
        for (PatientDto p : patients) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        throw new ResourceNotFoundException("Patient not found");
    }
    public PatientDto updatePatient(Long id, PatientCreateDto dto) {
        for (PatientDto patient : patients) {
            if (patient.getId().equals(id)) {
                patient.setFirstName(dto.getFirstName());
                patient.setDateOfBirth(dto.getDateOfBirth());
                patient.setAge(calculateAge(dto.getDateOfBirth())); // automatically calculate age
                patient.setEmail(dto.getEmail());
                patient.setLastName(dto.getLastName());
                patient.setPhoneNumber(String.valueOf(dto.getPhoneNumber()));
                return patient;
            }
        }
        throw new ResourceNotFoundException("Patient not found");
    }
    public void deletePatient(Long id) {
        boolean removed = this.patients.removeIf(p -> p.getId().equals(id));
        if (!removed) {
            throw new ResourceNotFoundException("Patient not found");
        }
    }
}

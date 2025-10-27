package com.example.HospitalMangmentSystem.service;

import com.example.HospitalMangmentSystem.dto.PatientCreateDto;
import com.example.HospitalMangmentSystem.dto.PatientDto;
import com.example.HospitalMangmentSystem.exception.DuplicateResourceException;
import com.example.HospitalMangmentSystem.exception.InvalidOperationException;
import com.example.HospitalMangmentSystem.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

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
    //. getPatientsByAgeRange(int minAge, int maxAge) → List<PatientDto>
    public List<PatientDto> getPatientsByAgeRange(int minAge, int maxAge) {
        if (maxAge < minAge) {
            throw new InvalidOperationException("max age must be greater min age");
        }
        if (patients.isEmpty()) {
            throw new ResourceNotFoundException("Patients not found");
        }
        return patients.stream()
                .filter(p -> p.getAge() >= minAge && p.getAge() <= maxAge)
                .sorted(Comparator.comparingInt(PatientDto::getAge))
                .collect(Collectors.toList());
    }
    //getPatientAgeStatistics() → Map<String, Object>
    public Map<String,Object> getPatientAgeStatistics() {
        Map<String,Object> map = new HashMap<>();
        if (patients.isEmpty()) {
            throw new ResourceNotFoundException("Patients not found");
        }
        int minAge = calculateAge(patients.getFirst().getDateOfBirth());
        int maxAge = calculateAge(patients.getFirst().getDateOfBirth());
        int sumAge = 0;
        int totalPatients = patients.size();
        for (PatientDto p : patients) {
            int age = p.getAge();
            sumAge += age;
            if (age < minAge) {
                minAge = age;
            }
            if (age > maxAge) {
                maxAge = age;
            }
        }
        map.put("minAge", minAge);
        map.put("maxAge", maxAge);
        map.put("averageAge", sumAge / totalPatients);
        map.put("totalPatients", totalPatients);
        return map;
    }
    //getPatientsGroupedByAgeGroup() → Map<String, List<PatientDto>>
    public Map<String,List<PatientDto>> getPatientsGroupedByAgeGroup() {
        Map<String,List<PatientDto>> map = new HashMap<>();
        map.put("0-18",new ArrayList<>());
        map.put("19-35", new ArrayList<>());
        map.put("36-60", new ArrayList<>());
        map.put("+61", new ArrayList<>());
        for (PatientDto p : patients) {
            int age = p.getAge();
            if (age >= 0 && age <= 18) {
                map.get("0-18").add(p);
            }else if (age <= 35) {
                map.get("19-35").add(p);
            }else if (age <= 60) {
                map.get("36-60").add(p);
            }else{
                map.get("+61").add(p);
            }
        }
        return map;
    }
    // getPatientsWithMostAppointments(int limit) → List<PatientDto>
    public List<PatientDto> getPatientsWithMostAppointments(int limit){
        if (patients.isEmpty()) {
            throw new ResourceNotFoundException("No patients found");
        }
        if (limit <= 0) {
            throw new InvalidOperationException("Limit must be greater than 0");
        }
        List<PatientDto> sortedPatients = new ArrayList<>(patients);
        sortedPatients.sort((p1,p2) -> Integer.compare(p2.getAppointmentIds().size(), p1.getAppointmentIds().size()));
        return sortedPatients.stream().limit(limit).collect(Collectors.toList());
    }
    // searchPatientsByName(String keyword) → List<PatientDto>
    public List<PatientDto> searchPatientsByName(String keyword){
        if (patients.isEmpty()) {
            throw new ResourceNotFoundException("No patients found");
        }
        List<PatientDto> sortedPatients = new ArrayList<>(patients);
        String finalKeyword = keyword.toLowerCase();
        return sortedPatients.stream().filter(p -> p.getFirstName().toLowerCase().contains(finalKeyword) || p.getLastName().toLowerCase().contains(finalKeyword)).sorted(Comparator.comparing(PatientDto::getLastName)).collect(Collectors.toList());
    }
}

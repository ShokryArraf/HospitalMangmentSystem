package com.example.HospitalMangmentSystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class PatientDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String dateOfBirth;
    private Integer age;
    private Set<Long> appointmentIds = new HashSet<>();

    public PatientDto(Long id, String lastName, String firstName, String email, String phoneNumber, String dateOfBirth, Integer age, Set<Long> appointmentIds) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.appointmentIds = appointmentIds;
    }
    public PatientDto() {}
}
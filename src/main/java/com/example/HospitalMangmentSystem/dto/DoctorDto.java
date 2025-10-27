package com.example.HospitalMangmentSystem.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class DoctorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String specialization;
    private String phoneNumber;
    private Integer yearsOfExperience;
    private Double rating;
    private Set<Long> appointmentIds = new HashSet<>();

    public DoctorDto(Long id, String firstName, String lastName, String email, String specialization, String phoneNumber, Integer yearsOfExperience, Double rating, Set<Long> appointmentIds) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
        this.yearsOfExperience = yearsOfExperience;
        this.rating = rating;
        this.appointmentIds = appointmentIds;
    }

    // Getters, Setters, Constructors...
}
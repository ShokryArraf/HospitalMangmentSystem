package com.example.HospitalMangmentSystem.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
public class Doctor {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String specialization;
    private String phoneNumber;
    private String yearsOfExperience;
    private Double rating;
    private Set<Long> appointmentId;

    public Doctor(Long id, String firstName, String lastName, String email, String specialization, String phoneNumber, String yearsOfExperience, Double rating, Set<Long> appointmentId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.specialization = specialization;
        this.phoneNumber = phoneNumber;
        this.yearsOfExperience = yearsOfExperience;
        this.rating = rating;
        this.appointmentId = appointmentId;
    }

    public Doctor() {

    }
}

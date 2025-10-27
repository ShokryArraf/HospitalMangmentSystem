package com.example.HospitalMangmentSystem.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Patient {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String dateOfBirth;
    private Integer age;
    private Set<Long> appointmentId;


    public Patient() {

    }

    public Patient(Long id, String firstName, String lastName, String phoneNumber, String email, String dateOfBirth, Set<Long> appointmentId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.appointmentId = appointmentId;
        this.age = 0;
    }
}
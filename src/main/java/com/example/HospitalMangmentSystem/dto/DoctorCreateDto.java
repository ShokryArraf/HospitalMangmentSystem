package com.example.HospitalMangmentSystem.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class DoctorCreateDto {
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "Cardiology|Orthopedics|Pediatrics|Neurology|Dermatology|General",
            message = "Specialty must be one of Cardiology, Orthopedics, Pediatrics, Neurology, Dermatology, General")
    private String specialization;
    @Pattern(regexp = "05\\d{8}",
            message = "Must match the format: 05XXXXXXXX (10 digits total)")
    private String phoneNumber;
    @NotNull
    @Min(0)
    @Max(50)
    private Integer yearsOfExperience;
    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("5.0")
    private Double rating;
    private Set<Long> appointmentIds = new HashSet<>();
}

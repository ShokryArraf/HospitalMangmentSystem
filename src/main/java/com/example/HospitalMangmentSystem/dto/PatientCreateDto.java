package com.example.HospitalMangmentSystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientCreateDto {
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;
    @Email
    @NotBlank
    private String email;
    @Pattern(regexp = "05\\d{8}",
            message = "Must match the format: 05,XXXXXXXX (8 digits)")
    private String phoneNumber;
    @Pattern(
            regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])",
            message = "Date must be in the format YYYY-MM-DD"
    )
    private String dateOfBirth;
}

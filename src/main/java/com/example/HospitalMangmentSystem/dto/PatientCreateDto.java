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
    @Pattern(regexp = "05\\d{7}",
            message = "Must match the format: 05,XXXXXXX (7 digits)")
    private String phoneNumber;
    @Pattern(
            regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])",
            message = "Date must be in the format YYYY-MM-DD"
    )
    private String dateOfBirth;
}

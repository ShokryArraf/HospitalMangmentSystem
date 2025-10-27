package com.example.HospitalMangmentSystem.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentCreateDto {

    @NotNull(message = "doctorId is required")
    private Long doctorId;

    @NotNull(message = "patientId is required")
    private Long patientId;

    @NotBlank(message = "appointmentDate is required")
    @Pattern(
            regexp = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])",
            message = "appointmentDate must be in the format YYYY-MM-DD"
    )
    private String appointmentDate;

    @NotBlank(message = "appointmentTime is required")
    @Pattern(
            regexp = "([01]\\d|2[0-3]):([0-5]\\d)",
            message = "appointmentTime must be in the format HH:MM (24-hour)"
    )
    private String appointmentTime;

    @NotNull(message = "status is required")
    @Pattern(
            regexp = "SCHEDULED|COMPLETED|CANCELLED",
            message = "status must be one of: SCHEDULED, COMPLETED, CANCELLED"
    )
    private String status;

    @NotNull(message = "duration is required")
    @Min(value = 15, message = "duration must be one of: 15, 30, 45, 60")
    @Max(value = 60, message = "duration must be one of: 15, 30, 45, 60")
    private Integer duration;

    @NotNull(message = "priority is required")
    @Pattern(
            regexp = "LOW|MEDIUM|HIGH|URGENT",
            message = "priority must be one of: LOW, MEDIUM, HIGH, URGENT"
    )
    private String priority;

    @Size(max = 500, message = "notes can be max 500 characters")
    private String notes;

}

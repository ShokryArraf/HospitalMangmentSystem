package com.example.HospitalMangmentSystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentDto {
    private Long id;
    private Long doctorId;
    private Long patientId;
    private String appointmentDate;
    private String appointmentTime;
    private String status;
    private Integer duration;
    private String priority;
    private String notes;

    public AppointmentDto(Long id, Long patientId, Long doctorId, String appointmentDate, String appointmentTime, String status, Integer duration, String priority, String notes) {
        this.id = id;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.duration = duration;
        this.priority = priority;
        this.notes = notes;
    }
    public AppointmentDto() {}
}

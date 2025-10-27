package com.example.HospitalMangmentSystem.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Appointment {

    @Id
    private Long id;
    private Long doctorId;
    private Long patientId;
    private String appointmentDate;
    private String appointmentTime;
    private String status;
    private Integer duration;
    private String priority;
    private String notes;

    public Appointment(Long id, Long doctorId, Long patientId, String appointmentDate, String appointmentTime, String status, Integer duration, String priority, String notes) {
        this.id = id;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.duration = duration;
        this.priority = priority;
        this.notes = notes;
    }

    public Appointment() {

    }
}
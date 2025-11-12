package com.example.HospitalMangmentSystem.service;

import com.example.HospitalMangmentSystem.dto.AppointmentCreateDto;
import com.example.HospitalMangmentSystem.dto.AppointmentDto;
import com.example.HospitalMangmentSystem.exception.InvalidOperationException;
import com.example.HospitalMangmentSystem.exception.ResourceNotFoundException;
import com.example.HospitalMangmentSystem.exception.TimeSlotNotAvailableException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private final List<AppointmentDto> appointments = new ArrayList<>();
    private Long nextId = 1L;

    private final DoctorService doctorService;
    private final PatientService patientService;

    public AppointmentService(DoctorService doctorService, PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    //          CRUD
    // Create new appointment
    public AppointmentDto createAppointment(AppointmentCreateDto dto) {
        // Validate doctor and patient existence
        doctorService.getDoctorById(dto.getDoctorId());
        patientService.getPatientById(dto.getPatientId());

        // Check for conflicting appointments (overlapping times)
        List<AppointmentDto> conflicts = getConflictingAppointments(
                dto.getDoctorId(),
                dto.getAppointmentDate(),
                dto.getAppointmentTime(),
                dto.getDuration()
        );

        if (!conflicts.isEmpty()) {
            throw new TimeSlotNotAvailableException(
                    "Doctor already has a conflicting appointment at " + dto.getAppointmentDate() + " " + dto.getAppointmentTime()
            );
        }

        // Create and add appointment
        AppointmentDto appointment = new AppointmentDto();
        appointment.setId(nextId++);
        toDto(dto, appointment);
        appointments.add(appointment);
        // Link appointment to patient
        patientService.getPatientById(dto.getPatientId()).getAppointmentIds().add(appointment.getId());
        // Link appointment to doctor
        doctorService.getDoctorById(dto.getDoctorId()).getAppointmentIds().add(appointment.getId());
        return appointment;
    }

    private void toDto(AppointmentCreateDto dto, AppointmentDto appointment) {
        appointment.setDoctorId(dto.getDoctorId());
        appointment.setPatientId(dto.getPatientId());
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setStatus(dto.getStatus());
        appointment.setDuration(dto.getDuration());
        appointment.setPriority(dto.getPriority());
        appointment.setNotes(dto.getNotes());
    }

    // Get all appointments
    public List<AppointmentDto> getAllAppointments() {
        return new ArrayList<>(appointments); // Return a copy to avoid external modifications
    }

    // Get appointment by ID
    public AppointmentDto getAppointmentById(Long id) {
        return appointments.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Appointment with id " + id + " not found"));
    }

    // Update appointment
    public AppointmentDto updateAppointment(Long id, AppointmentCreateDto dto) {
        AppointmentDto existing = getAppointmentById(id);

        // Validate doctor & patient existence
        doctorService.getDoctorById(dto.getDoctorId());
        patientService.getPatientById(dto.getPatientId());

        // Check for conflicting appointments (excluding the current one)
        List<AppointmentDto> conflicts = getConflictingAppointments(
                dto.getDoctorId(),
                dto.getAppointmentDate(),
                dto.getAppointmentTime(),
                dto.getDuration()
        ).stream()
                .filter(a -> !a.getId().equals(id)) // exclude the appointment being updated
                .toList();

        if (!conflicts.isEmpty()) {
            throw new TimeSlotNotAvailableException(
                    "Doctor already has a conflicting appointment at " + dto.getAppointmentDate() + " " + dto.getAppointmentTime()
            );
        }

        // Apply updates
        toDto(dto, existing);

        return existing;
    }

    public AppointmentDto updateAppointmentStatus(Long id, String status) {
        AppointmentDto existing = getAppointmentById(id);
        existing.setStatus(status);
        return existing;
    }

    // Delete appointment
    public void deleteAppointment(Long id) {
        boolean removed = appointments.removeIf(a -> a.getId().equals(id));
        if (!removed) {
            throw new ResourceNotFoundException("Appointment with id " + id + " not found");
        }
    }
    //getAppointmentsByPriority(String priority) → List<AppointmentDto>
    public List<AppointmentDto> getAppointmentByPriority(String priority){
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }

        if (priority == null || priority.isBlank()) {
            throw new InvalidOperationException("Priority must be provided");
        }
        List<AppointmentDto> result = appointments.stream().filter(a ->a.getPriority()
                        .equals(priority))
                .sorted(Comparator.comparing(AppointmentDto::getAppointmentDate).
                        thenComparing(AppointmentDto::getAppointmentTime)).toList();
        if (result.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found for priority: " + priority);
        }
        return result;
    }

    //getUpcomingAppointments(int days) → List<AppointmentDto>
    public List<AppointmentDto> getUpcomingAppointments(int days) {
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        if (days < 1) {
            throw new InvalidOperationException("Days must be greater than 0");
        }

        LocalDate today = LocalDate.now();
        LocalDate futureDate = today.plusDays(days);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); // if time stored as "14:30"

        return appointments.stream()
                .filter(a -> a.getStatus().equalsIgnoreCase("SCHEDULED"))
                .filter(a -> {
                    LocalDate appointmentDate = LocalDate.parse(a.getAppointmentDate(), dateFormatter);
                    return !appointmentDate.isBefore(today) && !appointmentDate.isAfter(futureDate);
                })
                .sorted(Comparator
                        .comparing((AppointmentDto a) -> LocalDate.parse(a.getAppointmentDate(), dateFormatter))
                        .thenComparing(a -> LocalTime.parse(a.getAppointmentTime(), timeFormatter))
                )
                .collect(Collectors.toList());
    }
    //getAppointmentStatisticsByStatus() → Map<String, Integer>
    public Map<String, Integer> getAppointmentStatisticsByStatus(){
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        return appointments.stream().collect(Collectors.groupingBy
                (AppointmentDto::getStatus, Collectors.summingInt(a->1)));
    }

    //getAppointmentsByDateRange(String startDate, String endDate) →
    //List<AppointmentDto>
    public List<AppointmentDto> getAppointmentsByDateRange(String startDate, String endDate){
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        if (startDate == null || endDate == null) {
            throw new InvalidOperationException("startDate and endDate must not be null");
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate1 = LocalDate.parse(startDate, dateFormatter);
        LocalDate endDate1 = LocalDate.parse(endDate, dateFormatter);

        if (endDate1.isBefore(startDate1)) {
            throw new InvalidOperationException("endDate must not be before startDate");
        }
        return appointments.stream().filter(a -> {
            LocalDate appointmentDate = LocalDate.parse(a.getAppointmentDate(), dateFormatter);
            return !appointmentDate.isBefore(startDate1) && !appointmentDate.isAfter(endDate1);
        }).sorted(Comparator.comparing(AppointmentDto::getAppointmentDate).thenComparing(AppointmentDto :: getAppointmentTime)).toList();
    }
    //getDailySchedule(Long doctorId, String date) →
    //List<AppointmentDto>
    public List<AppointmentDto> getDailySchedule(Long doctorId, String date){
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        if (doctorId == null) {
            throw new InvalidOperationException("doctorId must not be null");
        }
        if (date == null) {
            throw new InvalidOperationException("date must not be null");
        }
        List<AppointmentDto> schedule =
        appointments.stream().filter(a -> a.getDoctorId().equals(doctorId) && a.getAppointmentDate().equalsIgnoreCase(date))
                .sorted(Comparator.comparing(a -> LocalTime.parse(a.getAppointmentTime())))
                .collect(Collectors.toList());
        if (schedule.isEmpty()) throw new ResourceNotFoundException("No appointments found");
        return schedule;
    }
    // getAvailableTimeSlots(Long doctorId, String date) → List<String>
    public List<String> getAvailableTimeSlots(Long doctorId, String date){
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        if (doctorId == null || date == null) {
            throw new InvalidOperationException("doctorId or date must not be null");
        }

        List<String> allSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9,0);
        LocalTime endTime = LocalTime.of(17,0);
        while (startTime.isBefore(endTime)) {
            allSlots.add(startTime.toString());
            startTime = startTime.plusMinutes(15);
        }
        List<AppointmentDto> doctorAppointments = appointments.stream().filter(a ->
                a.getDoctorId().equals(doctorId) &&
                a.getAppointmentDate().equalsIgnoreCase(date)).toList();
        if (doctorAppointments.isEmpty()) {
            return allSlots;
        }
        for (AppointmentDto doctorAppointment : doctorAppointments) {
            LocalTime startDoctorAppointmentTime = LocalTime.parse(doctorAppointment.getAppointmentTime());
            LocalTime endDoctorAppointmentTime = startDoctorAppointmentTime.plusMinutes(doctorAppointment.getDuration());
            allSlots.removeIf(s -> {
                LocalTime time = LocalTime.parse(s);
                return !time.isBefore(startDoctorAppointmentTime) && time.isBefore(endDoctorAppointmentTime);
            });
        }
        return allSlots;
    }
//getConflictingAppointments(Long doctorId, String date, String time,
//int duration) → List<AppointmentDto>
    public List<AppointmentDto> getConflictingAppointments(Long doctorId, String date, String time, int duration){
        if (appointments.isEmpty()) {
            return new ArrayList<>();
        }
        if (doctorId == null || date == null || time == null) {
            throw new InvalidOperationException("doctorId or date or time must not be null");
        }
        List<AppointmentDto> conflictingAppointments = new ArrayList<>();
        List<AppointmentDto> allAppointments = appointments.stream().filter(a ->{
            return (a.getDoctorId().equals(doctorId) ) && (a.getAppointmentDate().equalsIgnoreCase(date));
        }).toList();
        LocalTime startNewAppointmentTime = LocalTime.parse(time);
        LocalTime endNewAppointmentTime = startNewAppointmentTime.plusMinutes(duration);
        for (AppointmentDto doctorAppointment : allAppointments) {
            LocalTime startDoctorAppointmentTime = LocalTime.parse(doctorAppointment.getAppointmentTime());
            LocalTime  endDoctorAppointmentTime = startDoctorAppointmentTime.plusMinutes(doctorAppointment.getDuration());
            if (startDoctorAppointmentTime.isBefore(endNewAppointmentTime) &&
                    endDoctorAppointmentTime.isAfter(startNewAppointmentTime)) {
                conflictingAppointments.add(doctorAppointment);
            }
        }
        return conflictingAppointments;

    }
    //. getMostBusyDay() → String
    public String getMostBusyDay(){
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        Map<String,Integer> dates = appointments.stream().collect(Collectors.groupingBy
                (AppointmentDto::getAppointmentDate, Collectors.summingInt(a->1)));
        int max = 0;
        String mostBusyDay = "";
        for(String date: dates.keySet()){
            if(dates.get(date)>max){
                max = dates.get(date);
                mostBusyDay = date;
            }
        }
        return mostBusyDay;
    }
//getAppointmentsByDurationRange(int minDuration, int
//maxDuration) → List<AppointmentDto>
    public List<AppointmentDto> getAppointmentsByDurationRange(int minDuration, int maxDuration){
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        return appointments.stream()
                .filter(a -> a.getDuration() >= minDuration && a.getDuration() <= maxDuration)
                .toList();
    }
    //getCancelledAppointmentsReport() → Map<String, Object>
    public Map<String,Object> getCancelledAppointmentsReport(){
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        List<AppointmentDto> cancelledAppointments = appointments.stream().filter(a -> {
            return a.getStatus().equalsIgnoreCase("Cancelled");
        }).toList();
        if (cancelledAppointments.isEmpty()) {
            throw new ResourceNotFoundException("No cancelled appointments found");
        }
        int totalCancelled =  cancelledAppointments.size();
        Map<String,Object> report = new HashMap<>();
        report.put("totalCancelled",totalCancelled);

        // Group by doctorId
        Map<Long, Long> cancelledByDoctor = cancelledAppointments.stream()
                .collect(Collectors.groupingBy(AppointmentDto::getDoctorId, Collectors.counting()));
        // Group by patientId
        Map<Long, Long> cancelledByPatient = cancelledAppointments.stream()
                .collect(Collectors.groupingBy(AppointmentDto::getPatientId, Collectors.counting()));
        report.put("cancelledByDoctor",cancelledByDoctor);
        report.put("cancelledByPatient",cancelledByPatient);
        return report;
    }
}

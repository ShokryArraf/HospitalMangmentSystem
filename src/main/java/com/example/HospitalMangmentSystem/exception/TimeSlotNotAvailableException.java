package com.example.HospitalMangmentSystem.exception;

public class TimeSlotNotAvailableException extends RuntimeException{
    public TimeSlotNotAvailableException(String message){
        super(message);
    }
}

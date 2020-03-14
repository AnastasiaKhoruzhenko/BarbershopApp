package com.coursework.barbershopapp.Interface;

import com.coursework.barbershopapp.model.TimeSlot;

import java.util.List;

public interface ITimeSlotLoadListener {

    void onTimeSlotLoadListener(List<TimeSlot> timeSlotList);
    void onTimeSlotFailedListener(String message);
    void onTimeSlotLoadEmpty();
}

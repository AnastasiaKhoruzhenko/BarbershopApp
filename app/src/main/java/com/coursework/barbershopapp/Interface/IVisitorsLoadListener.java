package com.coursework.barbershopapp.Interface;

import com.coursework.barbershopapp.model.BookingInformation;

import java.util.List;

public interface IVisitorsLoadListener {

    void onVisitorsSuccessLoadListener(List<BookingInformation> visList);
    void onVisitorsLoadFailedListener(String message);
    void onVisitorsLoadEmpty();
}

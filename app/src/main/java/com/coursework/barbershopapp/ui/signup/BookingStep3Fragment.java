package com.coursework.barbershopapp.ui.signup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coursework.barbershopapp.Interface.ITimeSlotLoadListener;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.TimeSlot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class BookingStep3Fragment extends Fragment implements ITimeSlotLoadListener {

    static BookingStep3Fragment instance;
    private DocumentReference docRef;

    ITimeSlotLoadListener iTimeSlotLoadListener;
    Unbinder unbinder;
    Calendar selected_date;
    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recview_time)
    RecyclerView recyclerViewTime;
//    @BindView(R.id.calendarView)
//    HorizontalCalendar calendarViewHor;
    SimpleDateFormat simpleDateFormat;

    private FirebaseFirestore db;

    public static BookingStep3Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep3Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iTimeSlotLoadListener = this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_booking_step3, container, false);

        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

        unbinder = ButterKnife.bind(this, view);

        Log.d("error", "on create view before init");

        selected_date = Calendar.getInstance();
        selected_date.add(Calendar.DATE, 0);

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .defaultSelectedDate(startDate)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if(selected_date.getTime() != date.getTime())
                {
                    selected_date = date;
                    loadAllBarbers(selected_date, "");
                    simpleDateFormat.format(date.getTime());
                }
            }
        });

        init(view);

        db = FirebaseFirestore.getInstance();

        return view;
    }

    private void loadAllBarbers(Calendar selected_date, String s) {
    }

    private void init(View view) {

        recyclerViewTime.setHasFixedSize(true);
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        //recyclerViewTime.setLayoutManager(gridLayoutManager);
        //recyclerViewTime.addItemDecoration(new SpacesItemDecoration(8));


//        Calendar startDate = Calendar.getInstance();
//        startDate.add(Calendar.DATE, 0);
//        Calendar endDate = Calendar.getInstance();
//        endDate.add(Calendar.DATE, 10);
//        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
//                .range(startDate, endDate)
//                .datesNumberOnScreen(7)
//                .defaultSelectedDate(startDate)
//                .build();
//
//        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
//            @Override
//            public void onDateSelected(Calendar date, int position) {
//                if(selected_date.getTimeInMillis() != date.getTimeInMillis())
//                {
//                    selected_date = date;
//                    // loadAvailiable timeslot barbers part6, 34:48
//
//                }
//            }
//        });

        iTimeSlotLoadListener.onTileSlotLoadEmpty();
    }

//    @Override
//    public void onDestroy() {
//        //localBroadcastManager.unregisterReceiver(s);
//    }

    @Override
    public void onTimeSlotLoadListener(List<TimeSlot> timeSlotList) {
        RecyclerViewTimeSlotsAdapter adapter = new RecyclerViewTimeSlotsAdapter(getContext(), timeSlotList);
        recyclerViewTime.setAdapter(adapter);
        recyclerViewTime.setLayoutManager(new GridLayoutManager(getContext(), 5));

        Log.d("error", "on time slot load listener");
    }

    @Override
    public void onTimeSlotFailedListener(String message) {
        Log.d("error", "problem");
    }

    @Override
    public void onTileSlotLoadEmpty() {
        RecyclerViewTimeSlotsAdapter adapter = new RecyclerViewTimeSlotsAdapter(getContext());
        recyclerViewTime.setAdapter(adapter);
        recyclerViewTime.setLayoutManager(new GridLayoutManager(getContext(), 5));

        Log.d("error", "on time slot load empty");
    }
}



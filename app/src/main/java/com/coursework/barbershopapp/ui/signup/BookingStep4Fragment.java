package com.coursework.barbershopapp.ui.signup;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.coursework.barbershopapp.Interface.ITimeSlotLoadListener;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class BookingStep4Fragment extends Fragment implements ITimeSlotLoadListener {

    static BookingStep4Fragment instance;
    private DocumentReference docRef;

    ITimeSlotLoadListener iTimeSlotLoadListener;
    Unbinder unbinder;
    LocalBroadcastManager localBroadcastManager;

    AlertDialog dialog;

    @BindView(R.id.recview_time)
    RecyclerView recyclerViewTime;

    @BindView(R.id.calendarView)
    HorizontalCalendarView calendarView;
    SimpleDateFormat simpleDateFormat;

    private FirebaseFirestore db;

    BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE, 0);
            loadAvailiableTimeSlot(Common.currentBarber.getEmail(), simpleDateFormat.format(date.getTime()));
        }
    };

    private void loadAvailiableTimeSlot(String email, String format) {

        //  /Masters/a@a.ri

        db.collection("Masters").document(email).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot snapshot = task.getResult();
                        if(snapshot.exists())
                        {
                            db.collection("Masters").document(email).collection(format).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                QuerySnapshot querySnapshot = task.getResult();
                                                if(querySnapshot.isEmpty()) {
                                                    iTimeSlotLoadListener.onTileSlotLoadEmpty();
                                                    Toast.makeText(getActivity(), "emptyyy", Toast.LENGTH_SHORT).show();
                                                }
                                                else
                                                {
                                                    List<TimeSlot> times = new ArrayList<>();
                                                    for(QueryDocumentSnapshot doc : task.getResult())
                                                        times.add(doc.toObject(TimeSlot.class));
                                                    Toast.makeText(getActivity(), String.valueOf(times.size()), Toast.LENGTH_SHORT).show();
                                                    iTimeSlotLoadListener.onTimeSlotLoadListener(times);
                                                }
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    iTimeSlotLoadListener.onTimeSlotFailedListener(e.getMessage());
                                }
                            });
                        }
                    }
                });

        Toast.makeText(getActivity(), email, Toast.LENGTH_SHORT).show();
    }

    public static BookingStep4Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep4Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iTimeSlotLoadListener = this;
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(displayTimeSlot, new IntentFilter(Common.KEY_DISPLAY_TIMESLOT));
        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

        //dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(displayTimeSlot);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_booking_step4, container, false);
        unbinder = ButterKnife.bind(this, view);

        init(view);

        db = FirebaseFirestore.getInstance();

        return view;
    }

    private void init(View view) {

        recyclerViewTime.setHasFixedSize(true);
        recyclerViewTime.setLayoutManager(new GridLayoutManager(getContext(), 5));

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(startDate)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if(Common.currentDate.getTime() != date.getTime())
                {
                    Common.currentDate = date;
                    loadAvailiableTimeSlot(Common.currentBarber.getEmail(), simpleDateFormat.format(date.getTime()));
                    simpleDateFormat.format(date.getTime());
                }
            }
        });
    }

    @Override
    public void onTimeSlotLoadListener(List<TimeSlot> timeSlotList) {
        RecyclerViewTimeSlotsAdapter adapter = new RecyclerViewTimeSlotsAdapter(getContext(), timeSlotList);
        recyclerViewTime.setAdapter(adapter);
    }

    @Override
    public void onTimeSlotFailedListener(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTileSlotLoadEmpty() {
        RecyclerViewTimeSlotsAdapter adapter = new RecyclerViewTimeSlotsAdapter(getContext());
        recyclerViewTime.setAdapter(adapter);
    }
}



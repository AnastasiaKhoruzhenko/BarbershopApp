package com.coursework.barbershopapp.User.ui.signup;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class BookingStep4Fragment extends Fragment implements ITimeSlotLoadListener {

    private static BookingStep4Fragment instance;
    private DocumentReference docRef;
    private ITimeSlotLoadListener iTimeSlotLoadListener;
    private LocalBroadcastManager localBroadcastManager;
    private SimpleDateFormat simpleDateFormat;
    private FirebaseFirestore db;

    @BindView(R.id.textView9)
    TextView emptyRec;
    @BindView(R.id.recview_time)
    RecyclerView recyclerViewTime;

    @BindView(R.id.calendarView)
    HorizontalCalendarView calendarView;

    private BroadcastReceiver displayTimeSlot = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Calendar date = Calendar.getInstance();
            date.add(Calendar.DATE, 0);
            //Common.currentDate.add(Calendar.DATE, 0);
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
                                                Calendar rightNow = Calendar.getInstance();
                                                Date currentTime = Calendar.getInstance().getTime();
                                                // return the hour in 24 hrs format (ranging from 0-23)
                                                int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);

                                                QuerySnapshot querySnapshot = task.getResult();
                                                List<TimeSlot> times = new ArrayList<>();
                                                if((currentHourIn24Format>19)
                                                        && simpleDateFormat.format(currentTime).equals(format)) {
                                                    emptyRec.setText(R.string.error_book_today);
                                                    iTimeSlotLoadListener.onTimeSlotLoadListener(times);
                                                }
                                                else if(querySnapshot.isEmpty()) {
                                                    int time = Integer.valueOf(Common.currentServiceType.getTime());
                                                    int countSl = Integer.valueOf(String.valueOf(Math.round(Math.ceil(time/20.0))));
                                                    if(currentHourIn24Format>=10 && currentHourIn24Format <= 19
                                                            && simpleDateFormat.format(currentTime).equals(format))
                                                    {
                                                        int k = Common.convertHourToIndex(currentHourIn24Format);
                                                        for(int i=k;i<=32 - countSl;i++)
                                                        {
                                                            times.add(new TimeSlot(Long.valueOf(i)));
                                                        }
                                                        iTimeSlotLoadListener.onTimeSlotLoadListener(times);
                                                        emptyRec.setText("");
                                                    }
                                                    else {
                                                        for (int i = 0; i <= 32-countSl; i++)
                                                            times.add(new TimeSlot(Long.valueOf(i)));
                                                        iTimeSlotLoadListener.onTimeSlotLoadListener(times);
                                                        emptyRec.setText("");
                                                    }
                                                }
                                                else
                                                {
                                                    List<Integer> intList = new ArrayList<>();
                                                    int time = Integer.valueOf(Common.currentServiceType.getTime());
                                                    int countSl = Integer.valueOf(String.valueOf(Math.round(Math.ceil(time/20.0))));
                                                    Toast.makeText(getActivity(), String.valueOf(countSl), Toast.LENGTH_SHORT).show();

                                                    for(QueryDocumentSnapshot doc : task.getResult())
                                                    {
                                                        String str = doc.getId();
                                                        if(!str.contains(".")) {
                                                            intList.add(Integer.valueOf(str));
                                                            for(int i=Integer.valueOf(str)-countSl + 1;i<=Integer.valueOf(str);i++)
                                                            {
                                                                if(!intList.contains(i))
                                                                    intList.add(i);
                                                            }
                                                        }
                                                        else
                                                        {
                                                            String[] arr = str.split("\\.");
                                                            int l = Integer.valueOf(arr[0])+Integer.valueOf(arr[1]);
                                                            intList.add(l);
                                                            for(int i = l-countSl + 1;i<=l;i++)
                                                            {
                                                                if(!intList.contains(i))
                                                                    intList.add(i);
                                                            }
                                                        }
                                                    }
                                                    for(int i = 33 - countSl; i <= 32; i++) {
                                                        if (!intList.contains(i))
                                                            intList.add(i);
                                                    }

                                                    if(currentHourIn24Format>=10 && currentHourIn24Format <= 19
                                                            && simpleDateFormat.format(currentTime).equals(format))
                                                    {
                                                        int k = Common.convertHourToIndex(currentHourIn24Format);
                                                        for(int i=0;i<=k;i++)
                                                        {
                                                            intList.add(i);
                                                        }
                                                    }

                                                    for(int i=0;i<=32;i++)
                                                    {
                                                        if(!intList.contains(i)) {
                                                            times.add(new TimeSlot(Long.valueOf(i)));
                                                        }
                                                    }

                                                    iTimeSlotLoadListener.onTimeSlotLoadListener(times);
                                                    emptyRec.setText("");
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
        Unbinder unbinder = ButterKnife.bind(this, view);

        init(view);

        db = FirebaseFirestore.getInstance();

        return view;
    }

    private void init(View view) {

        recyclerViewTime.setHasFixedSize(true);
        recyclerViewTime.setLayoutManager(new LinearLayoutManager(getContext()));

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 2);

        Calendar defaultDate = Calendar.getInstance();

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .mode(HorizontalCalendar.Mode.DAYS)
                .defaultSelectedDate(defaultDate)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                if(Common.currentDate.getTime() != date.getTime())
                {
                    Common.currentDate = date;
                }
                loadAvailiableTimeSlot(Common.currentBarber.getEmail(), simpleDateFormat.format(Common.currentDate.getTime()));
                simpleDateFormat.format(Common.currentDate.getTime());
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
    public void onTimeSlotLoadEmpty() {
        RecyclerViewTimeSlotsAdapter adapter = new RecyclerViewTimeSlotsAdapter(getContext());
        recyclerViewTime.setAdapter(adapter);
    }
}



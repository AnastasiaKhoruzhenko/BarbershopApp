package com.coursework.barbershopapp.ui.signup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.Common;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.ColorLong;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BookingStep5Fragment extends Fragment {

    @BindView(R.id.tv_chosen_time)
    TextView chosen_time;
    @BindView(R.id.tv_chosen_barber)
    TextView chosen_barber;
    @BindView(R.id.tv_chosen_service)
    TextView chosen_service;
    @BindView(R.id.tv_chosen_barber_my)
    TextView chosen_barber_my;
    @BindView(R.id.tv_chosen_time_my)
    TextView chosen_time_my;
    @BindView(R.id.tv_chosen_service_my)
    TextView chosen_service_my;
    @BindView(R.id.tv_booking_details)
    TextView bookingDetails;
    @BindView(R.id.btn_confirm)
    Button confirm;

    @OnClick(R.id.btn_confirm)
    void setConfirm(){
        BookingInformation info = new BookingInformation();

        info.setBarberEmail(Common.currentBarber.getEmail());
        info.setBarberName(Common.currentBarber.getName());
        info.setBarberSurname(Common.currentBarber.getSurname());
        info.setService(Common.currentServiceType.getTitle());
        info.setPrice(Long.valueOf(Common.currentServiceType.getPrice()));
        info.setCustomerName(" Cus Name");
        info.setRating(String.valueOf(-1));
        info.setCustomerSurname("Cus Surname");
        info.setSlot(Long.valueOf(Common.currentTimeSlot));
        info.setTime(Common.convertTimeSlotToString(Common.currentTimeSlot));
        info.setDateId(simpleDateFormat.format(Common.currentDate.getTime()));
        info.setDate(simpleDateFormatForDB.format(Common.currentDate.getTime()));

        DocumentReference doc = FirebaseFirestore.getInstance()
                .collection("Masters").document(Common.currentBarber.getEmail())
                .collection(Common.simpleDateFormat.format(Common.currentDate.getTime()))
                .document(String.valueOf(Common.currentTimeSlot));

        doc.set(info)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // go to some fragment
                        resetStaticData();
                        Toast.makeText(getContext(), "Confirm done", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Confirm error", Toast.LENGTH_SHORT).show();
            }
        });



        //   /Users/rfff@mail.ru/Visitings/1,2,3,4
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();

        db.collection("Users").document("rfff@mail.ru").collection("Visitings").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            int count =0;
                            for(QueryDocumentSnapshot d : task.getResult())
                                count++;

                            setUserVisiting(count, info);

                        }
                    }
                });
    }

    private void setUserVisiting(int count, BookingInformation info) {
        FirebaseFirestore.getInstance().collection("Users").document("rfff@mail.ru")
                .collection("Visitings").document(String.valueOf(count)).set(info);
    }


    private void resetStaticData() {
        Common.STEP = 0;
        Common.currentDate.add(Calendar.DATE, 0);
        Common.currentTimeSlot = -1;
        Common.currentBarber = null;
        Common.currentService = null;
        Common.currentServiceType = null;
    }

    private Unbinder unbinder;

    SimpleDateFormat simpleDateFormat, simpleDateFormatForDB;
    LocalBroadcastManager localBroadcastManager;

    static BookingStep5Fragment instance;

    BroadcastReceiver confurmBookingReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        chosen_barber_my.setText(Common.currentBarber.getName());
        chosen_service_my.setText(Common.currentServiceType.getTitle());
        chosen_time_my.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
        .append(" ")
        .append(simpleDateFormatForDB.format(Common.currentDate.getTime())));
    }

    public static BookingStep5Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep5Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleDateFormatForDB = new SimpleDateFormat("dd/MM/yyyy");
        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confurmBookingReciever, new IntentFilter(Common.KEY_CONFURM_BOOKING));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confurmBookingReciever);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_booking_step5, container, false);

        unbinder = ButterKnife.bind(this, view);

        return view;
    }
}

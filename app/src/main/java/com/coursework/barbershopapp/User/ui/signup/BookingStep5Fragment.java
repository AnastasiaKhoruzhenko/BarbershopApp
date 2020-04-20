package com.coursework.barbershopapp.User.ui.signup;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.User.ui.home.HomeFragment;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.Comment;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.Master;
import com.coursework.barbershopapp.model.TranslitClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BookingStep5Fragment extends Fragment{

    private static final int TAG = 1;
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
    @BindView(R.id.til_name_vis)
    TextInputLayout layName;
    @BindView(R.id.til_phone_vis)
    TextInputLayout layPhone;
    @BindView(R.id.ti_name_vis)
    TextInputEditText textName;
    @BindView(R.id.ti_phone_vis)
    TextInputEditText textPhone;
    @BindView(R.id.cardview_name_phone)
    CardView card;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final static String DEFAULT = "N/A";

    @OnClick(R.id.btn_confirm)
    void setConfirm() {

        UUID uniqueId = UUID.randomUUID();
        if (user != null)
        {
            BookingInformation info = new BookingInformation();
            db.collection("Users").document(user.getEmail()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            String name = task.getResult().getString("name");
                            String phone = task.getResult().getString("phone");
                            String surname = task.getResult().getString("surname");

                            info.setBarberEmail(Common.currentBarber.getEmail());
                            info.setBarberName(Common.currentBarber.getName());
                            info.setBarberSurname(Common.currentBarber.getSurname());
                            info.setService(Common.currentServiceType.getTitle());
                            info.setPrice(Long.valueOf(Common.currentServiceType.getPrice()));
                            info.setCustomerName(name);
                            info.setTimeService(Common.currentServiceType.getTime());
                            info.setRating(String.valueOf(-1));
                            info.setCustomerPhone(phone);
                            info.setId(String.valueOf(uniqueId));
                            info.setCustomerSurname(surname);
                            info.setServiceEN(Common.currentServiceType.getTitleEN());
                            info.setCustomerEmail(mAuth.getCurrentUser().getEmail());
                            info.setServiceId(Common.currentService.getName());
                            info.setSlot(Long.valueOf(Common.currentTimeSlot));
                            info.setTime(Common.convertTimeSlotToString(Common.currentTimeSlot));
                            info.setDateId(simpleDateFormat.format(Common.currentDate.getTime()));
                            info.setDate(simpleDateFormatForDB.format(Common.currentDate.getTime()));


                            int count = Math.round(Integer.valueOf(Common.currentServiceType.getTime())/20);
                            //Toast.makeText(getActivity(), count, Toast.LENGTH_LONG).show();

                            for(int i=0;i<=count;i++)
                            {
                                CollectionReference colRef = FirebaseFirestore.getInstance()
                                        .collection("Masters").document(Common.currentBarber.getEmail())
                                        .collection(Common.simpleDateFormat.format(Common.currentDate.getTime()));

                                if(i == 0)
                                {
                                    colRef.document(String.valueOf(Common.currentTimeSlot))
                                            .set(info)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // go to some fragment

                                                    //Toast.makeText(getContext(), "Confirm done", Toast.LENGTH_SHORT).show();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });
                                }
                                else
                                {
                                    colRef.document(String.valueOf(Common.currentTimeSlot)+"."+i)
                                            .set(info)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // go to some fragment

                                                    //Toast.makeText(getContext(), "Confirm done", Toast.LENGTH_SHORT).show();

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                        }
                                    });
                                }
                            }

                            //   /Users/rfff@mail.ru/Visitings/1,2,3,4
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> user = new HashMap<>();

                            db.collection("Users").document(task.getResult().getString("email"))
                                    .collection("Visitings").get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                int max = 0;
                                                for (QueryDocumentSnapshot d : task.getResult())
                                                {
                                                    if(Integer.valueOf(d.getId())>max)
                                                        max = Integer.valueOf(d.getId());
                                                }
                                                info.setIdVisiting(String.valueOf(max+1));
                                                setUserVisiting(max+1, info, mAuth.getCurrentUser().getEmail());

                                            }
                                        }
                                    });

                        }
                    });
        } else
            {
                if (textName.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.set_all_fields), Toast.LENGTH_SHORT).show();
                } else if (textPhone.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), getResources().getString(R.string.set_all_fields), Toast.LENGTH_SHORT).show();
                } else {
                    BookingInformation info = new BookingInformation();

                    info.setBarberEmail(Common.currentBarber.getEmail());
                    info.setBarberName(Common.currentBarber.getName());
                    info.setBarberSurname(Common.currentBarber.getSurname());
                    info.setService(Common.currentServiceType.getTitle());
                    info.setPrice(Long.valueOf(Common.currentServiceType.getPrice()));
                    info.setCustomerName(textName.getText().toString());
                    info.setRating(String.valueOf(-1));
                    info.setCustomerPhone("");
                    info.setId(String.valueOf(uniqueId));
                    info.setServiceEN(Common.currentServiceType.getTitleEN());
                    info.setTimeService(Common.currentServiceType.getTime());
                    info.setCustomerEmail(textPhone.getText().toString());
                    info.setCustomerSurname("");
                    info.setServiceId(Common.currentService.getName());
                    info.setSlot(Long.valueOf(Common.currentTimeSlot));
                    info.setTime(Common.convertTimeSlotToString(Common.currentTimeSlot));
                    info.setDateId(simpleDateFormat.format(Common.currentDate.getTime()));
                    info.setDate(simpleDateFormatForDB.format(Common.currentDate.getTime()));

                    int count = Math.round(Integer.valueOf(Common.currentServiceType.getTime())/20);


                    for(int i=0;i<=count;i++) {

                        CollectionReference colRef = FirebaseFirestore.getInstance()
                                .collection("Masters").document(Common.currentBarber.getEmail())
                                .collection(Common.simpleDateFormat.format(Common.currentDate.getTime()));
                        if(i==0)
                        {
                            colRef.document(String.valueOf(Common.currentTimeSlot)).set(info)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // go to some fragment

                                            //Toast.makeText(getContext(), "Confirm done", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        }
                        else{
                            colRef.document(String.valueOf(Common.currentTimeSlot)+"."+i).set(info)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // go to some fragment

                                            //Toast.makeText(getContext(), "Confirm done", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                        }
                    }


                    //   /Users/rfff@mail.ru/Visitings/1,2,3,4
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    Map<String, Object> user = new HashMap<>();

                    db.collection("Users").document(textPhone.getText().toString())
                            .collection("Visitings").get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        int max = 0;
                                        for (QueryDocumentSnapshot d : task.getResult())
                                        {
                                            if(Integer.valueOf(d.getId())>max)
                                                max = Integer.valueOf(d.getId());
                                        }
                                        info.setIdVisiting(String.valueOf(max+1));
                                        setUserVisiting(max+1, info, textPhone.getText().toString());

                                    }
                                }
                            });
            }
        }

        Toast.makeText(getContext(), getResources().getString(R.string.confirm_successful), Toast.LENGTH_SHORT).show();
        //resetStaticData();

//        HomeFragment homeFragment = new HomeFragment();
//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.fr5_user, homeFragment);
//        fragmentTransaction.commit();
    }

    private void setUserVisiting(int count, BookingInformation info, String email) {
        FirebaseFirestore.getInstance().collection("Users").document(email)
                .collection("Visitings").document(String.valueOf(count)).set(info);

        db.collection("Masters").document(Common.currentBarber.getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    Master master = task.getResult().toObject(Master.class);
                    if(master.getDates() == null) {
                        Map<String, Object> map = new HashMap<>();
                        List<String> datesList = new ArrayList<>();
                        datesList.add(simpleDateFormat.format(Common.currentDate.getTime()));
                        //master.getDates().add(simpleDateFormat.format(Common.currentDate.getTime()));
                        map.put("dates", datesList);
                        db.collection("Masters").document(Common.currentBarber.getEmail())
                                .update(map);
                    }
                    else if(!master.getDates().contains(simpleDateFormat.format(Common.currentDate.getTime())))
                    {
                        Map<String, Object> map = new HashMap<>();
                        master.getDates().add(simpleDateFormat.format(Common.currentDate.getTime()));
                        map.put("dates", master.getDates());
                        db.collection("Masters").document(Common.currentBarber.getEmail())
                                .update(map);
                    }
                }
            }
        });

        saveText();
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

    private SimpleDateFormat simpleDateFormat, simpleDateFormatForDB;
    private LocalBroadcastManager localBroadcastManager;

    private static BookingStep5Fragment instance;

    private BroadcastReceiver confirmBookingReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        SharedPreferences prefs = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "ru");

        TranslitClass translitClass = new TranslitClass();

        if(language.equals("ru"))
        {
            chosen_barber_my.setText(Common.currentBarber.getName() + " " + Common.currentBarber.getSurname());
            chosen_service_my.setText(Common.currentServiceType.getTitle());
        }
        else
        {
            chosen_barber_my.setText(translitClass.toTranslit(Common.currentBarber.getName()) + " " + translitClass.toTranslit(Common.currentBarber.getSurname()));
            chosen_service_my.setText(Common.currentServiceType.getTitleEN());
        }

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
        localBroadcastManager.registerReceiver(confirmBookingReciever, new IntentFilter(Common.KEY_CONFURM_BOOKING));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReciever);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_booking_step5, container, false);

        unbinder = ButterKnife.bind(this, view);

        user = mAuth.getCurrentUser();
        if(user!=null)
            card.setVisibility(View.GONE);
        else
            card.setVisibility(View.VISIBLE);

        loadEmail();

        return view;
    }

    private  void saveText(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", textPhone.getText().toString());
        editor.commit();
    }

    private void loadEmail(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", DEFAULT);
        if(!email.equals(DEFAULT))
            textPhone.setText(email);
    }
}

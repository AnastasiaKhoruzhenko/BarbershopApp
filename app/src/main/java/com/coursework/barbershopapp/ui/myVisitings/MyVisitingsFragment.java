package com.coursework.barbershopapp.ui.myVisitings;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Banner;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.Person;
import com.coursework.barbershopapp.ui.settings.SettingsViewModel;
import com.coursework.barbershopapp.ui.signup.RecycleViewAdapterStep1;
import com.coursework.barbershopapp.ui.signup.RecyclerViewMastersChooseAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyVisitingsFragment extends Fragment {

    private MyVisitingsViewModel myVisitingsViewModel;
    FirebaseFirestore db;

    List<BookingInformation> bookingList;
    RecyclerView recyclerView;

    public static MyVisitingsFragment newInstance() {
        return new MyVisitingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myVisitingsViewModel =
                ViewModelProviders.of(this).get(MyVisitingsViewModel.class);
        bookingList = new ArrayList<>();
        View root = inflater.inflate(R.layout.my_visitings_fragment, container, false);
        recyclerView = root.findViewById(R.id.recview_my_vis);

        loadData();

        resetStaticData();

        return root;
    }

    private void loadData() {

        db = FirebaseFirestore.getInstance();
        db.collection("Users").document("rfff@mail.ru").collection("Visitings")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    List<BookingInformation> list = new ArrayList<>();
                    for(QueryDocumentSnapshot doc : task.getResult())
                    {
                        list.add(doc.toObject(BookingInformation.class));
                    }
                    initRecView(list);
                }
            }
        });
    }

    private void initRecView(List<BookingInformation> list) {
        RecyclerViewMyVisitingAdapter recView = new RecyclerViewMyVisitingAdapter(getContext(), list);
        recyclerView.setAdapter(recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void resetStaticData() {
        Common.STEP = 0;
        Common.currentDate.add(Calendar.DATE, 0);
        Common.currentTimeSlot = -1;
        Common.currentBarber = null;
        Common.currentService = null;
        Common.currentServiceType = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myVisitingsViewModel = ViewModelProviders.of(this).get(MyVisitingsViewModel.class);
        // TODO: Use the ViewModel
    }

}

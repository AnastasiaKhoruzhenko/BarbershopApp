package com.coursework.barbershopapp.User.ui.myVisitings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.coursework.barbershopapp.model.Common.simpleDateFormat;

public class TabVisitingFragment extends Fragment {

    RecyclerView recyclerView;
    FirebaseFirestore db;
    FirebaseAuth user;

    int title;

    public TabVisitingFragment() { }

    public TabVisitingFragment(int title) {
        this.title = title;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = FirebaseAuth.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_visiting_fragment, container, false);

        recyclerView = view.findViewById(R.id.recview_my_vis);

        loadData(title);

        resetStaticData();

        return view;
    }

    private void loadData(int title) {

        db = FirebaseFirestore.getInstance();

        db.collection("Users").document("rfff@mail.ru").collection("Visitings")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    List<BookingInformation> list = new ArrayList<>();
                    switch (title)
                    {
                        case 1:
                            for(QueryDocumentSnapshot doc : task.getResult())
                            {
                                BookingInformation info = doc.toObject(BookingInformation.class);
                                long inDays = 0;
                                try {
                                    Date nowDate = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime()));
                                    Date appointmentDate = simpleDateFormat.parse(info.getDateId());
                                    inDays = (nowDate.getTime() - appointmentDate.getTime())/ (24 * 60 * 60 * 1000);
                                }
                                catch (ParseException e)
                                {
                                    e.printStackTrace();
                                }
                                if(inDays <= 0)
                                {
                                    list.add(info);
                                }
                            }
                            initRecView(title, list);
                            break;
                        case 2:
                            for(QueryDocumentSnapshot doc : task.getResult())
                            {
                                BookingInformation info = doc.toObject(BookingInformation.class);
                                long inDays = 0;
                                try {
                                    Date nowDate = simpleDateFormat.parse(simpleDateFormat.format(Calendar.getInstance().getTime()));
                                    Date appointmentDate = simpleDateFormat.parse(info.getDateId());
                                    inDays = (nowDate.getTime() - appointmentDate.getTime())/ (24 * 60 * 60 * 1000);
                                }
                                catch (ParseException e)
                                {
                                    e.printStackTrace();
                                }
                                if(inDays > 0)
                                {
                                    list.add(info);
                                }
                            }
                            initRecView(title, list);
                            break;
                            default:
                                Toast.makeText(getActivity(), "in titles", Toast.LENGTH_LONG).show();
                                break;
                    }
                }
            }
        });
    }

    private void initRecView(int title, List<BookingInformation> list) {

        RecyclerViewMyVisitingAdapter recView = new RecyclerViewMyVisitingAdapter(getActivity(), list, title);
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
}

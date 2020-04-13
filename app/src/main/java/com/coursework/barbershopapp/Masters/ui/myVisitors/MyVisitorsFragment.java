package com.coursework.barbershopapp.Masters.ui.myVisitors;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.HorizontalCalendarView;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.coursework.barbershopapp.Interface.IVisitorsLoadListener;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MyVisitorsFragment extends Fragment implements IVisitorsLoadListener {

    private IVisitorsLoadListener iVisitorsLoadListener;
    private FirebaseFirestore db;
    private SimpleDateFormat simpleDateFormat;
    private FirebaseAuth mAuth;

    @BindView(R.id.tv_empty_visitors)
    TextView empty;
    @BindView(R.id.recview_my_visitors)
    RecyclerView recyclerView;
    @BindView(R.id.calendar_master)
    HorizontalCalendarView calendarView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_visitors_fragment, container, false);
        Unbinder unbinder = ButterKnife.bind(this, view);

        iVisitorsLoadListener = this;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");

        init(view);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init(View view) {

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, 0);
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 2);

        Calendar defaultDate = Calendar.getInstance();

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(view, R.id.calendar_master)
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
                loadAvailiableTimeSlot(mAuth.getCurrentUser().getEmail(), simpleDateFormat.format(Common.currentDate.getTime()));
                simpleDateFormat.format(Common.currentDate.getTime());
            }
        });


    }

    private void loadAvailiableTimeSlot(String s, String format) {

        db.collection("Masters").document(s).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot snapshot = task.getResult();
                        if(snapshot.exists())
                        {
                            //db.collection("masters").document(s).collection(format).orderBy("slot");
                            db.collection("Masters").document(s)
                                    .collection(format)
                                    .orderBy("slot")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if(task.isSuccessful())
                                            {
                                                QuerySnapshot querySnapshot = task.getResult();
                                                if(querySnapshot.isEmpty())
                                                {
                                                    iVisitorsLoadListener.onVisitorsLoadEmpty();
                                                }
                                                else{
                                                    List<BookingInformation> visList=new ArrayList<>();
                                                    for(DocumentSnapshot doc : querySnapshot)
                                                        if(!doc.getId().contains("."))
                                                            visList.add(doc.toObject(BookingInformation.class));

                                                    iVisitorsLoadListener.onVisitorsSuccessLoadListener(visList);
                                                }
                                            }
                                        }
                                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    iVisitorsLoadListener.onVisitorsLoadFailedListener(e.getMessage());
                                }
                            });
                        }
                    }
                });

    }

    @Override
    public void onVisitorsSuccessLoadListener(List<BookingInformation> visList) {
        empty.setText("");
        RecycleViewMyVisitorsAdapter adapter = new RecycleViewMyVisitorsAdapter(getContext(), visList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onVisitorsLoadFailedListener(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVisitorsLoadEmpty() {
        RecycleViewMyVisitorsAdapter adapter = new RecycleViewMyVisitorsAdapter(getContext());
        recyclerView.setAdapter(adapter);
        empty.setText(getResources().getString(R.string.no_bookings_on_date));
    }
}

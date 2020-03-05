package com.coursework.barbershopapp.ui.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coursework.barbershopapp.Admin.ui.masters.RecyclerViewAdapter;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Banner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookingStep1Fragment extends Fragment {

    static BookingStep1Fragment instance;

    private RecyclerView recyclerView;

    public static BookingStep1Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep1Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_booking_step1, container, false);
        recyclerView = view.findViewById(R.id.recview_frag1);

        loadImgAndTitles();

        return view;
    }

    private void loadImgAndTitles() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ServicesMan").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<Banner> list = new ArrayList<>();
                            for (QueryDocumentSnapshot querySnapshot : task.getResult())
                            {
                                Banner aboutService = querySnapshot.toObject(Banner.class);
                                list.add(aboutService);
                            }
                            initRecView(list);
                        }
                    }
                });
    }

    private void initRecView(List<Banner> listServices) {
        RecycleViewAdapterStep1 recView = new RecycleViewAdapterStep1(getContext(), listServices){
            @Override
            protected void OnCardViewClicked() {

            }
        };
        recyclerView.setAdapter(recView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

    }
}

package com.coursework.barbershopapp.ui.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.AboutService;
import com.coursework.barbershopapp.model.Banner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookingStep2Fragment extends Fragment {

    static BookingStep2Fragment instance;

    private RecyclerView recyclerView;

    private FirebaseFirestore db;

    public static BookingStep2Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep2Fragment();
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

        View view = inflater.inflate(R.layout.fragment_booking_step2, container, false);
        recyclerView = view.findViewById(R.id.recview_frag2);
        db = FirebaseFirestore.getInstance();

        db.collection("ServicesMan").document("HairCut").collection("Services").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<AboutService> list = new ArrayList<>();
                            for (QueryDocumentSnapshot querySnapshot : task.getResult())
                            {
                                AboutService aboutService = querySnapshot.toObject(AboutService.class);
                                list.add(aboutService);
                            }

                            initRecView(list);
                        }
                    }
                });

        return view;
    }

    private void initRecView(List<AboutService> listServices) {
        RecycleViewAdapterStep2 recView = new RecycleViewAdapterStep2(getContext(), listServices);
        recyclerView.setAdapter(recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}

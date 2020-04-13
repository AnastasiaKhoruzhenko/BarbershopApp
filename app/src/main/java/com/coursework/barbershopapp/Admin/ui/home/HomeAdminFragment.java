package com.coursework.barbershopapp.Admin.ui.home;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Banner;
import com.coursework.barbershopapp.model.Master;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeAdminFragment extends Fragment {

    private RecyclerView recyclerView, recyclerViewServices;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_admin_fragment, container, false);

        Unbinder unbinder = ButterKnife.bind(this, view);

        recyclerView = view.findViewById(R.id.review_admin_homeMasters);
        recyclerViewServices = view.findViewById(R.id.rec_edit_services);
        db = FirebaseFirestore.getInstance();
        initMasters();
        initServices();


        return view;
    }

    private void initServices() {
        db.collection("ServicesMan").orderBy("name").get()
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
        RecycleViewServicesAdapter recView = new RecycleViewServicesAdapter(getContext(), listServices);
        recyclerViewServices.setAdapter(recView);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewServices.setLayoutManager(layoutManager);

    }

    private void initMasters() {

        db.collection("Masters").orderBy("score", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<Master> list=new ArrayList<>();
                            for (QueryDocumentSnapshot doc : task.getResult())
                            {
                                list.add(doc.toObject(Master.class));
                            }

                            initRecVie(list);
                        }
                    }
                });
    }

    private void initRecVie(List<Master> list) {

        RecyclerViewBestMastersAdapter adapter = new RecyclerViewBestMastersAdapter(list, getContext());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

}

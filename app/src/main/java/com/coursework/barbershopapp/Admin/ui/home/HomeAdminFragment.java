package com.coursework.barbershopapp.Admin.ui.home;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coursework.barbershopapp.Admin.ui.masters.RecyclerViewAdapter;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeAdminFragment extends Fragment {

    private HomeAdminViewModel mViewModel;
    RecyclerView recyclerView;
    FirebaseFirestore db;

    public static HomeAdminFragment newInstance() {
        return new HomeAdminFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_admin_fragment, container, false);

        recyclerView = view.findViewById(R.id.review_admin_homeMasters);
        db = FirebaseFirestore.getInstance();
        initMasters();


        return view;
    }

    private void initMasters() {

        db.collection("Masters").orderBy("score").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<Person> list=new ArrayList<>();
                            for (QueryDocumentSnapshot doc : task.getResult())
                            {
                                list.add(doc.toObject(Person.class));
                            }

                            initRecView(list);
                        }
                    }
                });
    }

    private void initRecView(List<Person> list) {

        RecyclerViewBestMastersAdapter adapter = new RecyclerViewBestMastersAdapter(list, getContext());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeAdminViewModel.class);
        // TODO: Use the ViewModel
    }

}

package com.coursework.barbershopapp.Masters.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Master;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeMasterFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    public static HomeMasterFragment newInstance() {
        return new HomeMasterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_master_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        mAuth=FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
            checkDefaulPassword(mAuth.getCurrentUser().getEmail());

        return view;
    }

    private void checkDefaulPassword(String email) {
        db.collection("Masters").document(email).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            Master master = task.getResult().toObject(Master.class);
                            if(master.getDefaultPass())
                                showChangeDefaultPassword(email);
                        }
                    }
                });
    }

    private void showChangeDefaultPassword(String email) {
        
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}

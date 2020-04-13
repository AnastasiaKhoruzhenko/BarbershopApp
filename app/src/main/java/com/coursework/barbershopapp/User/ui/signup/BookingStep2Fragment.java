package com.coursework.barbershopapp.User.ui.signup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.AboutService;
import com.coursework.barbershopapp.model.Common;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookingStep2Fragment extends Fragment {

    private static BookingStep2Fragment instance;
    private LocalBroadcastManager localBroadcastManager;
    private RecyclerView recyclerView;

    private BroadcastReceiver serviceDoneReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ArrayList<AboutService> aboutServices = intent.getParcelableArrayListExtra(Common.KEY_SERVICE_LOAD_DONE);

            RecycleViewAdapterStep2 adapter = new RecycleViewAdapterStep2(getContext(), aboutServices);
            recyclerView.setAdapter(adapter);
        }
    };

    public static BookingStep2Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep2Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(serviceDoneReciever, new IntentFilter(Common.KEY_SERVICE_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(serviceDoneReciever);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_booking_step2, container, false);
        recyclerView = view.findViewById(R.id.recview_frag2);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        initRecView();

        return view;
    }

    private void initRecView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}

package com.coursework.barbershopapp.ui.myVisitings;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.ui.settings.SettingsViewModel;

public class MyVisitingsFragment extends Fragment {

    private MyVisitingsViewModel myVisitingsViewModel;

    public static MyVisitingsFragment newInstance() {
        return new MyVisitingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myVisitingsViewModel =
                ViewModelProviders.of(this).get(MyVisitingsViewModel.class);
        View root = inflater.inflate(R.layout.my_visitings_fragment, container, false);
        //final TextView textView = root.findViewById(R.id.text_notifications);
        myVisitingsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myVisitingsViewModel = ViewModelProviders.of(this).get(MyVisitingsViewModel.class);
        // TODO: Use the ViewModel
    }

}

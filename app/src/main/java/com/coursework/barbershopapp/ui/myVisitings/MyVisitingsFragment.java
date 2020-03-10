package com.coursework.barbershopapp.ui.myVisitings;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.ui.settings.SettingsViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
//        //final TextView textView = root.findViewById(R.id.text_notifications);
//        myVisitingsViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                //textView.setText(s);
//            }
//        });

        resetStaticData();

        return root;
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

package com.coursework.barbershopapp.Admin.ui.reviews;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.coursework.barbershopapp.Interface.IAllMastersLoadListener;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.Master;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ReviewsAdminFragment extends Fragment implements IAllMastersLoadListener{

    private BarChart chart;
    private IAllMastersLoadListener iAllMastersLoadListener;
    private FirebaseFirestore db;
    private MaterialSpinner spinnerName;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reviews_admin_fragment, container, false);
        iAllMastersLoadListener = this;
        chart = view.findViewById(R.id.barChart);
        spinnerName = view.findViewById(R.id.spinner1);
        db = FirebaseFirestore.getInstance();

        loadSpinners();

        //getChart();

        return view;
    }

    private void loadSpinners(){
        db.collection("Masters").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<Master> list = new ArrayList<>();
                            list.add(new Master());
                            for(QueryDocumentSnapshot doc:task.getResult())
                            {
                                list.add(doc.toObject(Master.class));
                            }
                            iAllMastersLoadListener.onAllMastersLoad(list);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iAllMastersLoadListener.onAllMastersLoadFailed(e.getMessage());
                    }
                });
    }

    @Override
    public void onAllMastersLoad(List<Master> areaEmailsList) {
        List<String> listNames = new ArrayList<>();
        for(Master master:areaEmailsList)
            listNames.add(master.getName() + " " + master.getSurname());

        spinnerName.setItems(listNames);
        spinnerName.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(!areaEmailsList.get(position).getEmail().equals(" "))
                    loadGraphics(areaEmailsList.get(position).getEmail(), areaEmailsList.get(position).getDates());
                else
                    loadEmpty();
            }
        });
    }

    private void loadEmpty(){
        ArrayList<BarEntry> list = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        BarDataSet set = new BarDataSet(list, "No data");
        BarData data = new BarData(dates, set);
        chart.setData(data);
        chart.animateY(1500);
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.invalidate();
    }

    private void loadGraphics(String email, List<String> dates) {
        for(String date : dates)
        {
            db.collection("Masters").document(email)
                    .collection(date).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful())
                            {
                                int count = 0;
                                for(DocumentSnapshot doc : task.getResult())
                                {
                                    if(!doc.getId().contains("."))
                                        count++;
                                }

                                SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
                                editor.putString(date, String.valueOf(count));
                                editor.apply();
                            }
                        }
                    });
        }

        SharedPreferences prefs = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        List<Integer> integerList = new ArrayList<>();

        ArrayList<BarEntry> list = new ArrayList<>();

        int i = 0;
        for(String date:dates)
        {
            String count = prefs.getString(date, "0");
            integerList.add(Integer.valueOf(count));

            list.add(new BarEntry(Integer.valueOf(count), i));
            i++;
        }

        BarDataSet set = new BarDataSet(list, "Cells");
        BarData data = new BarData(dates, set);
        chart.setData(data);
        chart.animateY(1500);
        set.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.invalidate();
    }

    @Override
    public void onAllMastersLoadFailed(String mess) {

    }
}

package com.coursework.barbershopapp.Admin.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.RegistrationActivity;
import com.coursework.barbershopapp.model.SalonInfo;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.OnClick;

public class SalonInfoActivity extends AppCompatActivity {

    EditText address, info;
    MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salon_info_activity);

        address = findViewById(R.id.et_address_admin);
        info = findViewById(R.id.editText3);
        map = findViewById(R.id.mapView);

        setData();


    }

    public void setData(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("SalonInfo").document("information")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    SalonInfo salonInfo = task.getResult().toObject(SalonInfo.class);
                    address.setText(salonInfo.getAddress());
                    info.setText(salonInfo.getDefinition());
                }
            }
        });
    }
}

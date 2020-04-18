package com.coursework.barbershopapp.Admin.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.RegistrationActivity;
import com.coursework.barbershopapp.model.SalonInfo;
import com.google.android.gms.maps.GoogleMap;
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

    private EditText address, info, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.salon_info_activity);

        TextView tv = findViewById(R.id.textView8);
        ImageView img = findViewById(R.id.imageView10);
        tv.setText(tv.getText() + ":");
        TextView close = findViewById(R.id.close_img);
        address = findViewById(R.id.et_address_admin);
        info = findViewById(R.id.editText3);
        phone = findViewById(R.id.et_phone_info);

        MapView map = findViewById(R.id.mapView);

        setData();

        Intent intent = getIntent();
        if(intent.hasExtra("from_user"))
        {
            address.setFocusable(false);
            address.setClickable(false);
            info.setFocusable(false);
            info.setClickable(false);
            phone.setFocusable(false);
            phone.setClickable(false);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone.getText().toString(), null));
                    startActivity(intent);
                }
            });
        }
        else
        {
            img.setVisibility(View.GONE);
        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                    phone.setText(salonInfo.getPhone());
                }
            }
        });
    }
}

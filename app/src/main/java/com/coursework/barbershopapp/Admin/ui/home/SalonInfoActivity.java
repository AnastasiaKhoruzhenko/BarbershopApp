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

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.OnClick;

public class SalonInfoActivity extends AppCompatActivity {

    @BindView(R.id.card_address)
    CardView address;
    @BindView(R.id.card_about_us)
    CardView about_us;
    @BindView(R.id.card_sales)
    CardView sales;

    @OnClick(R.id.card_address)
    void Address(){

//        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext(), R.style.CustomAlertDialog);
//        ViewGroup viewGroup = findViewById(android.R.id.content);
//        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.alert_set_stars, viewGroup, false);
//
//        EditText comment = dialogView.findViewById(R.id.et_comment);
//        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar_master);
//
//        builder.setView(dialogView);
//        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String com = comment.getText().toString();
//                Float rat = ratingBar.getRating();
//
//                Map<String, Object> map = new HashMap<>();
//                map.put("rating", String.valueOf(Math.round(rat)));
//                map.put("comment", com);
//
//                db.collection("Masters").document(bookingList.get(position).getBarberEmail())
//                        .collection(bookingList.get(position).getDateId())
//                        .document(String.valueOf(bookingList.get(position).getSlot()))
//                        .update(map);
//
//                //   /Users/rfff@mail.ru/Visitings/1
//                db.collection("Users").document("rfff@mail.ru")
//                        .collection("Visitings")
//                        .document(String.valueOf(position)).update(map);
//
//                holder.rating.setVisibility(View.VISIBLE);
//                holder.rate_me.setText("Оценено");
//                holder.rating.setRating(Math.round(rat));
//                holder.rating.setIsIndicator(true);
//            }
//        });
//        builder.setNegativeButton("ЗАКРЫТЬ", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog alertDialog = builder.create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


    }
}

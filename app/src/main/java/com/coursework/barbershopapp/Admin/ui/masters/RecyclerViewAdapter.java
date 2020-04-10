package com.coursework.barbershopapp.Admin.ui.masters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Master;
import com.coursework.barbershopapp.model.TranslitClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Master> personList = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapter(Context mContext, List<Master> personList) {
        this.mContext = mContext;
        this.personList = personList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_masters_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        SharedPreferences prefs = mContext.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "ru");

        TranslitClass translitClass = new TranslitClass();

        if(language.equals("ru"))
            holder.nameSurname.setText(personList.get(position).getName() + " " + personList.get(position).getSurname());
        else
            holder.nameSurname.setText(translitClass.toTranslit(personList.get(position).getName()) + " " + translitClass.toTranslit(personList.get(position).getSurname()));

        holder.score.setText(personList.get(position).getScore());

        StorageReference phRef = FirebaseStorage.getInstance().getReference()
                .child("personal_photos/"+personList.get(position).getEmail());
        phRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //img.setImageURI(uri);

                Glide.with(mContext)
                        .load(uri)
                        .into(holder.photo);
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(mContext, "Error", Toast.LENGTH_LONG).show();
                    }
                });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, personList.get(position).getEmail(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView photo;
        TextView nameSurname, score;
        ImageView starImg;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.masterPhoto);
            nameSurname = itemView.findViewById(R.id.masterNameSurname);
            score = itemView.findViewById(R.id.tv_score_master_admin);
            starImg = itemView.findViewById(R.id.iv_star_admin);
            cardView = itemView.findViewById(R.id.cardview_master_admin_for_list);
        }
    }
}

package com.coursework.barbershopapp.Admin.ui.home;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Comment;
import com.coursework.barbershopapp.model.Master;
import com.coursework.barbershopapp.model.TranslitClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewBestMastersAdapter extends RecyclerView.Adapter<RecyclerViewBestMastersAdapter.ViewHolder>{

    private List<Master> list;
    private Context mContext;
    private Dialog dialog;
    private FirebaseFirestore db;

    public RecyclerViewBestMastersAdapter(List<Master> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
        db = FirebaseFirestore.getInstance();
    }

    public RecyclerViewBestMastersAdapter() {
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_best_master, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences prefs = mContext.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "ru");

        TranslitClass translitClass = new TranslitClass();

        if(language.equals("ru"))
            holder.name_surname.setText(list.get(position).getName()+" "+list.get(position).getSurname());
        else
            holder.name_surname.setText(translitClass.toTranslit(list.get(position).getName())+" "+translitClass.toTranslit(list.get(position).getSurname()));


        holder.score.setText(list.get(position).getScore());

        StorageReference phRef = FirebaseStorage.getInstance().getReference()
                .child("personal_photos/"+list.get(position).getEmail());
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
                    }
                });

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!list.get(position).getScore().equals(String.valueOf(0.0)))
                    showFullDialog(v, position);
                else
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.no_comments_master), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView card;
        TextView name_surname, score;
        CircleImageView photo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card_bestmaster);
            name_surname = itemView.findViewById(R.id.tv_namesurname_home_a);
            score = itemView.findViewById(R.id.tv_score_home_adm);
            photo = itemView.findViewById(R.id.master_photo_home_adm);
        }
    }

    private void showFullDialog(View view, int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.full_master_comments_dialog, null);
        dialog = new Dialog(mContext, R.style.AppTheme_FullScreenDialog);
        dialog.setContentView(v);
        Toolbar toolbar = (Toolbar)dialog.findViewById(R.id.toolbar_close);
        TextView close = dialog.findViewById(R.id.close_img);
        RecyclerView recyclerView = dialog.findViewById(R.id.recview_comments);

        // add

        db.collection("Comments")
                .document(list.get(position).getEmail())
                .collection("Comments")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    List<Comment> commentList = new ArrayList<>();
                    for(DocumentSnapshot comment : task.getResult())
                        commentList.add(comment.toObject(Comment.class));

                    initRecViewComment(commentList, recyclerView, list.get(position).getEmail(), false);
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ConstraintLayout constraintLayout = view.findViewById(R.id.constr_master_comm);
        dialog.show();
    }

    private void initRecViewComment(List<Comment> list, RecyclerView recyclerView, String email, boolean canDelete) {
        RecyclerViewCommentAdapter adapter = new RecyclerViewCommentAdapter(mContext, list, email, canDelete);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
    }
}

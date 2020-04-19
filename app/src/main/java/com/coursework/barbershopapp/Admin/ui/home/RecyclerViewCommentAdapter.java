package com.coursework.barbershopapp.Admin.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.LoginActivity;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.RegistrationActivity;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.Comment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewCommentAdapter extends RecyclerView.Adapter<RecyclerViewCommentAdapter.ViewHolder>{

    private List<Comment> list;
    private Context mContext;
    private FirebaseFirestore db;
    private String masterEmail;

    public RecyclerViewCommentAdapter(Context mContext, List<Comment> list, String masterEmail) {
        this.list = list;
        this.mContext = mContext;
        this.masterEmail = masterEmail;
        db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_my_comments, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        holder.text.setText(list.get(position).getComment());
        holder.score.setText(list.get(position).getRating());
        holder.name_surname.setText(list.get(position).getName() + " " + list.get(position).getSurname());

        StorageReference phRef = FirebaseStorage.getInstance().getReference()
                .child("personal_photos/"+list.get(position).getCustomerEmail());
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

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.setMessage(mContext.getResources().getString(R.string.sure_to_delete));
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, mContext.getResources().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, mContext.getResources().getString(R.string.delete),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String id = list.get(position).getId();
                                list.remove(list.get(position));

                                deleteItem(position, id, Float.valueOf(holder.score.getText().toString()));
                            }
                        });
                alertDialog.show();
            }
        });
    }

    private void deleteItem(int position, String id, float score) {
        db.collection("Comments")
                .document(masterEmail)
                .collection("Comments")
                .document(id).delete();
        db.collection("Comments").document(masterEmail)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    int count = Integer.valueOf(task.getResult().getString("count"));
                    Float rating = Float.valueOf(task.getResult().getString("rating"));

                    int newCount = count - 1;
                    float newRating = 0.0f;
                    if(newCount!=0)
                        newRating = (rating*2 - score)/newCount;

                    db.collection("Comments").document(masterEmail)
                            .update("count", String.valueOf(newCount));
                    db.collection("Comments").document(masterEmail)
                            .update("rating", String.valueOf(newRating));
                    db.collection("Masters").document(masterEmail)
                            .update("score", String.valueOf(newRating));

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                notifyItemRemoved(position);
                            }
                        }, 1000);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        TextView name_surname, score, text;
        ImageView img;
        CircleImageView photo;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card_my_comments);
            name_surname = itemView.findViewById(R.id.tv_name_surname_comment);
            score = itemView.findViewById(R.id.tv_score_comment);
            text = itemView.findViewById(R.id.tv_comment_text);
            img = itemView.findViewById(R.id.imageView3);
            photo = itemView.findViewById(R.id.imageView2);
            delete = itemView.findViewById(R.id.imageView11);
        }
    }
}

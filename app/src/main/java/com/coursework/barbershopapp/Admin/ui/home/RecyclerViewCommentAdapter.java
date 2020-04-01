package com.coursework.barbershopapp.Admin.ui.home;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.Comment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewCommentAdapter extends RecyclerView.Adapter<RecyclerViewCommentAdapter.ViewHolder>{

    List<Comment> list;
    Context mContext;

    public RecyclerViewCommentAdapter(Context mContext, List<Comment> list) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_my_comments, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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
                        //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card_my_comments);
            name_surname = itemView.findViewById(R.id.tv_name_surname_comment);
            score = itemView.findViewById(R.id.tv_score_comment);
            text = itemView.findViewById(R.id.tv_comment_text);
            img = itemView.findViewById(R.id.imageView3);
            photo = itemView.findViewById(R.id.imageView2);
        }
    }
}

package com.coursework.barbershopapp.Admin.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.BookingInformation;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewCommentAdapter extends RecyclerView.Adapter<RecyclerViewCommentAdapter.ViewHolder>{

    List<BookingInformation> list;
    Context mContext;

    public RecyclerViewCommentAdapter(Context mContext, List<BookingInformation> list) {
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
        holder.name_surname.setText(list.get(position).getCustomerName()+ " "+ list.get(position).getCustomerSurname());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        TextView name_surname, score, text;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.card_my_comments);
            name_surname = itemView.findViewById(R.id.tv_name_surname_comment);
            score = itemView.findViewById(R.id.tv_score_comment);
            text = itemView.findViewById(R.id.tv_comment_text);
            img = itemView.findViewById(R.id.imageView3);
        }
    }
}

package com.coursework.barbershopapp.Admin.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursework.barbershopapp.Admin.ui.masters.RecyclerViewAdapter;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Person;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewBestMastersAdapter extends RecyclerView.Adapter<RecyclerViewBestMastersAdapter.ViewHolder>{

    List<Person> list;
    Context mContext;

    public RecyclerViewBestMastersAdapter(List<Person> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
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

        holder.score.setText(list.get(position).getScore());
        holder.name_surname.setText(list.get(position).getName()+" "+list.get(position).getSurname());
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
}

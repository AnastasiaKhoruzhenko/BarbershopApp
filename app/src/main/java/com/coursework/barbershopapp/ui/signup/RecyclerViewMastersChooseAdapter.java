package com.coursework.barbershopapp.ui.signup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Person;
import com.coursework.barbershopapp.model.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewMastersChooseAdapter extends RecyclerView.Adapter<RecyclerViewMastersChooseAdapter.MyViewHolder>{

    // /ServicesMan/HairCut/Barbers/a@a.ri
    List<String> listNameSurname = new ArrayList<>();
    List<String> listOffers = new ArrayList<>();
    List<String> listScore = new ArrayList<>();
    List<Person> personList = new ArrayList<>();

    Context mContext;

//    public RecyclerViewMastersChooseAdapter(Context mContext, List<String> listNameSurname, List<String> listOffers, List<String> listScore) {
//        this.listNameSurname = listNameSurname;
//        this.listOffers = listOffers;
//        this.listScore = listScore;
//        this.mContext = mContext;
//    }

    public RecyclerViewMastersChooseAdapter(Context mContext, List<Person> personList)
    {
        this.mContext = mContext;
        this.personList = personList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mContext).inflate(R.layout.cardview_masters, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        holder.score.setText(listScore.get(position));
//        holder.name_surname.setText(listNameSurname.get(position));
//        holder.offer.setText(listOffers.get(position));

        holder.score.setText(personList.get(position).getScore());
        holder.name_surname.setText(personList.get(position).getName() + " " + personList.get(position).getSurname());
        holder.offer.setText(personList.get(position).getPhone());

    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CardView card;
        TextView name_surname, offer, score;
        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.cardview_master_foruser);
            name_surname = itemView.findViewById(R.id.textV_name_sur);
            offer = itemView.findViewById(R.id.text_offers);
            score = itemView.findViewById(R.id.text_score);
            img = itemView.findViewById(R.id.image_photo);
        }
    }
}

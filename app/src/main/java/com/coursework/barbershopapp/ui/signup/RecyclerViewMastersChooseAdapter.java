package com.coursework.barbershopapp.ui.signup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coursework.barbershopapp.Interface.IRecyclerItemSelectedListener;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.Person;
import com.coursework.barbershopapp.model.User;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewMastersChooseAdapter extends RecyclerView.Adapter<RecyclerViewMastersChooseAdapter.MyViewHolder>{

    // /ServicesMan/HairCut/Barbers/a@a.ri
    List<String> listNameSurname = new ArrayList<>();
    List<String> listOffers = new ArrayList<>();
    List<String> listScore = new ArrayList<>();
    ArrayList<Person> personList = new ArrayList<>();
    List<CardView> cardViews;
    List<ConstraintLayout> lays;
    LocalBroadcastManager localBroadcastManager;

    Context mContext;

    public RecyclerViewMastersChooseAdapter(Context mContext, ArrayList<Person> personList)
    {
        this.mContext = mContext;
        this.personList = personList;
        cardViews = new ArrayList<>();
        lays = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
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

        if(!cardViews.contains(holder.card)) {
            lays.add(holder.lay);
            cardViews.add(holder.card);
        }

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void OnItemSelectedListener(View view, int position) {
                for(CardView card:cardViews)
                    card.setCardBackgroundColor(mContext.getResources().getColor(R.color.GreyForCard));

                for(ConstraintLayout card:lays)
                    card.setBackgroundColor(mContext.getResources().getColor(R.color.GreyForCard));

                holder.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorDone));
                holder.lay.setBackgroundColor(mContext.getResources().getColor(R.color.colorDone));


                Intent intent = new Intent(Common.KEY_NEXT_BTN);
                intent.putExtra(Common.KEY_BARBER_SELECTED, personList.get(position));
                intent.putExtra(Common.KEY_STEP, 3);
                localBroadcastManager.sendBroadcast(intent);
            }
        });

        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

//        holder.info.setOnClickListener(new View.OnClickListener() {
//        });
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView card;
        TextView name_surname, offer, score;
        ImageView img, info;
        ConstraintLayout lay;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.cardview_master_foruser);
            name_surname = itemView.findViewById(R.id.textV_name_sur);
            offer = itemView.findViewById(R.id.text_offers);
            score = itemView.findViewById(R.id.text_score);
            img = itemView.findViewById(R.id.image_photo);
            lay = itemView.findViewById(R.id.rel);
            info = itemView.findViewById(R.id.image_choose);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.OnItemSelectedListener(v, getAdapterPosition());
        }
    }
}

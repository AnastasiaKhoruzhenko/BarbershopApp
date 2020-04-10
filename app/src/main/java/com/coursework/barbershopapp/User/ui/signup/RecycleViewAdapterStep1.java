package com.coursework.barbershopapp.User.ui.signup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursework.barbershopapp.Interface.IRecyclerItemSelectedListener;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Banner;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.TranslitClass;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewAdapterStep1 extends RecyclerView.Adapter<RecycleViewAdapterStep1.ViewHolder> {

    private List<Banner> listService;
    private Context mContext;
    private LocalBroadcastManager localBroadcastManager;
    private List<CardView> listCard;
    private List<ConstraintLayout> lays;

    public RecycleViewAdapterStep1(Context mContext, List<Banner> listService) {
        this.listService = listService;
        this.mContext = mContext;
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        listCard = new ArrayList<>();
        lays = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_service_step1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences prefs = mContext.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "ru");

        if(language.equals("ru"))
            holder.text.setText(listService.get(position).getText());
        else
            holder.text.setText(listService.get(position).getTextEN());

        if(!listCard.contains(holder.step1)) {
            listCard.add(holder.step1);
            lays.add(holder.lay);
        }

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void OnItemSelectedListener(View view, int position) {
                for(CardView card:listCard)
                    card.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorLightBrown));
                for(ConstraintLayout lay:lays)
                    lay.setBackgroundColor(mContext.getResources().getColor(R.color.colorLightBrown));

                holder.step1.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorBrown));
                holder.lay.setBackgroundColor(mContext.getResources().getColor(R.color.colorBrown));

                Intent intent = new Intent(Common.KEY_NEXT_BTN);
                intent.putExtra(Common.KEY_SERVICE_STORE, listService.get(position));
                intent.putExtra(Common.KEY_STEP, 1);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listService.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView step1;
        TextView text;
        ImageView img;
        ConstraintLayout lay;
        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            step1 = itemView.findViewById(R.id.cardview_step1);
            text = itemView.findViewById(R.id.tv_name_step1);
            img = itemView.findViewById(R.id.img_step1);
            lay = itemView.findViewById(R.id.const_lay_step1);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.OnItemSelectedListener(v, getAdapterPosition());
        }
    }
}

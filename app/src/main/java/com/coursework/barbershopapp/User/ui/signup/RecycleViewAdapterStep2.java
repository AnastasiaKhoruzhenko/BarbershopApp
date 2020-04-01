package com.coursework.barbershopapp.User.ui.signup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.coursework.barbershopapp.Interface.IRecyclerItemSelectedListener;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.AboutService;
import com.coursework.barbershopapp.model.Common;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewAdapterStep2 extends RecyclerView.Adapter<RecycleViewAdapterStep2.ViewHolder>{

    private List<AboutService> listServices = new ArrayList<>();
    private Context mContext;
    List<CardView> cardViews;
    List<ConstraintLayout> lays;
    LocalBroadcastManager localBroadcastManager;

    public RecycleViewAdapterStep2(Context mContext, List<AboutService> listServices) {
        this.listServices = listServices;
        this.mContext = mContext;
        cardViews = new ArrayList<>();
        lays = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_service, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.s_name.setText(listServices.get(position).getTitle());
        holder.s_price.setText(listServices.get(position).getPrice() + " " + " RUB");
        holder.s_descr.setText(listServices.get(position).getDescr());
        holder.s_time.setText(listServices.get(position).getTime() + " мин");

        if(!cardViews.contains(holder.step2)) {
            lays.add(holder.lay);
            cardViews.add(holder.step2);
        }

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void OnItemSelectedListener(View view, int position) {
                for(CardView card:cardViews) {
                    card.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                }
                for(ConstraintLayout lay:lays) {

                    lay.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                }

                holder.step2.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorLightBrown));
                holder.lay.setBackgroundColor(mContext.getResources().getColor(R.color.colorLightBrown));


                Intent intent = new Intent(Common.KEY_NEXT_BTN);
                intent.putExtra(Common.KEY_SERVICE_SELECTED, listServices.get(position));
                intent.putExtra(Common.KEY_STEP, 2);
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView step2;
        TextView s_name, s_descr, s_price, s_time;
        CheckBox check;
        View divider_service;
        ConstraintLayout lay;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            step2 = itemView.findViewById(R.id.cardview_service);
            s_name = itemView.findViewById(R.id.tx_serviceName);
            s_descr = itemView.findViewById(R.id.tw_serviceDescription);
            s_price = itemView.findViewById(R.id.tv_price);
            check  =itemView.findViewById(R.id.checkBox_choose);
            s_time = itemView.findViewById(R.id.tv_cardserv_time);
            divider_service = itemView.findViewById(R.id.divider_service);
            lay = itemView.findViewById(R.id.constr_card_serv);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.OnItemSelectedListener(v, getAdapterPosition());
        }
    }
}

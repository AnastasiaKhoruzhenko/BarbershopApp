package com.coursework.barbershopapp.User.ui.signup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coursework.barbershopapp.Interface.IRecyclerItemSelectedListener;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.TimeSlot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewTimeSlotsAdapter extends RecyclerView.Adapter<RecyclerViewTimeSlotsAdapter.MyViewHolder> {

    Context mContext;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViews;
    LocalBroadcastManager localBroadcastManager;

    public RecyclerViewTimeSlotsAdapter(Context mContext, List<TimeSlot> timeSlotList) {
        this.mContext = mContext;
        this.timeSlotList = timeSlotList;
        cardViews = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
    }

    public RecyclerViewTimeSlotsAdapter(Context mContext) {
        this.mContext = mContext;
        this.timeSlotList = new ArrayList<>();
        cardViews = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(mContext).inflate(R.layout.time_slot, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.time.setText(Common.convertTimeSlotToString(position));
        holder.cardViewTime.setEnabled(true);

        if(timeSlotList.size() == 0) // all are availiable
        {
            holder.cardViewTime.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorDone));
            holder.time.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
        }
        else {
            for (TimeSlot timeSlot : timeSlotList)
            {
                int slot = Integer.parseInt(timeSlot.getSlot().toString());
                if(slot == position)
                {
                    holder.cardViewTime.setTag(Common.DISABLE_TAG);
                    holder.cardViewTime.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                    holder.time.setTextColor(mContext.getResources().getColor(R.color.colorGrey));
                    holder.cardViewTime.setEnabled(false);
                }
            }
        }

        // add all cards
        if(!cardViews.contains(holder.cardViewTime))
            cardViews.add(holder.cardViewTime);

        holder.setiRecyclerItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void OnItemSelectedListener(View view, int position) {
                for(CardView card : cardViews)
                {
                    if(card.getTag() == null)
                        card.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorDone));
                }

                // selected card
                holder.cardViewTime.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryDark));

                Intent intent = new Intent(Common.KEY_NEXT_BTN);
                intent.putExtra(Common.KEY_TIME_SLOT, position);
                intent.putExtra(Common.KEY_STEP, 4);
                localBroadcastManager.sendBroadcast(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return Common.SLOT_COUNT;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cardViewTime;
        TextView time;

        IRecyclerItemSelectedListener iRecyclerItemSelectedListener;

        public void setiRecyclerItemSelectedListener(IRecyclerItemSelectedListener iRecyclerItemSelectedListener) {
            this.iRecyclerItemSelectedListener = iRecyclerItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardViewTime = itemView.findViewById(R.id.cardview_choose_time);
            time = itemView.findViewById(R.id.tv_choose_time);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecyclerItemSelectedListener.OnItemSelectedListener(v, getAdapterPosition());
        }
    }
}

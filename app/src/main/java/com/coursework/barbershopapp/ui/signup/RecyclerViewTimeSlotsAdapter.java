package com.coursework.barbershopapp.ui.signup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.TimeSlot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewTimeSlotsAdapter extends RecyclerView.Adapter<RecyclerViewTimeSlotsAdapter.MyViewHolder> {

    Context mContext;
    List<TimeSlot> timeSlotList;

    public RecyclerViewTimeSlotsAdapter(Context mContext, List<TimeSlot> timeSlotList) {
        this.mContext = mContext;
        this.timeSlotList = timeSlotList;
    }

    public RecyclerViewTimeSlotsAdapter(Context mContext) {
        this.mContext = mContext;
        this.timeSlotList = new ArrayList<>();
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

        if(timeSlotList.size() == 0) // all are availiable
        {
            holder.cardViewTime.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorDone));
            holder.time.setTextColor(mContext.getResources().getColor(R.color.colorLightBrown));
        }
        else {
            for (TimeSlot timeSlot : timeSlotList)
            {
                int slot = Integer.parseInt(timeSlot.getSlot().toString());
                if(slot == position)
                {
                    holder.cardViewTime.setCardBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                    holder.time.setTextColor(mContext.getResources().getColor(R.color.colorGrey));
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return Common.SLOT_COUNT;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        CardView cardViewTime;
        TextView time;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            cardViewTime = itemView.findViewById(R.id.cardview_choose_time);
            time = itemView.findViewById(R.id.tv_choose_time);
        }
    }
}

package com.coursework.barbershopapp.ui.signup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Banner;
import com.google.android.gms.common.internal.service.Common;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewAdapterStep1 extends RecyclerView.Adapter<RecycleViewAdapterStep1.ViewHolder> {

    private List<Banner> listService;
    private Context mContext;

    public RecycleViewAdapterStep1(Context mContext, List<Banner> listService) {
        this.listService = listService;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_service_step1, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(mContext)
                .asBitmap()
                .load(listService.get(position).getImg())
                .into(holder.img);
        holder.text.setText(listService.get(position).getText());

        holder.step1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return listService.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        CardView step1;
        TextView text;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            step1 = itemView.findViewById(R.id.cardview_step1);
            text = itemView.findViewById(R.id.tv_name_step1);
            img = itemView.findViewById(R.id.img_step1);
        }
    }
}

package com.coursework.barbershopapp.Masters.ui.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterMasterSett extends RecyclerView.Adapter<RecyclerViewAdapterMasterSett.ViewHolder> {

    private List<String> arrSettings;
    private Context mContext;

    public RecyclerViewAdapterMasterSett(Context mContext, List<String> arrSettings) {
        this.arrSettings = arrSettings;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_profile_settings, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setting.setText(arrSettings.get(position));
//        holder.card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return arrSettings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView setting;
        ImageView imgNext;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.cardview_profile_sett);
            //imgNext = itemView.findViewById(R.id.iv_next);
            setting = itemView.findViewById(R.id.tv_setting);
        }
    }
}

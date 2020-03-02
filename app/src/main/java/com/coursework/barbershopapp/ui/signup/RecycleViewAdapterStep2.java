package com.coursework.barbershopapp.ui.signup;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.AboutService;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecycleViewAdapterStep2 extends RecyclerView.Adapter<RecycleViewAdapterStep2.ViewHolder>{

    private List<AboutService> listServices = new ArrayList<>();
    private Context mContext;

    public RecycleViewAdapterStep2(Context mContext, List<AboutService> listServices) {
        this.listServices = listServices;
        this.mContext = mContext;
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
        //Toast.makeText(mContext, String.valueOf(listPrices.get(position)), Toast.LENGTH_LONG).show();
        holder.s_name.setText(listServices.get(position).getTitle());
        holder.s_price.setText(listServices.get(position).getPrice());
        holder.s_descr.setText(listServices.get(position).getDescr());
    }

    @Override
    public int getItemCount() {
        return listServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView step2;
        TextView s_name, s_descr, s_price;
        CheckBox check;
        View divider_service;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            step2 = itemView.findViewById(R.id.cardview_service);
            s_name = itemView.findViewById(R.id.tx_serviceName);
            s_descr = itemView.findViewById(R.id.tw_serviceDescription);
            s_price = itemView.findViewById(R.id.tv_price);
            check  =itemView.findViewById(R.id.checkBox_choose);
            divider_service = itemView.findViewById(R.id.divider_service);
        }
    }
}

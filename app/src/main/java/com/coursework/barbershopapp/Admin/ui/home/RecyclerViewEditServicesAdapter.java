package com.coursework.barbershopapp.Admin.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.RegistrationActivity;
import com.coursework.barbershopapp.model.AboutService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewEditServicesAdapter extends RecyclerView.Adapter<RecyclerViewEditServicesAdapter.ViewHolder>{

    private List<AboutService> listServices = new ArrayList<>();
    private Context mContext;
    List<CardView> cardViews;
    List<ConstraintLayout> lays;
    LocalBroadcastManager localBroadcastManager;
    FirebaseFirestore db;
    String serv;

    public RecyclerViewEditServicesAdapter(Context mContext, List<AboutService> listServices, String serv) {
        this.listServices = listServices;
        this.mContext = mContext;
        this.serv = serv;
        cardViews = new ArrayList<>();
        lays = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
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
        //Toast.makeText(mContext, String.valueOf(listPrices.get(position)), Toast.LENGTH_LONG).show();
        holder.s_name.setText(listServices.get(position).getTitle());
        holder.s_price.setText(listServices.get(position).getPrice());
        holder.s_descr.setText(listServices.get(position).getDescr());
        holder.s_time.setText(listServices.get(position).getTime() + " мин");

        holder.step2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(listServices.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView step2;
        TextView s_name, s_descr, s_price, s_time;
        CheckBox check;
        View divider_service;
        ConstraintLayout lay;

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
        }
    }

    private void showDialog(String id) {

        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_edit_service_info);
        dialog.setTitle("Настройки аккаунта");

        TextInputLayout pricel = dialog.findViewById(R.id.til_edit_price);
        TextInputLayout namel = dialog.findViewById(R.id.til_edit_service_name);
        TextInputLayout descl = dialog.findViewById(R.id.til_edit_service_desc);
        TextInputEditText price = dialog.findViewById(R.id.ti_edit_price);
        TextInputEditText name = dialog.findViewById(R.id.ti_edit_service_name);
        TextInputEditText desc = dialog.findViewById(R.id.ti_edit_service_desc);

//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        dialog.show();
    }
}

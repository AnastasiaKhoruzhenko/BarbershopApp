package com.coursework.barbershopapp.Admin.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toolbar;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.RegistrationActivity;
import com.coursework.barbershopapp.model.AboutService;
import com.coursework.barbershopapp.model.Master;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
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
    private List<CardView> cardViews;
    private List<ConstraintLayout> lays;
    private LocalBroadcastManager localBroadcastManager;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String serv;

    private ConstraintLayout constraintLayout;

    private Dialog dialog;

    public RecyclerViewEditServicesAdapter(Context mContext, List<AboutService> listServices, String serv) {
        this.listServices = listServices;
        this.mContext = mContext;
        this.serv = serv;
        cardViews = new ArrayList<>();
        lays = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        SharedPreferences prefs = mContext.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "ru");

        if(language.equals("ru"))
        {
            holder.s_name.setText(listServices.get(position).getTitle());
            holder.s_descr.setText(listServices.get(position).getDescr());
        }
        else
        {
            holder.s_name.setText(listServices.get(position).getTitleEN());
            holder.s_descr.setText(listServices.get(position).getDescrEN());
        }

        holder.s_price.setText(listServices.get(position).getPrice() + " RUB");
        holder.s_time.setText(listServices.get(position).getTime() + mContext.getResources().getString(R.string.min));

        holder.step2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditInfo(v, position);
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
        View divider_service;
        ConstraintLayout lay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            step2 = itemView.findViewById(R.id.cardview_service);
            s_name = itemView.findViewById(R.id.tx_serviceName);
            s_descr = itemView.findViewById(R.id.tw_serviceDescription);
            s_price = itemView.findViewById(R.id.tv_price);
            s_time = itemView.findViewById(R.id.tv_cardserv_time);
            lay = itemView.findViewById(R.id.constr_card_serv);
        }
    }

    private void showEditInfo(View view, int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.edit_service_info, null);
        dialog = new Dialog(mContext, R.style.AppTheme_FullScreenDialog);
        dialog.setContentView(v);
        Toolbar toolbar = (Toolbar)dialog.findViewById(R.id.toolbar_edit_close);

        EditText eName = dialog.findViewById(R.id.ti_admin_edit_name);
        EditText ePrice = dialog.findViewById(R.id.ti_admin_edit_price);
        EditText eTime = dialog.findViewById(R.id.ti_admin_edit_time);
        EditText eDescr = dialog.findViewById(R.id.ti_admin_edit_descr);
        EditText eDescrEN = dialog.findViewById(R.id.ti_admin_edit_descr_en);
        EditText eTitleEN = dialog.findViewById(R.id.ti_admin_edit_title_en);
        TextView close = dialog.findViewById(R.id.close_edit_img);
        Button save = dialog.findViewById(R.id.button);


        db.collection("ServicesMan").document(serv)
                .collection("Services").document(listServices.get(position).getId())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    AboutService service = task.getResult().toObject(AboutService.class);

                    eName.setText(service.getTitle());
                    ePrice.setText(service.getPrice());
                    eTime.setText(service.getTime());
                    eDescr.setText(service.getDescr());
                    eDescrEN.setText(service.getDescrEN());
                    eTitleEN.setText(service.getTitleEN());

                    constraintLayout = view.findViewById(R.id.constr_admin_edit_serv);
                    dialog.show();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<>();
                map.put("price", ePrice.getText().toString());
                map.put("time", eTime.getText().toString());
                map.put("title", eName.getText().toString());
                map.put("descr", eDescr.getText().toString());
                map.put("descrEN", eDescrEN.getText().toString());
                map.put("titleEN", eTitleEN.getText().toString());

                db.collection("ServicesMan").document(serv)
                        .collection("Services").document(listServices.get(position).getId())
                        .update(map);

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

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
import android.widget.RadioButton;
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

    private List<AboutService> listServices;
    private Context mContext;
    private FirebaseFirestore db;
    private String serv;
    private ConstraintLayout constraintLayout;
    private Dialog dialog;

    public RecyclerViewEditServicesAdapter(Context mContext, List<AboutService> listServices, String serv) {
        this.listServices = listServices;
        this.mContext = mContext;
        this.serv = serv;
        List<CardView> cardViews = new ArrayList<>();
        List<ConstraintLayout> lays = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.radioButton.setVisibility(View.GONE);

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
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            step2 = itemView.findViewById(R.id.cardview_service);
            s_name = itemView.findViewById(R.id.tx_serviceName);
            s_descr = itemView.findViewById(R.id.tw_serviceDescription);
            s_price = itemView.findViewById(R.id.tv_price);
            s_time = itemView.findViewById(R.id.tv_cardserv_time);
            lay = itemView.findViewById(R.id.constr_card_serv);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }

    private void showEditInfo(View view, int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.edit_service_info, null);
        dialog = new Dialog(mContext, R.style.AppTheme_FullScreenDialog);
        dialog.setContentView(v);
        Toolbar toolbar = (Toolbar)dialog.findViewById(R.id.toolbar_edit_close);
        TextInputLayout lTitle = dialog.findViewById(R.id.til_admin_edit_name);
        TextInputLayout lTitleEN = dialog.findViewById(R.id.til_admin_edit_title_en);
        TextInputLayout lDescr = dialog.findViewById(R.id.til_admin_edit_descr);
        TextInputLayout lDescrEN = dialog.findViewById(R.id.til_admin_edit_descr_en);
        TextInputLayout lPrice = dialog.findViewById(R.id.til_admin_edit_price);
        TextInputLayout lTime = dialog.findViewById(R.id.til_admin_edit_time);
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
                validateTitle(lTitle, eName);
                validateTitleEN(lTitleEN, eTitleEN);
                validateDescr(lDescr, eDescr);
                validateDescrEN(lDescrEN, eDescrEN);
                validatePrice(lPrice, ePrice);
                validateTime(lTime, eTime);

                if(validateTitle(lTitle, eName) && validateTitleEN(lTitleEN, eTitleEN)
                && validateDescr(lDescr, eDescr) && validateDescrEN(lDescrEN, eDescrEN)
                && validatePrice(lPrice, ePrice) && validateTime(lTime, eTime))
                {
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

                    listServices.get(position).setTitle(eName.getText().toString());
                    listServices.get(position).setTitleEN(eTitleEN.getText().toString());
                    listServices.get(position).setDescr(eDescr.getText().toString());
                    listServices.get(position).setDescrEN(eDescrEN.getText().toString());
                    listServices.get(position).setTime(eTime.getText().toString());
                    listServices.get(position).setPrice(ePrice.getText().toString());
                    notifyItemChanged(position);

                    dialog.dismiss();
                }
//                else{
//                    if(!validateTitle(lTitle, eName))
//                        lTitle.setError(mContext.getResources().getString(R.string.not_filled));
//                    if(validateTitleEN(lTitleEN, eTitleEN))
//                        lTitleEN.setError(mContext.getResources().getString(R.string.not_filled));
//                    if(validateDescr(lDescr, eDescr))
//                        lDescr.setError(mContext.getResources().getString(R.string.not_filled));
//                    if(validateDescrEN(lDescrEN, eDescrEN))
//                        lDescrEN.setError(mContext.getResources().getString(R.string.not_filled));
//                    if(validatePrice(lPrice, ePrice))
//                        lPrice.setError(mContext.getResources().getString(R.string.not_filled));
//                    if(validateTime(lTime, eTime))
//                        lTime.setError(mContext.getResources().getString(R.string.not_filled));
//                }
            }
        });

        dialog.show();
    }

    private boolean validateTitle(TextInputLayout lTitle, EditText eTitle){
        if(eTitle.getText().toString().isEmpty()) {
            lTitle.setError(mContext.getResources().getString(R.string.not_filled));
            return false;
        }
        lTitle.setError(null);
        return true;
    }

    private boolean validateTitleEN(TextInputLayout lTitleEN, EditText eTitleEN){
        if(eTitleEN.getText().toString().isEmpty()) {
            lTitleEN.setError(mContext.getResources().getString(R.string.not_filled));
            return false;
        }
        lTitleEN.setError(null);
        return true;
    }

    private boolean validateDescr(TextInputLayout lDescr, EditText eDescr){
        if(eDescr.getText().toString().isEmpty()) {
            lDescr.setError(mContext.getResources().getString(R.string.not_filled));
            return false;
        }
        lDescr.setError(null);
        return true;
    }

    private boolean validateDescrEN(TextInputLayout lDescrEN, EditText eDescrEN){
        if(eDescrEN.getText().toString().isEmpty()) {
            lDescrEN.setError(mContext.getResources().getString(R.string.not_filled));
            return false;
        }
        lDescrEN.setError(null);
        return true;
    }

    private boolean validatePrice(TextInputLayout lPrice, EditText ePrice){
        if(ePrice.getText().toString().isEmpty()) {
            lPrice.setError(mContext.getResources().getString(R.string.not_filled));
            return false;
        }
        lPrice.setError(null);
        return true;
    }

    private boolean validateTime(TextInputLayout lTime, EditText eTime){
        if(eTime.getText().toString().isEmpty()) {
            lTime.setError(mContext.getResources().getString(R.string.not_filled));
            return false;
        }
        lTime.setError(null);
        return true;
    }
}

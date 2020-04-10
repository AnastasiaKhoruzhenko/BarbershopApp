package com.coursework.barbershopapp.Admin.ui.info;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.coursework.barbershopapp.Admin.ui.home.SalonInfoActivity;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.User.ui.settings.RecyclerViewSettingsAdapter;
import com.github.angads25.toggle.widget.LabeledSwitch;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewSettingsAdmin extends RecyclerView.Adapter<RecyclerViewSettingsAdmin.ViewHolder>{

    private List<String> listName, listDescr;
    private Context mContext;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    Dialog dialog;

    public RecyclerViewSettingsAdmin(Context mContext, List<String> listName, List<String> listDescr) {
        this.listName = listName;
        this.listDescr = listDescr;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        db = FirebaseFirestore.getInstance();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_profile_settings, parent, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_namesett.setText(listName.get(position));
        holder.tv_descr.setText(listDescr.get(position));

        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position){
                    case 0:
                        salonInfo();
                        break;
                    case 1:
                        appSetting();
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CardView setting;
        TextView tv_namesett, tv_descr;
        View div;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            setting = itemView.findViewById(R.id.cardview_profile_sett);
            tv_namesett = itemView.findViewById(R.id.tv_setting);
            tv_descr = itemView.findViewById(R.id.tv_descr_profile);
            div = itemView.findViewById(R.id.divider_sett);
        }
    }

    public void salonInfo(){

        View v = LayoutInflater.from(mContext).inflate(R.layout.salon_info_activity, null);
        dialog = new Dialog(mContext, R.style.AppTheme_FullScreenDialog);
        dialog.setContentView(v);

        MapView map = dialog.findViewById(R.id.mapView);
        EditText address = dialog.findViewById(R.id.et_address_admin);
        EditText info = dialog.findViewById(R.id.editText3);
        //Button ok = dialog.findViewById(R.id.btn_alert_ok);

        address.setClickable(false);
        info.setClickable(false);

        db.collection("SalonInfo").document("information")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                address.setText(task.getResult().getString("address"));
                info.setText(task.getResult().getString("definition"));
            }
        });

        dialog.show();
    }

    public void appSetting(){
        LoadLocale();
        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_settings_select, null);
        dialog = new Dialog(mContext, R.style.AppTheme_FullScreenDialog);
        dialog.setContentView(v);

        LabeledSwitch push = dialog.findViewById(R.id.switch_push);
        LabeledSwitch lang = dialog.findViewById(R.id.switch_language);
        Button save = dialog.findViewById(R.id.btn_save_settapp);

        lang.setLabelOff("RU");
        lang.setLabelOn("EN");

        SharedPreferences prefs = mContext.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "ru");
        if(!language.equals("ru"))
            lang.setOn(false);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lang.isOn())
                    setLocale("en");
                else
                    setLocale("ru");

                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config, mContext.getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = mContext.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_lang", lang);
        editor.apply();
    }

    public void LoadLocale(){
        SharedPreferences prefs = mContext.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "ru");
        setLocale(language);
    }
}

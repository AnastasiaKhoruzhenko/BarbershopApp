package com.coursework.barbershopapp.Masters.ui.settings;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.AboutService;
import com.coursework.barbershopapp.model.Banner;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.Master;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.value.ReferenceValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterMasterSett extends RecyclerView.Adapter<RecyclerViewAdapterMasterSett.ViewHolder> {

    private List<String> arrSettings;
    private Context mContext;
    Dialog dialog;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    ConstraintLayout constraintLayout;

    public RecyclerViewAdapterMasterSett(Context mContext, List<String> arrSettings) {
        this.arrSettings = arrSettings;
        this.mContext = mContext;
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_profile_settings, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_namesett.setText(arrSettings.get(position));
        holder.tv_descr.setText(arrSettings.get(position));

        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position)
                {
                    case 0:
                        //showPersonalInfo();
                        break;
                    case 1:
                        //showWorkTime();
                        break;
                    case 2:
                        showServices(v, position);
                        break;
                    case 3:
                        //showAppSettings();
                        break;

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrSettings.size();
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

    private void showServices(View view, int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.full_master_choose_service, null);
        dialog = new Dialog(mContext, R.style.AppTheme_FullScreenDialog);
        dialog.setContentView(v);
        Toolbar toolbar = (Toolbar)dialog.findViewById(R.id.toolbar_close);
        TextView close = dialog.findViewById(R.id.close_img);
        CheckBox ch1 = dialog.findViewById(R.id.checkBox);
        CheckBox ch2 = dialog.findViewById(R.id.checkBox2);
        CheckBox ch3 = dialog.findViewById(R.id.checkBox3);
        CheckBox ch4 = dialog.findViewById(R.id.checkBox4);
        CheckBox ch5 = dialog.findViewById(R.id.checkBox5);
        CheckBox ch6 = dialog.findViewById(R.id.checkBox6);

        ch1.setText("Стрижка");
        ch2.setText("Покраска");
        ch3.setText("Barber-SPA");
        ch4.setText("Оформление бороды и усов");
        ch5.setText("Нанесение тату");
        ch6.setText("Комплексные услуги");

        db.collection("Masters").document(mAuth.getCurrentUser().getEmail())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    Master master = task.getResult().toObject(Master.class);
                    List<String> arr = master.getServices();
                    for(String serv : arr)
                    {
                        switch (serv){
                            case "Barber-SPA":
                                ch3.setChecked(true);break;
                            case "HairCut":
                                ch1.setChecked(true);break;
                            case "Coloring":
                                ch2.setChecked(true);break;
                            case "BeardAndMustacheCut":
                                ch4.setChecked(true);break;
                            case "Tatoo":
                                ch5.setChecked(true);break;
                            case "CombineService":
                                ch6.setChecked(true);break;
                        }
                    }

                    ch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            addServiceToBarber("Barber-SPA", isChecked);
                        }
                    });
                    ch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            addServiceToBarber("HairCut", isChecked);
                        }
                    });
                    ch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            addServiceToBarber("Coloring", isChecked);
                        }
                    });
                    ch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            addServiceToBarber("BeardAndMustacheCut", isChecked);
                        }
                    });
                    ch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            addServiceToBarber("Tatoo", isChecked);
                        }
                    });
                    ch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            addServiceToBarber("CombineService", isChecked);
                        }
                    });

                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            List<String> list = new ArrayList<>();
                            if(ch1.isChecked())
                                list.add("HairCut");
                            if(ch2.isChecked())
                                list.add("Coloring");
                            if(ch3.isChecked())
                                list.add("Barber-SPA");
                            if(ch4.isChecked())
                                list.add("BeardAndMustacheCut");
                            if(ch5.isChecked())
                                list.add("Tatoo");
                            if(ch6.isChecked())
                                list.add("CombineService");

                            db.collection("Masters")
                                    .document(mAuth.getCurrentUser().getEmail())
                                    .update("services", list);

                            dialog.dismiss();
                        }
                    });
                    constraintLayout = view.findViewById(R.id.constr_master_comm);
                    dialog.show();
                }
            }
        });
    }

    private void addServiceToBarber(String s, boolean flag) {
        Map<String, Object> map = new HashMap<>();
        map.put("barber", db.collection("Masters").document(mAuth.getCurrentUser().getEmail()));

        if(flag)
            db.collection("ServicesMan").document(s)
                .collection("Barbers").document(mAuth.getCurrentUser().getEmail()).set(map);
        else
            db.collection("ServicesMan").document(s)
                .collection("Barbers").document(mAuth.getCurrentUser().getEmail()).delete();
    }
}

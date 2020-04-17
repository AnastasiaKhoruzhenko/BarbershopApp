package com.coursework.barbershopapp.Masters.ui.settings;

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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.User.ui.settings.SettingsSelectActivity;
import com.coursework.barbershopapp.model.AboutService;
import com.coursework.barbershopapp.model.Banner;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.MaskWatcherBirthDate;
import com.coursework.barbershopapp.model.MaskWatcherPhone;
import com.coursework.barbershopapp.model.Master;
import com.coursework.barbershopapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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

    private List<String> arrSettings, arrDescrSettings;
    private Context mContext;
    private Dialog dialog;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ConstraintLayout constraintLayout;

    public RecyclerViewAdapterMasterSett(Context mContext, List<String> arrSettings, List<String> arrDescrSettings) {
        this.arrSettings = arrSettings;
        this.arrDescrSettings = arrDescrSettings;
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
        holder.tv_descr.setText(arrDescrSettings.get(position));

        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position)
                {
                    case 0:
                        showPersonalInfo();
                        break;
                    case 1:
                        showServices(v, position);
                        break;
                    case 2:
                        showAppSettings();
                        break;
                    case 3:
                        showExitDialog();
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
                    if(arr != null)
                        for(String serv : arr)
                        {
                            switch (serv){
                                case "BarberSPA":
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
                            addServiceToBarber("BarberSPA", isChecked);
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
                                list.add("BarberSPA");
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

    public void showPersonalInfo(){
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_settings_account);
        dialog.setTitle(mContext.getResources().getString(R.string.account_settings));
        TextInputLayout surname1 = dialog.findViewById(R.id.til_surname_sett);
        TextInputLayout name1 = dialog.findViewById(R.id.til_name_sett);
        TextInputLayout email1 = dialog.findViewById(R.id.til_email_sett);
        TextInputLayout birth1 = dialog.findViewById(R.id.til_birth_sett);
        TextInputLayout phone1 = dialog.findViewById(R.id.til_phone_sett);
        EditText surname = dialog.findViewById(R.id.ti_surname_sett);
        EditText name = dialog.findViewById(R.id.ti_name_sett);
        EditText email = dialog.findViewById(R.id.ti_email_sett);
        EditText birth = dialog.findViewById(R.id.ti_birth_sett);
        EditText phone = dialog.findViewById(R.id.ti_phone_sett);
        Button ok = dialog.findViewById(R.id.btn_alert_ok);

        birth1.setHelperText(mContext.getResources().getString(R.string.not_change_datebirth));
        phone.addTextChangedListener(new MaskWatcherPhone("#(###)###-##-##"));
        birth.addTextChangedListener(new MaskWatcherBirthDate("##.##.####"));

        db.collection("Masters").document(mAuth.getCurrentUser().getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Master user = task.getResult().toObject(Master.class);
                            surname.setText(user.getSurname());
                            name.setText(user.getName());
                            birth.setText(user.getBirth());
                            phone.setText(user.getPhone());
                            email.setText(user.getEmail());
                        }
                    }
                });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(surname.getText().equals("") || name.getText().equals("") || phone.getText().equals("")){
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.set_all_fields), Toast.LENGTH_SHORT).show();
                }
                else if(phone.getText().toString().length() != 15)
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.numbers_11), Toast.LENGTH_LONG).show();
                else if (birth.getText().toString().length() != 10
                        || Integer.valueOf(birth.getText().toString().substring(0,2)) > 31
                        || Integer.valueOf(birth.getText().toString().substring(0,2)) < 1
                        || Integer.valueOf(birth.getText().toString().substring(3,5)) < 1
                        || Integer.valueOf(birth.getText().toString().substring(3,5)) > 12
                        || Integer.valueOf(birth.getText().toString().substring(6,10)) > 2020
                        || Integer.valueOf(birth.getText().toString().substring(6,10)) < 1920)
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.incorrect_date_of_birth), Toast.LENGTH_SHORT).show();
                else{
                    Map<String, Object> data = new HashMap<>();
                    data.put("surname", surname.getText().toString());
                    data.put("name", name.getText().toString());
                    data.put("phone", phone.getText().toString());
                    data.put("birth", birth.getText().toString());
                    db.collection("Masters").document(mAuth.getCurrentUser().getEmail()).update(data);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void showAppSettings(){
        Intent intent = new Intent(mContext, SettingsSelectActivity.class);
        mContext.startActivity(intent);
    }

    private void showExitDialog()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setMessage(mContext.getResources().getString(R.string.want_to_exit));
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, mContext.getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, mContext.getResources().getString(R.string.exit),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                    }
                });
        alertDialog.show();
    }
}

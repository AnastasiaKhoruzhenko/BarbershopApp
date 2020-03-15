package com.coursework.barbershopapp.ui.settings;

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
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.RegistrationActivity;
import com.coursework.barbershopapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewSettingsAdapter extends RecyclerView.Adapter<RecyclerViewSettingsAdapter.ViewHolder>{

    List<String> listName, listDescr;
    Context mContext;
    private LocalBroadcastManager localBroadcastManager;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;

    public RecyclerViewSettingsAdapter(Context mContext, List<String> listName, List<String> listDescr) {
        this.listName = listName;
        this.listDescr = listDescr;
        this.mContext = mContext;
        localBroadcastManager = LocalBroadcastManager.getInstance(mContext);
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
                switch (position)
                {
                    case 0:
                        if(user == null)
                            showRegisterDialog();
                        else
                            showInfoDialog();
                        break;
                    case 1:
                        showAppDialog();
                        break;
                    case 2:
                        showInviteDialog();
                        break;
                    case 3:
                        showLoyaltyDialog();
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

    private void showInfoDialog() {

        //final Dialog dialog = new Dialog(mContext, R.style.DialogTheme);
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_settings_account);
        dialog.setTitle("Настройки аккаунта");
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

        db.collection("Users").document(user.getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            User user = task.getResult().toObject(User.class);
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
                    Toast.makeText(mContext, "Заполните все поля", Toast.LENGTH_SHORT).show();
                }
                else{
                    Map<String, Object> data = new HashMap<>();
                    data.put("surname", surname.getText().toString());
                    data.put("name", name.getText().toString());
                    data.put("phone", phone.getText().toString());
                    db.collection("Users").document(mAuth.getCurrentUser().getEmail()).update(data);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    private void showAppDialog() {
        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_settings_app);
        dialog.setTitle("Настройки аккаунта");

        Switch push = dialog.findViewById(R.id.switch_push);
        Switch theme = dialog.findViewById(R.id.switch_theme);
        Switch lan = dialog.findViewById(R.id.switch_language);
        Button save = dialog.findViewById(R.id.btn_save_settapp);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showInviteDialog() {
    }

    private void showLoyaltyDialog() {
    }

    private void showRegisterDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
        //alertDialog.setTitle("Alert");
        alertDialog.setMessage("Редактирование доступно только после регитсрации");
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Не сейчас",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Зарегистрироваться",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, RegistrationActivity.class);
                        mContext.startActivity(intent);
                    }
                });
        alertDialog.show();
    }
}

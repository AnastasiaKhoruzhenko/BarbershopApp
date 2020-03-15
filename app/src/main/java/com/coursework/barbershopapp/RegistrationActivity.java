package com.coursework.barbershopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coursework.barbershopapp.model.MaskWatcherBirthDate;
import com.coursework.barbershopapp.model.MaskWatcherPhone;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText inp_log, inp_pass, inp_name, inp_surname, inp_conf_pass;
    EditText inp_phone, inp_birthdate;
    private TextInputLayout lay_log, lay_pass, lay_name, lay_surname, lay_conf_pass, lay_phone, lay_birthdate;
    private Button btn_reg;

    SharedPreferences mPreferences;
    SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPreferences.edit();

        inp_log=findViewById(R.id.inputReg_log);
        inp_pass=findViewById(R.id.inputReg_pass);
        inp_name=findViewById(R.id.inputReg_name);
        inp_surname = findViewById(R.id.inputReg_surname);
        inp_phone = (EditText)findViewById(R.id.ti_phone_reg);
        inp_conf_pass = findViewById(R.id.ti_conf_password);
        inp_birthdate = (EditText) findViewById(R.id.ti_birthdate_reg);
        lay_log=findViewById(R.id.layReg_log);
        lay_pass=findViewById(R.id.layreg_pass);
        lay_name = findViewById(R.id.layReg_name);
        lay_surname = findViewById(R.id.layReg_surname);
        lay_phone = findViewById(R.id.til_phone_reg);
        lay_conf_pass = findViewById(R.id.til_conf_password);
        lay_birthdate = findViewById(R.id.til_birthdate_reg);
        btn_reg=findViewById(R.id.reg_btn);

        inp_phone.addTextChangedListener(new MaskWatcherPhone("#(###)###-##-##"));
        inp_birthdate.addTextChangedListener(new MaskWatcherBirthDate("##.##.####"));

        mAuth = FirebaseAuth.getInstance();

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inp_name.getText().toString();
                String surname = inp_surname.getText().toString();
                String email = inp_log.getText().toString();
                String password = inp_pass.getText().toString();
                String confPass = inp_conf_pass.getText().toString();
                String phone = inp_phone.getText().toString();
                String birth = inp_birthdate.getText().toString();

                if(email.isEmpty() || password.isEmpty() || phone.isEmpty() || confPass.isEmpty()
                        || surname.isEmpty() || birth.isEmpty()){
                    showMessage("Заполните все поля!");
                }
                else if(!password.equals(confPass))
                    showMessage("Пароли не совпадают!");
                else if(password.length() < 8)
                    showMessage("Пароль должен содержать не менее 8 символов");
                else
                {
                    createUserAccount(email, name, surname, phone, birth, password);
                    mEditor.putString("email", email);
                    mEditor.commit();
                }
            }
        });
    }

    private void createUserAccount(final String email, final String name, final String surname, final String phone, final String birth, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    showMessage("Аккаунт успешно создан");
                    updateInfo(email, name, surname, phone, birth, mAuth.getCurrentUser());

                }
                else
                {
                    showMessage("Пользователь с такой почтой уже существует");
                }
            }
        });
    }

    private void updateInfo(String email, String name, String surname, String phone, String birth, FirebaseUser currentUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user=new HashMap<>();
        user.put("email", email);
        user.put("name", name);
        user.put("phone", phone);
        user.put("surname", surname);
        user.put("birth", birth);
        db.collection("Users").document(email).set(user);

        Toast.makeText(getApplicationContext(), mPreferences.getString("email", "default")
        , Toast.LENGTH_LONG).show();

        this.finish();

//        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
//                .setDisplayName(name).build();
//        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//
//                //user info updated successfully
//                //updateUI(type);
//
//            }
//        });

    }

    private void showMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}

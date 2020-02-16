package com.coursework.barbershopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
    private TextInputEditText inp_log, inp_pass, inp_name, inp_surname;
    private TextInputLayout lay_log, lay_pass, lay_name, lay_surname;
    private Button btn_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        inp_log=(TextInputEditText)findViewById(R.id.inputReg_log);
        inp_pass=(TextInputEditText)findViewById(R.id.inputReg_pass);
        inp_name=(TextInputEditText)findViewById(R.id.inputReg_name);
        inp_surname = (TextInputEditText)findViewById(R.id.inputReg_surname);
        lay_log=(TextInputLayout)findViewById(R.id.layReg_log);
        lay_pass=(TextInputLayout)findViewById(R.id.layreg_pass);
        lay_name = (TextInputLayout)findViewById(R.id.layReg_name);
        lay_surname = (TextInputLayout)findViewById(R.id.layReg_surname);
        btn_reg=(Button)findViewById(R.id.reg_btn);

        mAuth = FirebaseAuth.getInstance();

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inp_name.getText().toString();
                String surname = inp_surname.getText().toString();
                String email = inp_log.getText().toString();
                String password = inp_pass.getText().toString();

                if(email.isEmpty() || password.isEmpty()){
                    showMessage("Заполните все поля!");
                }
                else
                {
                    createUserAccount(email, name, surname, password);
                }
            }
        });
    }

    private void createUserAccount(final String email, final String name, final String surname, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    showMessage("Аккаунт успешно создан");
                    updateInfo(email, name, surname, mAuth.getCurrentUser());
                }
                else
                {
                    showMessage("Пользователь с такой почтой уже существует");
                }
            }
        });
    }

    private void updateInfo(String email, String name, String surname, FirebaseUser currentUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user=new HashMap<>();
        user.put("Email", email);
        user.put("Name", name);
        user.put("Surname", surname);
        db.collection("Users").document(email).set(user);

        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                .setDisplayName(name).build();
        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                //user info updated successfully
                //updateUI(type);

            }
        });

    }

    private void showMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}

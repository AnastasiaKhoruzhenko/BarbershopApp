package com.coursework.barbershopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coursework.barbershopapp.model.Master;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout1, textInputLayout2;
    private EditText log, pass;
    private Button login_btn;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadLocale();
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        textInputLayout1 = (TextInputLayout)findViewById(R.id.textInputLayout_log);
        textInputLayout2 = (TextInputLayout)findViewById(R.id.textInputField_pass);
        login_btn = (Button)findViewById(R.id.button_login);
        log = findViewById(R.id.log_text);
        pass = findViewById(R.id.pass_text);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String login_str=log.getText().toString();
                final String pass_str = pass.getText().toString();

                if(login_str.isEmpty() || pass_str.isEmpty())
                {
                    showMessage("Не введен логин или пароль");
                }
                else
                {
                    //checkIfIfNewMaster(login_str, pass_str);
                    signIn(login_str, pass_str);
                }
            }
        });
    }

    private void signIn(final String login_str, final String pass_str) {
        mAuth.signInWithEmailAndPassword(login_str, pass_str)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    showMessage("Логин или пароль введены верно");
                    updateUI(login_str);
                }
//                else{
//                    showMessage("Логин или пароль введены неверно");
//                }
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                checkIfIfNewMaster(login_str, pass_str);
            }
        });
//        .addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                showMessage("Логин или пароль введены неверно err");
//            }
//        });
    }

    private void checkIfIfNewMaster(String login_str, String pass_str) {
        db.collection("Masters").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    boolean flag = false;
                    List<String> masterList = new ArrayList<>();
                    List<Boolean> newPass = new ArrayList<>();
                    List<Master> masters = new ArrayList<>();
                    for(DocumentSnapshot doc : task.getResult()) {
                        masterList.add(doc.toObject(Master.class).getEmail());
                        masters.add(doc.toObject(Master.class));
                        //newPass
                    }

                    for(int i =0;i<masters.size();i++)
                    {
                        if(masters.get(i).getEmail().equals(login_str)){
                            flag = true;
                            break;
                        }
                    }

                    if(flag)
                        if(pass_str.equals("barbershop"))
                            createMaster(login_str, pass_str);
                        else
                            showMessage("Пароль неверен");
                    else
                        showMessage("Логин или пароль неверен");
                }
            }
        });
    }

    private void createMaster(String login_str, String pass_str) {
        mAuth.createUserWithEmailAndPassword(login_str, pass_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    updateUI(login_str);
                }
//                else
//                {
//                    showMessage("Пользователь с такой почтой уже существует");
//                }
            }
        });
    }

    private void updateUI(String login_str) {
        db.collection("Users").document(login_str).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists())
                        {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

        db.collection("Masters").document(login_str).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists())
                        {
                            Intent intent = new Intent(getApplicationContext(), MainMasterActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

        db.collection("Admin").document(login_str).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.getResult().exists())
                        {
                            Intent intent = new Intent(getApplicationContext(), MainAdminActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getBaseContext().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_lang", lang);
        editor.apply();
    }

    public void LoadLocale(){
        SharedPreferences prefs = getBaseContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "ru");
        setLocale(language);
    }
}

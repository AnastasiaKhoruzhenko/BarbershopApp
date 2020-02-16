package com.coursework.barbershopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout1, textInputLayout2;
    private TextInputEditText log, pass;
    private Button login_btn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputLayout1 = (TextInputLayout)findViewById(R.id.textInputLayout_log);
        textInputLayout2 = (TextInputLayout)findViewById(R.id.textInputField_pass);
        login_btn = (Button)findViewById(R.id.button_login);
        log = (TextInputEditText)findViewById(R.id.log_text);
        pass = (TextInputEditText)findViewById(R.id.pass_text);

        String login_str=log.getText().toString();
        String pass_str = pass.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null){

        }
        else
        {

        }
    }
}

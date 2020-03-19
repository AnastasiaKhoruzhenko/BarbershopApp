package com.coursework.barbershopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout textInputLayout1, textInputLayout2;
    private EditText log, pass;
    private Button login_btn;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    showMessage("Не введен логин лии пароль");
                }
                else
                {
                    signIn(login_str, pass_str);
                }
            }
        });
    }

    private void signIn(final String login_str, final String pass_str) {
        mAuth.signInWithEmailAndPassword(login_str, pass_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    showMessage("Логин или пароль введены верно");
                    updateUI(login_str);
                }
                else{
                    showMessage("Логин или пароль введены неверно");
                }
            }
        });
//        .addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                showMessage("Логин или пароль введены неверно err");
//            }
//        });
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
}

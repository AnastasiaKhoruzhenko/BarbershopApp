package com.coursework.barbershopapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.coursework.barbershopapp.model.MaskWatcherBirthDate;
import com.coursework.barbershopapp.model.MaskWatcherPhone;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private Uri mImageUri;
    private TextInputEditText inp_log, inp_pass, inp_name, inp_surname, inp_conf_pass;
    private EditText inp_phone, inp_birthdate;
    private CircleImageView img;
    private TextInputLayout lay_log, lay_pass, lay_name, lay_surname, lay_conf_pass, lay_phone, lay_birthdate;
    private Button btn_reg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadLocale();
        setContentView(R.layout.activity_registration);
        getSupportActionBar().setElevation(0);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("personal_photos");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

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
        img = findViewById(R.id.imageView);

        if(checkPref())
            inp_log.setText(getEmailPref());

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        inp_phone.addTextChangedListener(new MaskWatcherPhone("#(###)###-##-##"));
        inp_birthdate.addTextChangedListener(new MaskWatcherBirthDate("##.##.####"));

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_reg.setClickable(false);

                validateName();
                validateSurname();
                validateDate();
                validatePhone();
                validateEmail();
                validatePass();
                validateConfPass();

                if(validateEqualPasswords() && validateName() && validateSurname() && validateDate() && validatePhone()
                && validateEmail() && validatePass() && validateConfPass())
                    createUserAccount(inp_log.getText().toString(),
                            inp_name.getText().toString(),
                            inp_surname.getText().toString(),
                            inp_phone.getText().toString(),
                            inp_birthdate.getText().toString(),
                            inp_pass.getText().toString());
                else
                    btn_reg.setClickable(true);
            }
        });
    }

    private boolean validateName(){
        if(inp_name.getText().toString().isEmpty()) {
            lay_name.setError(getResources().getString(R.string.cant_empty));
            return false;
        }
        lay_name.setError(null);
        return true;
    }
    private boolean validateSurname(){
        if(inp_surname.getText().toString().isEmpty()) {
            lay_surname.setError(getResources().getString(R.string.cant_empty));
            return false;
        }
        lay_surname.setError(null);
        return true;
    }
    private boolean validateEmail(){
        if(inp_log.getText().toString().isEmpty()) {
            lay_log.setError(getResources().getString(R.string.cant_empty));
            return false;
        }
        lay_log.setError(null);
        return true;
    }
    private boolean validateDate(){
        if(inp_birthdate.getText().toString().isEmpty()) {
            lay_birthdate.setError(getResources().getString(R.string.cant_empty));
            return false;
        }
        else if(inp_birthdate.getText().toString().length() != 10)
        {
            lay_birthdate.setError(getResources().getString(R.string.incorrect_date_of_birth));
            return false;
        }
        else if (Integer.valueOf(inp_birthdate.getText().toString().substring(0,2)) > 31
                || Integer.valueOf(inp_birthdate.getText().toString().substring(0,2)) < 1
                || Integer.valueOf(inp_birthdate.getText().toString().substring(3,5)) < 1
                || Integer.valueOf(inp_birthdate.getText().toString().substring(3,5)) > 12
                || Integer.valueOf(inp_birthdate.getText().toString().substring(6,10)) > 2020
                || Integer.valueOf(inp_birthdate.getText().toString().substring(6,10)) < 1920)
        {
            lay_birthdate.setError(getResources().getString(R.string.incorrect_date_of_birth));
            return false;
        }
        lay_birthdate.setError(null);
        return true;
    }
    private boolean validatePhone(){
        if(inp_phone.getText().toString().isEmpty()) {
            lay_phone.setError(getResources().getString(R.string.cant_empty));
            return false;
        }
        else if(inp_phone.getText().toString().length()!=15){
            lay_phone.setError(getResources().getString(R.string.phone_error));
            return false;
        }
        lay_phone.setError(null);
        return true;
    }
    private boolean validatePass(){
        if(inp_pass.getText().toString().isEmpty()) {
            lay_pass.setError(getResources().getString(R.string.cant_empty));
            return false;
        }
        else if(inp_pass.getText().toString().length()<8){
            lay_pass.setError(getResources().getString(R.string.password_less_8));
            return false;
        }
        lay_pass.setError(null);
        return true;
    }
    private boolean validateConfPass(){
        if(inp_conf_pass.getText().toString().isEmpty()) {
            lay_conf_pass.setError(getResources().getString(R.string.cant_empty));
            return false;
        }
        else if(inp_conf_pass.getText().toString().length()<8){
            lay_conf_pass.setError(getResources().getString(R.string.password_less_8));
            return false;
        }
        lay_conf_pass.setError(null);
        return true;
    }

    private boolean validateEqualPasswords(){
        if(!inp_pass.getText().toString().equals(inp_conf_pass.getText().toString())) {
            lay_conf_pass.setError(getResources().getString(R.string.passwords_not_equal));
            return false;
        }
        return true;
    }

    private void createUserAccount(final String email, final String name, final String surname, final String phone, final String birth, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    showMessage(getResources().getString(R.string.account_created));
                    //PreferenceUtils.saveEmail(email, getApplicationContext());
                    updateInfo(email, name, surname, phone, birth, mAuth.getCurrentUser());
                    uploadFile();
                }
                else
                {
                    showMessage(getResources().getString(R.string.email_exists));
                    btn_reg.setClickable(true);
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

        btn_reg.setClickable(true);

        this.finish();
    }

    private void choosePhoto() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
            mImageUri = data.getData();

        Picasso.get().load(mImageUri).into(img);
    }

    private String getFileExtension(Uri uri)
    {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(){
        if(mImageUri != null){
            // set image name in storage
            StorageReference fileRef = mStorageRef.child(inp_log.getText().toString());
            fileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        }
        else{

        }
    }

    private void showMessage(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    public boolean checkPref(){
        SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "def");
        return !email.equals("def");
    }

    public String getEmailPref()
    {
        SharedPreferences sharedPreferences = getSharedPreferences("myData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "def");
        return email;
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

package com.coursework.barbershopapp.Admin;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.coursework.barbershopapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class AddMasterFragment extends Fragment {

    private AddMasterViewModel mViewModel;
    private TextInputLayout tilSurname, tilName, tilPhone, tilEmail;
    private TextInputEditText tit_surname, tit_name, tit_phone, tit_email;
    private Button create_master;

    private FirebaseAuth mAuth;

    public static AddMasterFragment newInstance() {
        return new AddMasterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_master_fragment, container, false);
        tilName = view.findViewById(R.id.til_name);
        tilSurname = view.findViewById(R.id.til_surname);
        tilPhone = view.findViewById(R.id.til_phone);
        tilEmail = view.findViewById(R.id.til_email);
        tit_name = view.findViewById(R.id.tit_name);
        tit_surname = view.findViewById(R.id.tit_surname);
        tit_phone = view.findViewById(R.id.tit_phone);
        tit_email = view.findViewById(R.id.tit_email);

        create_master = view.findViewById(R.id.btn_create_master);

        create_master.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tit_name.getText().toString();
                String surname = tit_surname.getText().toString();
                String phone = tit_phone.getText().toString();
                String email = tit_email.getText().toString();
                //Toast.makeText(getContext(), name, Toast.LENGTH_SHORT).show();
                checkInfo(name, surname, phone, email);
            }
        });

        return view;
    }

    private void checkInfo(String name, String surname, String phone, String email) {
        if(name.isEmpty() || surname.isEmpty() || email.isEmpty() || phone.isEmpty())
        {
            Toast.makeText(getContext(), "Add all required information", Toast.LENGTH_SHORT).show();
        }else{
            registerNewMasterWithDefaultPassword(email, name, surname, phone);
        }
    }

    private void registerNewMasterWithDefaultPassword(final String email, final String name, final String surname, final String phone) {
        mAuth = FirebaseAuth.getInstance();

        // дефолтный пароль -> сделать для изменения в профиле админа
        String defaultPassword = "barbershop";
        mAuth.createUserWithEmailAndPassword(email, defaultPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    showMessage("Аккаунт успешно создан");
                    updateInfo(name, surname, email, phone);
                }
                else
                {
                    showMessage("Пользователь с такой почтой уже существует");
                }
            }
        });
    }

    private void updateInfo(String name, String surname, String email, String phone) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> master = new HashMap<>();
        master.put("Name", name);
        master.put("Surname", surname);
        master.put("Phone", phone);
        master.put("Email", email);
        master.put("DefaultPass", true);
        db.collection("Masters").document(email).set(master);

    }

    private void showMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddMasterViewModel.class);
        // TODO: Use the ViewModel



    }

}

package com.coursework.barbershopapp.Admin.ui.masters;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.coursework.barbershopapp.Admin.AddMasterFragment;
import com.coursework.barbershopapp.Admin.AddMasterViewModel;
import com.coursework.barbershopapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MastersAdminFragment extends Fragment {

    private MastersAdminViewModel mViewModel;
    private Button btn_add;
    private FloatingActionButton floating_add;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private TextInputLayout tilSurname, tilName, tilPhone, tilEmail;
    private TextInputEditText tit_surname, tit_name, tit_phone, tit_email;
    private Button create_master;

    private FirebaseAuth mAuth;

    public static MastersAdminFragment newInstance() {
        return new MastersAdminFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(this).get(MastersAdminViewModel.class);

        initImageBitmaps();

        View root = inflater.inflate(R.layout.masters_admin_fragment, container, false);

        tilName = root.findViewById(R.id.til_name);
        tilSurname = root.findViewById(R.id.til_surname);
        tilPhone = root.findViewById(R.id.til_phone);
        tilEmail = root.findViewById(R.id.til_email);
        tit_name = root.findViewById(R.id.tit_name);
        tit_surname = root.findViewById(R.id.tit_surname);
        tit_phone = root.findViewById(R.id.tit_phone);
        tit_email = root.findViewById(R.id.tit_email);

        create_master = root.findViewById(R.id.btn_create_master);

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


        RecyclerView recyclerView = root.findViewById(R.id.recview_masters);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        floating_add = root.findViewById(R.id.floating_fab_add);
        View llBottomSheet = root.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                floating_add.animate()
                        .scaleX(1-v).scaleY(1-v)
                        .setDuration(0)
                        .start();
            }
        });


//        // for bottom view
//        LinearLayout llBottomSheet = root.findViewById(R.id.bottom_sheet);
//
//        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
//
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//
//        //bottomSheetBehavior.setPeekHeight();
//
//        bottomSheetBehavior.setHideable(false);
//
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View view, int i) {
//
//            }
//
//            @Override
//            public void onSlide(@NonNull View view, float v) {
//
//            }
//        });



        return root;
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

    private void swapFragment() {
        AddMasterFragment addMaster = new AddMasterFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.add(R.id.fl_masters_admin, addMaster);
        fragmentTransaction.replace(R.id.fl_masters_admin, addMaster, "masters");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void initImageBitmaps(){
        mImageUrls.add("https://c1.staticflickr.com/5/4636/25316407448_de5fbf183d_o.jpg");
        mNames.add("Havasu Falls");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Trondheim");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Portugal");

        mImageUrls.add("https://i.redd.it/j6myfqglup501.jpg");
        mNames.add("Rocky Mountain National Park");

        mImageUrls.add("https://i.redd.it/0h2gm1ix6p501.jpg");
        mNames.add("Mahahual");

        mImageUrls.add("https://i.redd.it/k98uzl68eh501.jpg");
        mNames.add("Frozen Lake");

        mImageUrls.add("https://i.redd.it/glin0nwndo501.jpg");
        mNames.add("White Sands Desert");

        mImageUrls.add("https://i.redd.it/obx4zydshg601.jpg");
        mNames.add("Austrailia");

        mImageUrls.add("https://i.imgur.com/ZcLLrkY.jpg");
        mNames.add("Washington");
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        mViewModel = ViewModelProviders.of(this).get(MastersAdminViewModel.class);
//        // TODO: Use the ViewModel
//    }

}

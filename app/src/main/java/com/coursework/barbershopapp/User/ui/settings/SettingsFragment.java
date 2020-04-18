package com.coursework.barbershopapp.User.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SettingsFragment extends Fragment {

    private CircleImageView img;
    private TextView nameSurname;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private FirebaseFirestore db;
    private Uri mImageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        nameSurname = view.findViewById(R.id.tv_namesurname_insettings);

        mStorageRef = FirebaseStorage.getInstance().getReference("personal_photos");

        List<String> list_settings = new ArrayList<>();
        list_settings.add(getActivity().getResources().getString(R.string.account_settings));
        list_settings.add(getActivity().getResources().getString(R.string.app_settings));
        list_settings.add(getActivity().getResources().getString(R.string.salon_info_text));
        list_settings.add(getActivity().getResources().getString(R.string.exit));

        List<String> list_settings_descr = new ArrayList<>();
        list_settings_descr.add(getActivity().getResources().getString(R.string.account_settings_descr));
        list_settings_descr.add(getActivity().getResources().getString(R.string.app_settings_descr));
        list_settings_descr.add(getActivity().getResources().getString(R.string.salon_info_descr));
        list_settings_descr.add(getActivity().getResources().getString(R.string.exit_descr));

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        RecyclerView recyclerView = view.findViewById(R.id.recview_sett_profile);
        img = view.findViewById(R.id.circleImageView);
        img.setClickable(true);

        if(mAuth.getCurrentUser() != null || checkPref())
        {
            String email = "";
            if(mAuth.getCurrentUser() != null) {
                email = mAuth.getCurrentUser().getEmail();
                setNameAndSurname(email);
            }
            else if(checkPref())
                email = getEmailPref();

            Toast.makeText(getContext(), email, Toast.LENGTH_LONG).show();

            StorageReference phRef = FirebaseStorage.getInstance().getReference()
                    .child("personal_photos/"+email);
            phRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    //img.setImageURI(uri);

                    Glide.with(getContext())
                            .load(uri)
                            .into(img);
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
        }

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });


        RecyclerViewSettingsAdapter adapter = new RecyclerViewSettingsAdapter(getContext(), list_settings, list_settings_descr);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        resetStaticData();

        return view;
    }

    private void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null)
            mImageUri = data.getData();

        Picasso.get().load(mImageUri).into(img);
        if(mAuth.getCurrentUser() != null)
        {
            uploadFile(mAuth.getCurrentUser().getEmail());
        }
        else{
            if(checkPref())
            {
                String email = getEmailPref();
                uploadFile(email);
            }
            else
                img.setClickable(false);
        }
    }

    private void uploadFile(String email){
        if(mImageUri != null){
            // set image name in storage
            StorageReference fileRef = mStorageRef.child(email);
            fileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    });
        }
    }

    private void resetStaticData() {
        Common.STEP = 0;
        Common.currentDate.add(Calendar.DATE, 0);
        Common.currentTimeSlot = -1;
        Common.currentBarber = null;
        Common.currentService = null;
        Common.currentServiceType = null;
    }

    private boolean checkPref(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "def");
        return !email.equals("def");
    }

    private String getEmailPref()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "def");
        return email;
    }

    private void setNameAndSurname(String email){
        db.collection("Users").document(email)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    User user = task.getResult().toObject(User.class);
                    nameSurname.setText(user.getName() + " " + user.getSurname());
                }
            }
        });
    }
}
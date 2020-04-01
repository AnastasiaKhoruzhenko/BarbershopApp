package com.coursework.barbershopapp.Masters.ui.settings;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.Admin.ui.masters.RecyclerViewAdapter;
import com.coursework.barbershopapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SettingsMasterFragment extends Fragment {

    FirebaseAuth mAuth;
    CircleImageView img;
    private List<String> arrSettings = new ArrayList<>(Arrays.asList("Личная информация", "Время работы", "Предоставляемые услуги", "Настройки приложения"));

    private static final int PICK_IMAGE_REQUEST = 1;
    StorageReference mStorageRef;
    Uri mImageUri;


    public static SettingsMasterFragment newInstance() {
        return new SettingsMasterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_master_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("personal_photos");
        img = view.findViewById(R.id.circleImageViewMaster);


        StorageReference phRef = FirebaseStorage.getInstance().getReference()
                .child("personal_photos/"+mAuth.getCurrentUser().getEmail());
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
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recview_sett_mas);
        RecyclerViewAdapterMasterSett adapterMasterSett =
                new RecyclerViewAdapterMasterSett(getContext(), arrSettings);
        recyclerView.setAdapter(adapterMasterSett);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



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
        uploadFile(mAuth.getCurrentUser().getEmail());
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
        else{

        }
    }
}

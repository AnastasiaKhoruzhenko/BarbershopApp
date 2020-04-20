package com.coursework.barbershopapp.Admin.ui.info;

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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.User.ui.settings.RecyclerViewSettingsAdapter;
import com.coursework.barbershopapp.model.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class InfoAdminFragment extends Fragment {

    private RecyclerView recyclerView;
    private CircleImageView img;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_admin_fragment, container, false);
        recyclerView = view.findViewById(R.id.recview_admin_sett);
        img = view.findViewById(R.id.imageView4);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("personal_photos");

        StorageReference phRef = FirebaseStorage.getInstance().getReference()
                .child("personal_photos/"+mAuth.getCurrentUser().getEmail());
        phRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
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

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        initRecView();

        return  view;
    }

    private void initRecView() {
        List<String> list_settings_admin = new ArrayList<String>();
        list_settings_admin.add(getActivity().getResources().getString(R.string.account_settings));
        list_settings_admin.add(getActivity().getResources().getString(R.string.salon_info_text));
        list_settings_admin.add(getActivity().getResources().getString(R.string.app_settings));
        list_settings_admin.add(getActivity().getResources().getString(R.string.exit));

        List<String> list_descr_settings_admin = new ArrayList<String>();
        list_descr_settings_admin.add(getActivity().getResources().getString(R.string.account_settings_descr));
        list_descr_settings_admin.add(getActivity().getResources().getString(R.string.salon_info_descr));
        list_descr_settings_admin.add(getActivity().getResources().getString(R.string.app_settings_descr));
        list_descr_settings_admin.add(getActivity().getResources().getString(R.string.exit_descr));

        RecyclerViewSettingsAdmin adapter = new RecyclerViewSettingsAdmin(getContext(), list_settings_admin, list_descr_settings_admin);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
    }
}

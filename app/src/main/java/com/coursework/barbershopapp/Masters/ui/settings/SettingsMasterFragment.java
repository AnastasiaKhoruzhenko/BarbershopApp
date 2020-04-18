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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.coursework.barbershopapp.Admin.ui.masters.RecyclerViewAdapter;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.Master;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class SettingsMasterFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CircleImageView img;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference mStorageRef;
    private Uri mImageUri;
    private TextView name_surname;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_master_fragment, container, false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("personal_photos");
        img = view.findViewById(R.id.circleImageViewMaster);
        name_surname = view.findViewById(R.id.tv_namesurname_massettings);
        setNameAndSurname(mAuth.getCurrentUser().getEmail());

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

        List<String> list_settings_master = new ArrayList<String>();
        list_settings_master.add(getActivity().getResources().getString(R.string.account_settings));
        list_settings_master.add(getActivity().getResources().getString(R.string.my_services));
        list_settings_master.add(getActivity().getResources().getString(R.string.app_settings));
        list_settings_master.add(getActivity().getResources().getString(R.string.exit));

        List<String> list_descr_settings_master = new ArrayList<String>();
        list_descr_settings_master.add(getActivity().getResources().getString(R.string.account_settings_descr));
        list_descr_settings_master.add(getActivity().getResources().getString(R.string.my_services_descr));
        list_descr_settings_master.add(getActivity().getResources().getString(R.string.app_settings_descr));
        list_descr_settings_master.add(getActivity().getResources().getString(R.string.exit_descr));



        RecyclerView recyclerView = view.findViewById(R.id.recview_sett_mas);
        RecyclerViewAdapterMasterSett adapterMasterSett =
                new RecyclerViewAdapterMasterSett(getContext(), list_settings_master, list_descr_settings_master);
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

    private void setNameAndSurname(String email){
        db.collection("Masters").document(email)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    Master user = task.getResult().toObject(Master.class);
                    name_surname.setText(user.getName() + " " + user.getSurname());
                }
            }
        });
    }
}

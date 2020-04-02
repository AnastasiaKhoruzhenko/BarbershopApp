package com.coursework.barbershopapp.Masters.ui.home;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.coursework.barbershopapp.Admin.ui.home.RecyclerViewCommentAdapter;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.BookingInformation;
import com.coursework.barbershopapp.model.Comment;
import com.coursework.barbershopapp.model.Master;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeMasterFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    RecyclerView recComments;

    public static HomeMasterFragment newInstance() {
        return new HomeMasterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_master_fragment, container, false);
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkDefaulPassword(mAuth.getCurrentUser().getEmail());
                }
            }, 200);
        }

        recComments = view.findViewById(R.id.recview_my_comments);
        initComments();

        return view;
    }

    private void initComments() {

        String email = mAuth.getCurrentUser().getEmail();

        db.collection("Comments").document(email)
                .collection("Comments").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Comment> listQ = new ArrayList<>();
                            for (QueryDocumentSnapshot querySnapshot : task.getResult()) {
                                Comment comment = querySnapshot.toObject(Comment.class);
                                listQ.add(comment);
                            }

                            RecyclerViewCommentAdapter adapter = new RecyclerViewCommentAdapter(getContext(), listQ);
                            recComments.setAdapter(adapter);
                            LinearLayoutManager layoutManager
                                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                            recComments.setLayoutManager(layoutManager);
                        }
                    }
                })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), getResources().getString(R.string.no_comments_on_you), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkDefaulPassword(String email) {
        db.collection("Masters").document(email).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            Master master = task.getResult().toObject(Master.class);
                            if(master.getDefaultPass())
                                showChangeDefaultPassword(email);
                        }
                    }
                });
    }

    private void showChangeDefaultPassword(String email) {
        Dialog dialog = new Dialog(getContext());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.alert_change_default_pass);
        dialog.setTitle(getResources().getString(R.string.set_new_password));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        TextInputLayout pass1 = dialog.findViewById(R.id.til_password_change);
        TextInputLayout pass2 = dialog.findViewById(R.id.til_password_conf_change);
        EditText pass1_c = dialog.findViewById(R.id.ti_password_change);
        EditText pass2_c = dialog.findViewById(R.id.ti_password_conf_change);
        TextView nameSurname = dialog.findViewById(R.id.tv_namesurname_changePass);
        ImageView img = dialog.findViewById(R.id.imageView_master);
        Button btn = dialog.findViewById(R.id.button2);

        db.collection("Masters").document(mAuth.getCurrentUser().getEmail()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Master master = task.getResult().toObject(Master.class);
                            nameSurname.setText(master.getName() + " " + master.getSurname());
                        }
                    }
                });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pas1 = pass1_c.getText().toString();
                String pas2 = pass2_c.getText().toString();
                if(!pas1.equals(pas2))
                {
                    Toast.makeText(getContext(), getResources().getString(R.string.passwords_not_equal), Toast.LENGTH_LONG).show();
                }
                else if(pas1.length()<8)
                    Toast.makeText(getContext(), getResources().getString(R.string.password_less_8), Toast.LENGTH_LONG).show();
                else
                {
                    mAuth.getCurrentUser().updatePassword(pas1);
                    db.collection("Masters")
                            .document(mAuth.getCurrentUser().getEmail())
                            .update("defaultPass", false);
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }
}

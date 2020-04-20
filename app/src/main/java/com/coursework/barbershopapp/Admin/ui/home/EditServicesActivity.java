package com.coursework.barbershopapp.Admin.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.AboutService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EditServicesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    FirebaseFirestore db;
    private TextView close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_services);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recview_edit_services);
        close = findViewById(R.id.close_img);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String name = getIntent().getStringExtra("service");
        initView(name);

    }

    private void initView(String name) {

        db.collection("ServicesMan").document(name)
                .collection("Services").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            ArrayList<AboutService> aboutServiceList = new ArrayList<>();
                            for(QueryDocumentSnapshot doc : task.getResult())
                            {
                                AboutService serv = doc.toObject(AboutService.class);
                                aboutServiceList.add(serv);
                            }

                            init(aboutServiceList, name);
                        }
                    }
                });
    }

    private void init(ArrayList<AboutService> aboutServiceList, String name) {
        RecyclerViewEditServicesAdapter adapter = new RecyclerViewEditServicesAdapter(this, aboutServiceList, name);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}

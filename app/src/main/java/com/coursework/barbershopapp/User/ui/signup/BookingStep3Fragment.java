package com.coursework.barbershopapp.User.ui.signup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.Master;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep3Fragment extends Fragment {

    static BookingStep3Fragment instance;
    Unbinder unbinder;

    @BindView(R.id.recview_masters_to_choose)
    RecyclerView recyclerView;

    private FirebaseFirestore db;
    LocalBroadcastManager localBroadcastManager;

    private BroadcastReceiver barberDoneReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadBarbers(Common.currentService.getName());
            //Toast.makeText(getContext(), Common.currentService.getName(), Toast.LENGTH_LONG).show();
        }
    };

    private void loadBarbers(String name) {

                db.collection("ServicesMan").document(name).collection("Barbers").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            ArrayList<DocumentReference> personList = new ArrayList<>();

                            ArrayList<Master> pList=new ArrayList<>();
                            for(QueryDocumentSnapshot person:task.getResult())
                            {
                                DocumentReference doc = person.getDocumentReference("barber");
                                personList.add(doc);
                            }

                            for(DocumentReference doc : personList)
                            {
                                doc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        Master p = task.getResult().toObject(Master.class);
                                        pList.add(p);
                                        //Toast.makeText(getActivity(), String.valueOf(pList.size()), Toast.LENGTH_LONG).show();
                                        RecyclerViewMastersChooseAdapter adapter = new RecyclerViewMastersChooseAdapter(getContext(), pList, true);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    }
                                });
                            }

//                            // send brouadcast to Fragment3
//                            Intent intent = new Intent(Common.KEY_SERVICES_LOAD_DONE);
//                            intent.putParcelableArrayListExtra(Common.KEY_SERVICES_LOAD_DONE, pList);
//                            localBroadcastManager.sendBroadcast(intent);
                        }
                    }
                });


    }

    //IMastersLoadListener iMastersLoadListener;

    public static BookingStep3Fragment getInstance(){
        if(instance == null)
            instance = new BookingStep3Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        iMastersLoadListener = this;
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(barberDoneReciever, new IntentFilter(Common.KEY_DISPLAY_BARBER));
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(barberDoneReciever);
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_booking_step3, container, false);

        unbinder = ButterKnife.bind(this, view);
        db = FirebaseFirestore.getInstance();

        //DocumentReference doc = db.collection()

        initRecView(view);

        // iMastersLoadListener.onMastersLoadSuccess();

        return view;
    }

    private void initRecView(View view) {

    }

//    private void loadList(List<Master> pList) {
//
//        RecyclerViewMastersChooseAdapter recView = new RecyclerViewMastersChooseAdapter(getContext(), pList);
//        recyclerView.setAdapter(recView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//    }
}

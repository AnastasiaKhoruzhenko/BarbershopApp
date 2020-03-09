package com.coursework.barbershopapp.ui.signup;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.coursework.barbershopapp.Interface.IMastersLoadListener;
import com.coursework.barbershopapp.Masters.ui.settings.RecyclerViewAdapterMasterSett;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookingStep3Fragment extends Fragment implements IMastersLoadListener {

    static BookingStep3Fragment instance;
    Unbinder unbinder;

    @BindView(R.id.recview_masters_to_choose)
    RecyclerView recyclerView;

    private FirebaseFirestore db;
    LocalBroadcastManager localBroadcastManager;

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
//        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
//        localBroadcastManager.registerReceiver(servicesDoneReciever, new IntentFilter(Common.KEY_SERVICES_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
        //localBroadcastManager.unregisterReceiver(servicesDoneReciever);
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

        // /ServicesMan/HairCut/Barbers/a@a.ri
        db.collection("ServicesMan").document("HairCut").collection("Barbers").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<DocumentReference> personList = new ArrayList<>();

                            List<Person> pList=new ArrayList<>();
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
                                        Person p = task.getResult().toObject(Person.class);
                                        Toast.makeText(getActivity(), p.getSurname(), Toast.LENGTH_LONG).show();
                                        pList.add(p);
                                    }
                                });
                            }

                            loadList(pList);
                        }
                    }
                });

        // iMastersLoadListener.onMastersLoadSuccess();

        return view;
    }

    private void loadList(List<Person> pList) {

        RecyclerViewMastersChooseAdapter recView = new RecyclerViewMastersChooseAdapter(getContext(), pList);
        recyclerView.setAdapter(recView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onMastersLoadSuccess(List<String> masters) {

    }

    @Override
    public void onMastersLoadFailed(String message) {

    }
}

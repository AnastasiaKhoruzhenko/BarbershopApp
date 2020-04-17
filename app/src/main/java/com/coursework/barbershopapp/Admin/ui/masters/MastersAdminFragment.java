package com.coursework.barbershopapp.Admin.ui.masters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.coursework.barbershopapp.model.MaskWatcherPhone;
import com.coursework.barbershopapp.model.Master;
import com.coursework.barbershopapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MastersAdminFragment extends Fragment {

    private Button btn_add;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mScore = new ArrayList<>();
    private ArrayList<String> mEmail = new ArrayList<>();

    private EditText tit_surname, tit_name, tit_phone, tit_email;

    private List<Master> personList = new ArrayList<>();
    private FirebaseFirestore db;
    private int count_masters_document=0;
    private RecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private View llBottomSheet;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.masters_admin_fragment, container, false);
        setHasOptionsMenu(true);

        TextInputLayout tilName = root.findViewById(R.id.til_name);
        TextInputLayout tilSurname = root.findViewById(R.id.til_surname);
        TextInputLayout tilPhone = root.findViewById(R.id.til_phone);
        TextInputLayout tilEmail = root.findViewById(R.id.til_email);
        tit_name = root.findViewById(R.id.tit_name);
        tit_surname = root.findViewById(R.id.tit_surname);
        tit_phone = root.findViewById(R.id.tit_phone);
        tit_email = root.findViewById(R.id.tit_email);

        tit_phone.addTextChangedListener(new MaskWatcherPhone("#(###)###-##-##"));

        db = FirebaseFirestore.getInstance();

        Button create_master = root.findViewById(R.id.btn_create_master);


        create_master.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tit_name.getText().toString();
                String surname = tit_surname.getText().toString();
                String phone = tit_phone.getText().toString();
                String email = tit_email.getText().toString();

                checkInfo(name, surname, phone, email);
            }
        });


        recyclerView = root.findViewById(R.id.recview_masters);

        llBottomSheet = root.findViewById(R.id.bottom_sheet);

        db.collection("Masters").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            count_masters_document = task.getResult().size();

                            getItemsForRecycleView(count_masters_document);
                        }
                    }
                });

        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuInflater inflater1 = getActivity().getMenuInflater();
        inflater1.inflate(R.menu.create_master_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search_master);
        SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void checkInfo(String name, String surname, String phone, String email) {
        if(name.isEmpty() || surname.isEmpty() || email.isEmpty() || phone.isEmpty())
        {
            Toast.makeText(getContext(), getResources().getString(R.string.set_all_fields), Toast.LENGTH_SHORT).show();
        }else{
            registerNewMasterWithDefaultPassword(email, name, surname, phone);
        }
    }

    private void registerNewMasterWithDefaultPassword(final String email, final String name, final String surname, final String phone) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // дефолтный пароль
        String defaultPassword = "barbershop";

        db.collection("Masters").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    List<String> masterList = new ArrayList<>();
                    for(DocumentSnapshot doc : task.getResult())
                        masterList.add(doc.toObject(Master.class).getEmail());

                    if(masterList.contains(email))
                        showMessage(getResources().getString(R.string.master_with_email_exists));
                    else
                        updateInfo(name, surname, email, phone);
                }
            }
        });
    }

    private void updateInfo(String name, String surname, String email, String phone) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> master = new HashMap<>();
        master.put("name", name);
        master.put("surname", surname);
        master.put("phone", phone);
        master.put("email", email);
        master.put("defaultPass", true);
        master.put("score", String.valueOf(0.0));
        master.put("services", null);
        master.put("dates", null);
        db.collection("Masters").document(email).set(master);

        Map<String, Object> map = new HashMap<>();
        map.put("count", String.valueOf(0));
        map.put("rating", String.valueOf(0));
        db.collection("Comments").document(email).set(map);

        // clear input text after creation
        tit_name.getText().clear();
        tit_email.getText().clear();
        tit_phone.getText().clear();
        tit_surname.getText().clear();

    }

    private void showMessage(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void getItemsForRecycleView(final int count)
    {
        db = FirebaseFirestore.getInstance();

        db.collection("Masters").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        Map<String, Object> d;
                        for(int i = 0; i < count; ++i)
                        {
                            d = list.get(i).getData();
                            personList.add(new Master(d.get("email").toString(),
                                    d.get("name").toString(),
                                    d.get("surname").toString(),
                                    d.get("phone").toString(),
                                    d.get("score").toString(),
                                    Boolean.getBoolean(d.get("defaultPass").toString())));
                        }

                        initImageBitmaps(personList);
                    }
                });
    }

    private void initImageBitmaps(List<Master> personList){

        adapter = new RecyclerViewAdapter(getContext(), personList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });
    }
}

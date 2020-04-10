package com.coursework.barbershopapp.User.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ss.com.bannerslider.Slider;

import com.coursework.barbershopapp.Admin.ui.home.RecyclerViewBestMastersAdapter;
import com.coursework.barbershopapp.Interface.IBannerLoadListener;
import com.coursework.barbershopapp.LoginActivity;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.RegistrationActivity;
import com.coursework.barbershopapp.Service.PicassoImageLoadService;
import com.coursework.barbershopapp.User.ui.signup.RecyclerViewMastersChooseAdapter;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.Master;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment implements IBannerLoadListener {

    private Unbinder unbinder;

    @BindView(R.id.slidingPaneLayout)
    Slider sliderNews;
    @BindView(R.id.recview_masters_user)
    RecyclerView recyclerView;

    private IBannerLoadListener iBannerLoadListener;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, root);

        setHasOptionsMenu(true);
        auth = FirebaseAuth.getInstance();

        Slider.init(new PicassoImageLoadService());
        iBannerLoadListener = this;
        
        loadBanner();
        loadMasters();

        resetStaticData();

        return root;
    }

    private void loadMasters() {
        db.collection("Masters").orderBy("score", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                        {
                            List<Master> list=new ArrayList<>();
                            for (QueryDocumentSnapshot doc : task.getResult())
                            {
                                list.add(doc.toObject(Master.class));
                            }

                            initRecVie(list);
                        }
                    }
                });
    }

    private void initRecVie(List<Master> list) {

        RecyclerViewBestMastersAdapter adapter = new RecyclerViewBestMastersAdapter(list, getContext());
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
    }

    private void resetStaticData() {
        Common.STEP = 0;
        Common.currentDate.add(Calendar.DATE, 0);
        Common.currentTimeSlot = -1;
        Common.currentBarber = null;
        Common.currentService = null;
        Common.currentServiceType = null;
    }

    private void loadBanner() {
        db = FirebaseFirestore.getInstance();
        db.collection("Banner").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<String> list = new ArrayList<>();
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot bannerSnap:task.getResult()){
                                list.add(String.valueOf(bannerSnap.get("image")));
                            }
                            iBannerLoadListener.onBannerLoadSuccess(list);
                        }
                        else{

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iBannerLoadListener.onBannerLoadFailed(e.getMessage());
                    }
                });
    }

    @Override
    public void onBannerLoadSuccess(List<String> banners) {
        sliderNews.setAdapter(new HomeSliderAdapter(banners));
    }

    @Override
    public void onBannerLoadFailed(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_my_account:
                if(auth.getCurrentUser() != null)
                {
                    showFullDialogWithInfo();
                }
                else {
                    showRegisterDialog();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFullDialogWithInfo() {
    }

    private void showRegisterDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        //alertDialog.setTitle("Вы не авторизованы");
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setMessage(getResources().getString(R.string.this_can_do_only_registered));
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getResources().getString(R.string.not_now),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.register),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                        startActivity(intent);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getResources().getString(R.string.sign_in),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                });
        alertDialog.show();
    }
}
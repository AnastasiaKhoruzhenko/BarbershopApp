package com.coursework.barbershopapp.User.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ss.com.bannerslider.Slider;

import com.coursework.barbershopapp.Interface.IBannerLoadListener;
import com.coursework.barbershopapp.LoginActivity;
import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.RegistrationActivity;
import com.coursework.barbershopapp.Service.PicassoImageLoadService;
import com.coursework.barbershopapp.model.Common;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment implements IBannerLoadListener {

    private Unbinder unbinder;

    @BindView(R.id.slidingPaneLayout)
    Slider sliderNews;

    IBannerLoadListener iBannerLoadListener;

    private FirebaseFirestore db;
    FirebaseAuth auth;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        auth = FirebaseAuth.getInstance();

        unbinder = ButterKnife.bind(this, root);
        Slider.init(new PicassoImageLoadService());
        iBannerLoadListener = this;
        
        loadBanner();

        resetStaticData();


        //final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                //textView.setText(s);
//            }
//        });
        return root;
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
        alertDialog.setMessage("Этот раздел доступен только авторизированным пользователям");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Не сейчас",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Регистрация",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                        startActivity(intent);
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Вход",
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
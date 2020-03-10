package com.coursework.barbershopapp.ui.signup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.AboutService;
import com.coursework.barbershopapp.model.Common;
import com.coursework.barbershopapp.model.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SignUpFragment extends Fragment {

    LocalBroadcastManager localBroadcastManager;
    FirebaseFirestore db;

    @BindView(R.id.btn_prev_step)
    ImageButton btn_prevStep;
    @BindView(R.id.btn_next_step)
    ImageButton btn_nextStep;
    @OnClick(R.id.btn_next_step)
    void NextClick(){
        if(Common.STEP < 4 || Common.STEP >= 0)
        {
            Common.STEP++;
            if(Common.STEP == 1) // after choose service type
            {
                if(Common.currentService != null)
                {
                    loadServicesMore(Common.currentService.getName());
                    loadBarber(Common.currentService.getName());
                }
            }
            else if(Common.STEP == 2)
            {
                if(Common.currentServiceType != null)
                {
                    loadBarber(Common.currentService.getName());
                }
                else
                {
                    Toast.makeText(getContext(), "Выберите тип услуги", Toast.LENGTH_SHORT).show();
                }
            }
            else if(Common.STEP == 3){
                if(Common.currentBarber != null){
                    loadTimeSlots(Common.currentBarber.getEmail());
                    Toast.makeText(getContext(), "Выберите dhtvzzzz", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Выберите", Toast.LENGTH_SHORT).show();
                }
            }
            else if(Common.STEP == 4)
            {
                if(Common.currentTimeSlot != -1)
                {
                    confirmBooking();
                }
            }
            viewPager.setCurrentItem(Common.STEP);
        }
    }

    private void confirmBooking() {

        Intent intent = new Intent(Common.KEY_CONFURM_BOOKING);
        localBroadcastManager.sendBroadcast(intent);
    }

    private void loadTimeSlots(String email) {

        Intent intent = new Intent(Common.KEY_DISPLAY_TIMESLOT);
        localBroadcastManager.sendBroadcast(intent);
    }

    @OnClick(R.id.btn_prev_step)
    void prevStep(){
        if(Common.STEP == 4 || Common.STEP > 0)
        {
            Common.STEP--;
            viewPager.setCurrentItem(Common.STEP);
            if(Common.STEP < 4)
            {
                btn_nextStep.setEnabled(true);
                setColorButton();
            }
        }
    }

    private void loadServicesMore(String name) {
        //  /ServicesMan/HairCut/Services/LongHairCut
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

                            // send brouadcast to Fragment2
                            Intent intent = new Intent(Common.KEY_SERVICE_LOAD_DONE);
                            intent.putParcelableArrayListExtra(Common.KEY_SERVICE_LOAD_DONE, aboutServiceList);
                            localBroadcastManager.sendBroadcast(intent);
                        }
                    }
                });
    }

    private void loadBarber(String name)
    {
        Intent intent = new Intent(Common.KEY_DISPLAY_BARBER);
        localBroadcastManager.sendBroadcast(intent);
    }

    BroadcastReceiver nextBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int step = intent.getIntExtra(Common.KEY_STEP, 0);
            if(step == 1)
                Common.currentService = intent.getParcelableExtra(Common.KEY_SERVICE_STORE);
            else if (step == 2)
                Common.currentServiceType = intent.getParcelableExtra(Common.KEY_SERVICE_SELECTED);
            else if(step == 3)
                Common.currentBarber = intent.getParcelableExtra(Common.KEY_BARBER_SELECTED);
            else if(step == 4)
                Common.currentTimeSlot = intent.getIntExtra(Common.KEY_TIME_SLOT, -1);

            btn_nextStep.setEnabled(true);
            setColorButton();
        }
    };

    @BindView(R.id.step_view)
    StepView stepView;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private SignUpViewModel signUpViewModel;
    private TextView serviceName, serviceDescription, servicePrice;

    private Unbinder unbinder;

    private MyViewPagerAdapterSignUp myViewPagerAdapterSignUp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    private void setColorButton() {

        if(btn_nextStep.isEnabled())
        {
            btn_nextStep.setBackgroundResource(R.drawable.right);
        }
        else
        {
            btn_nextStep.setBackgroundResource(R.drawable.right_dis);
        }
        if(btn_prevStep.isEnabled())
        {
            btn_prevStep.setBackgroundResource(R.drawable.left);
        }
        else
        {
            btn_prevStep.setBackgroundResource(R.drawable.left_dis);
        }
    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(nextBroadcastReceiver);
        super.onDestroy();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        signUpViewModel =
                ViewModelProviders.of(this).get(SignUpViewModel.class);
        View root = inflater.inflate(R.layout.fragment_signup, container, false);

        myViewPagerAdapterSignUp = new MyViewPagerAdapterSignUp(getChildFragmentManager());

        unbinder = ButterKnife.bind(this, root);
        setColorButton();

        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(nextBroadcastReceiver, new IntentFilter(Common.KEY_NEXT_BTN));

        setupStepView();
        viewPager.setAdapter(new MyViewPagerAdapterSignUp(getChildFragmentManager()));
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                // stepview animating when next or prev
                stepView.go(position, true);
                stepView.getState()
                        .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                        .animationType(StepView.ANIMATION_CIRCLE);

                if(position == 0)
                {
                    btn_prevStep.setEnabled(false);
                    btn_nextStep.setVisibility(View.VISIBLE);
                }

                if(position == 1) {
                    btn_nextStep.setVisibility(View.VISIBLE);
                    btn_prevStep.setEnabled(true);
                }

                if(position == 2) {
                    btn_nextStep.setVisibility(View.VISIBLE);
                    btn_prevStep.setEnabled(true);
                    btn_nextStep.setEnabled(true);
                }

                if(position == 3) {
                    btn_nextStep.setVisibility(View.VISIBLE);
                    btn_prevStep.setEnabled(true);
                    btn_nextStep.setEnabled(true);
                }

                if(position == 4) {
                    btn_prevStep.setEnabled(true);
                    btn_nextStep.setVisibility(View.INVISIBLE);
                }

                btn_nextStep.setEnabled(false);

                setColorButton();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Раздел");
        stepList.add("Услуги");
        stepList.add("Мастер");
        stepList.add("Дата и время");
        stepList.add("Подтверждение");

        stepView.setSteps(stepList);
    }
}
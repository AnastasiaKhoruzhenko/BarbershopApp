package com.coursework.barbershopapp.ui.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.coursework.barbershopapp.R;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;
import java.util.List;

public class SignUpFragment extends Fragment {

    @BindView(R.id.btn_next_step)
    Button btn_nextStep;

    @BindView(R.id.btn_prev_step)
    Button btn_prevStep;

    @BindView(R.id.step_view)
    StepView stepView;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private SignUpViewModel signUpViewModel;
    private TextView serviceName, serviceDescription, servicePrice;
    private ImageView chosen;
    private DividerItemDecoration dividerItem;

    private Unbinder unbinder;

    private MyViewPagerAdapterSignUp myViewPagerAdapterSignUp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        signUpViewModel =
                ViewModelProviders.of(this).get(SignUpViewModel.class);
        View root = inflater.inflate(R.layout.fragment_signup, container, false);

        myViewPagerAdapterSignUp = new MyViewPagerAdapterSignUp(getChildFragmentManager());

        unbinder = ButterKnife.bind(this, root);

        setupStepView();
        viewPager.setAdapter(new MyViewPagerAdapterSignUp(getChildFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                stepView.go(position, true);
                stepView.getState()
                        .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                        .animationType(StepView.ANIMATION_CIRCLE);

                if(position == 0) {
                    btn_prevStep.setEnabled(false);
                }
                else
                    btn_prevStep.setEnabled(true);

                if(position == 3)
                    btn_nextStep.setEnabled(false);
                else
                    btn_nextStep.setEnabled(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        signUpViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    private void setupStepView() {
        List<String> stepList = new ArrayList<>();
        stepList.add("Раздел");
        stepList.add("Услуги");
        stepList.add("Время и мастер");
        stepList.add("Готово");

        stepView.setSteps(stepList);
    }
}
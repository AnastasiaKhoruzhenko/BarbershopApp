package com.coursework.barbershopapp.ui.signup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.coursework.barbershopapp.R;

public class SignUpFragment extends Fragment {

    private SignUpViewModel signUpViewModel;
    private TextView serviceName, serviceDescription, servicePrice;
    private ImageView chosen;
    private DividerItemDecoration dividerItem;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        signUpViewModel =
                ViewModelProviders.of(this).get(SignUpViewModel.class);
        View root = inflater.inflate(R.layout.fragment_signup, container, false);

        serviceName=root.findViewById(R.id.tx_serviceName);
        serviceDescription = root.findViewById(R.id.tw_serviceDescription);
        servicePrice = root.findViewById(R.id.tv_price);
        chosen = root.findViewById(R.id.image_choose);




//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        signUpViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}
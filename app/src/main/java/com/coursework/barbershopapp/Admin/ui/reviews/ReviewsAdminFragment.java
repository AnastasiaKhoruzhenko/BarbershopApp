package com.coursework.barbershopapp.Admin.ui.reviews;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coursework.barbershopapp.R;

public class ReviewsAdminFragment extends Fragment {

    private ReviewsAdminViewModel mViewModel;

    public static ReviewsAdminFragment newInstance() {
        return new ReviewsAdminFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reviews_admin_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ReviewsAdminViewModel.class);
        // TODO: Use the ViewModel
    }

}

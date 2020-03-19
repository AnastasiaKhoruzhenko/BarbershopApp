package com.coursework.barbershopapp.User.ui.myVisitings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coursework.barbershopapp.R;
import com.google.android.material.tabs.TabLayout;

public class MyVisitingsFragment extends Fragment{

    TabLayout tabLayout;
    ViewPager viewPager;

    public static MyVisitingsFragment newInstance() {
        return new MyVisitingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.my_visitings_fragment, container, false);


        viewPager  = root.findViewById(R.id.viewpager_visitings);
        setupViewPager(viewPager);

        tabLayout = root.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);



        return root;
    }

    private void setupViewPager(ViewPager viewPager) {

        int a=1, b=2;
        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getChildFragmentManager());
        adapter.addFragment(new TabVisitingFragment(a), "Nearest");
        adapter.addFragment(new TabVisitingFragment(b), "Past");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}

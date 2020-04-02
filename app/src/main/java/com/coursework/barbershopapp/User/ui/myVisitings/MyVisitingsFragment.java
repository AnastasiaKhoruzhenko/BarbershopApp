package com.coursework.barbershopapp.User.ui.myVisitings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coursework.barbershopapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MyVisitingsFragment extends Fragment{

    TabLayout tabLayout;
    ViewPager viewPager;
    TextView text;
    FirebaseAuth mAuth;

    StorageReference mStorageRef;

    public static MyVisitingsFragment newInstance() {
        return new MyVisitingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.my_visitings_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("personal_photos");

        viewPager  = root.findViewById(R.id.viewpager_visitings);
        text = root.findViewById(R.id.tv_not_authorized);

        tabLayout = root.findViewById(R.id.tabs);

        if(!checkPref() && mAuth.getCurrentUser() == null)
            text.setText(getResources().getString(R.string.you_have_no_bookings));
        else if (mAuth.getCurrentUser() != null)
            setupViewPager(viewPager, mAuth.getCurrentUser().getEmail());
        else if(checkPref())
            setupViewPager(viewPager, getEmailPref());

        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    private void setupViewPager(ViewPager viewPager, String email) {

        int a=1, b=2;
        ViewPagerFragmentAdapter adapter = new ViewPagerFragmentAdapter(getChildFragmentManager());
        adapter.addFragment(new TabVisitingFragment(a, email), getResources().getString(R.string.nearest));
        adapter.addFragment(new TabVisitingFragment(b, email), getResources().getString(R.string.past));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public boolean checkPref(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "def");
        return !email.equals("def");
    }

    public String getEmailPref()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "def");
        return email;
    }
}

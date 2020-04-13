package com.coursework.barbershopapp.User.ui.myVisitings;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.model.Common;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class MyVisitingsFragment extends Fragment{

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.my_visitings_fragment, container, false);
        resetStaticData();
        setHasOptionsMenu(true);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("personal_photos");

        ViewPager viewPager = root.findViewById(R.id.viewpager_visitings);
        TextView text = root.findViewById(R.id.tv_not_authorized);

        TabLayout tabLayout = root.findViewById(R.id.tabs);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.question));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_my_account:
                showHelpDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        View customLayout = getLayoutInflater().inflate(R.layout.dialog_help, null);

        ImageView img = customLayout.findViewById(R.id.imageView6);
        TextView text = customLayout.findViewById(R.id.tv_swipe_right);
        Button button = customLayout.findViewById(R.id.button3);

        Point point = new Point();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        display.getSize(point);
        final int width = point.x;
        final float halfW = width/2.0f;
        ObjectAnimator lftToRgt,rgtToLft;
        lftToRgt = ObjectAnimator.ofFloat( img,"translationX",0f,halfW )
                .setDuration(1000);
        lftToRgt.setRepeatCount(ValueAnimator.INFINITE);
        lftToRgt.setRepeatMode(ValueAnimator.RESTART);

        AnimatorSet s = new AnimatorSet();
        s.play( lftToRgt );
        s.start();

        builder.setView(customLayout);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private boolean checkPref(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "def");
        return !email.equals("def");
    }

    private String getEmailPref()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myData", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "def");
        return email;
    }

    private void resetStaticData() {
        Common.STEP = 0;
        Common.currentDate.add(Calendar.DATE, 0);
        Common.currentTimeSlot = -1;
        Common.currentBarber = null;
        Common.currentService = null;
        Common.currentServiceType = null;
    }
}

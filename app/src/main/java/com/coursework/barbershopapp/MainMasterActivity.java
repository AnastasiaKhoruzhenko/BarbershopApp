package com.coursework.barbershopapp;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainMasterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_master);

        BottomNavigationView navView = findViewById(R.id.nav_view_master);
        AppBarConfiguration appBarConfigurationAdmin = new AppBarConfiguration.Builder(
                R.id.navigation_home_master, R.id.navigation_mynearestvisitors_master, R.id.navigation_settings_master)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_master);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfigurationAdmin);
        NavigationUI.setupWithNavController(navView, navController);
    }
}

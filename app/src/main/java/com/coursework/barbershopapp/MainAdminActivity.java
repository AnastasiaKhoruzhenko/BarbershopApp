package com.coursework.barbershopapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainAdminActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadLocale();
        setContentView(R.layout.activity_main_admin);

        BottomNavigationView navView = findViewById(R.id.nav_view_admin);
        //navView.inflateMenu(R.menu.bottom_nav_menu_admin);
        AppBarConfiguration appBarConfigurationAdmin = new AppBarConfiguration.Builder(
                R.id.navigation_home_admin, R.id.navigation_reviews_admin, R.id.navigation_masters_admin, R.id.navigation_reviews_admin)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_admin);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfigurationAdmin);
        NavigationUI.setupWithNavController(navView, navController);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getBaseContext().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_lang", lang);
        editor.apply();
    }

    public void LoadLocale(){
        SharedPreferences prefs = getBaseContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_lang", "ru");
        setLocale(language);
    }
}

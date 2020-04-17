package com.coursework.barbershopapp.User.ui.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.coursework.barbershopapp.R;
import com.github.angads25.toggle.widget.LabeledSwitch;

import java.util.Locale;

public class SettingsSelectActivity extends AppCompatActivity {

    LabeledSwitch push, theme, lan;
    Button save;
    String lang = "ru";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoadLocale();
        setContentView(R.layout.activity_settings_select);

        push = findViewById(R.id.switch_push);
        lan = findViewById(R.id.switch_language);
        save = findViewById(R.id.btn_save_settapp);
        TextView close = findViewById(R.id.close_img);

        lan.setLabelOff("RU");
        lan.setLabelOn("EN");

        if(lang.equals("en"))
            lan.setOn(true);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lan.isOn())
                {
                    setLocale("en");
                }
                else
                {
                    setLocale("ru");
                }

                finish();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        lang = language;
    }
}

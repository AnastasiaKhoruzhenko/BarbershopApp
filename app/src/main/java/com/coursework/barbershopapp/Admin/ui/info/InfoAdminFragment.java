package com.coursework.barbershopapp.Admin.ui.info;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coursework.barbershopapp.R;
import com.coursework.barbershopapp.User.ui.settings.RecyclerViewSettingsAdapter;
import com.coursework.barbershopapp.model.Common;

public class InfoAdminFragment extends Fragment {

    private RecyclerView recyclerView;

    public static InfoAdminFragment newInstance() {
        return new InfoAdminFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_admin_fragment, container, false);
        recyclerView = view.findViewById(R.id.recview_admin_sett);

        initRecView();

        return  view;
    }

    private void initRecView() {
        RecyclerViewSettingsAdmin adapter = new RecyclerViewSettingsAdmin(getContext(), Common.list_settings_admin, Common.list_settings_admin);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}

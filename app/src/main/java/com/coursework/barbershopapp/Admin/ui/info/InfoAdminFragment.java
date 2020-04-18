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

import java.util.ArrayList;
import java.util.List;

public class InfoAdminFragment extends Fragment {

    private RecyclerView recyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.info_admin_fragment, container, false);
        recyclerView = view.findViewById(R.id.recview_admin_sett);

        initRecView();

        return  view;
    }

    private void initRecView() {
        List<String> list_settings_admin = new ArrayList<String>();
        list_settings_admin.add(getActivity().getResources().getString(R.string.salon_info_text));
        list_settings_admin.add(getActivity().getResources().getString(R.string.app_settings));
        list_settings_admin.add(getActivity().getResources().getString(R.string.exit));

        List<String> list_descr_settings_admin = new ArrayList<String>();
        list_descr_settings_admin.add(getActivity().getResources().getString(R.string.salon_info_descr));
        list_descr_settings_admin.add(getActivity().getResources().getString(R.string.app_settings_descr));
        list_descr_settings_admin.add(getActivity().getResources().getString(R.string.exit_descr));

        RecyclerViewSettingsAdmin adapter = new RecyclerViewSettingsAdmin(getContext(), list_settings_admin, list_descr_settings_admin);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}

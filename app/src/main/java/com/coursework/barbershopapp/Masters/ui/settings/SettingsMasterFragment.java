package com.coursework.barbershopapp.Masters.ui.settings;

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

import com.coursework.barbershopapp.Admin.ui.masters.RecyclerViewAdapter;
import com.coursework.barbershopapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsMasterFragment extends Fragment {

    private SettingsMasterViewModel mViewModel;
    private List<String> arrSettings = new ArrayList<>(Arrays.asList("Личный кабинет", "Время работы", "Предоставляемые услуги", "Моя статистика", "Настройки приложения"));

    public static SettingsMasterFragment newInstance() {
        return new SettingsMasterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_master_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recview_sett_mas);
        RecyclerViewAdapterMasterSett adapterMasterSett =
                new RecyclerViewAdapterMasterSett(getContext(), arrSettings);
        recyclerView.setAdapter(adapterMasterSett);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SettingsMasterViewModel.class);
        // TODO: Use the ViewModel
    }

}

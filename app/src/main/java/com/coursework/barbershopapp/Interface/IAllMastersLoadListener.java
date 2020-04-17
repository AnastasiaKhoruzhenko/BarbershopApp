package com.coursework.barbershopapp.Interface;

import com.coursework.barbershopapp.model.Master;

import java.util.List;

public interface IAllMastersLoadListener {
    void onAllMastersLoad(List<Master> areaEmailsList);
    void onAllMastersLoadFailed(String mess);
}

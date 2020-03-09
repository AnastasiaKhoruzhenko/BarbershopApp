package com.coursework.barbershopapp.Interface;

import java.util.List;

public interface IMastersLoadListener {

    void onMastersLoadSuccess(List<String> masters);

    void onMastersLoadFailed(String message);
}

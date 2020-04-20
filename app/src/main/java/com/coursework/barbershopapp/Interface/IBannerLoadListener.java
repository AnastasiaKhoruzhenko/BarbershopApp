package com.coursework.barbershopapp.Interface;

import com.coursework.barbershopapp.model.Banner;

import java.util.List;

public interface IBannerLoadListener {

    void onBannerLoadSuccess(List<String> banners);
    void onBannerLoadFailed(String message);

}

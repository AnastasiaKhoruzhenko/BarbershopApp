package com.coursework.barbershopapp.ui.myVisitings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyVisitingsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyVisitingsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is myVisitings fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    // TODO: Implement the ViewModel
}

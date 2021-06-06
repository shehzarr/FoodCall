package com.example.foodcall.ui.HelpCenter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HelpcenterViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HelpcenterViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
package com.example.foodcall.ui.MyVouchers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyVouchersViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyVouchersViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is vouchers fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
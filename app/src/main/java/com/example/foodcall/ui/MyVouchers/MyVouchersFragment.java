package com.example.foodcall.ui.MyVouchers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.foodcall.R;

public class MyVouchersFragment extends Fragment {

    private MyVouchersViewModel myVouchersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myVouchersViewModel = ViewModelProviders.of(this).get(MyVouchersViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myaddresses, container, false);
        final TextView textView = root.findViewById(R.id.nav_myaddresses);
        myVouchersViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
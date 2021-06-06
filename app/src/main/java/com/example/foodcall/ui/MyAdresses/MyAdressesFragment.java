package com.example.foodcall.ui.MyAdresses;

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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MyAdressesFragment extends Fragment {

    private MyAdressesViewModel myAdressesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myAdressesViewModel = ViewModelProviders.of(this).get(MyAdressesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_myaddresses, container, false);
        final TextView textView = root.findViewById(R.id.nav_myaddresses);
        myAdressesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        return root;
    }
}
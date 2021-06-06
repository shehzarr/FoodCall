package com.example.foodcall.ui.HelpCenter;

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

public class HelpcenterFragment extends Fragment {

    private HelpcenterViewModel helpcenterViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        helpcenterViewModel =
                ViewModelProviders.of(this).get(HelpcenterViewModel.class);
        View root = inflater.inflate(R.layout.fragment_helpcenter, container, false);
        final TextView textView = root.findViewById(R.id.helpcenter);
//        helpcenterViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}
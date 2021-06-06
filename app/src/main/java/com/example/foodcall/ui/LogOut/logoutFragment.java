package com.example.foodcall.ui.LogOut;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.foodcall.Database.DB_Helper;
import com.example.foodcall.Login_SignUp;
import com.example.foodcall.R;
import com.example.foodcall.User;
import com.google.firebase.auth.FirebaseAuth;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

public class logoutFragment extends Fragment {

    public static final String TAG = "Logout Fragment";
    private logoutViewModel logoutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("device_token");
        Log.d(TAG, " Device token removed from shared preferences");
        editor.apply();

        DB_Helper helper = OpenHelperManager.getHelper(getActivity(), DB_Helper.class);
        RuntimeExceptionDao<User, Integer> myContactDao = helper.getContactRuntimeDao();

        List<User> users = myContactDao.queryForAll();
        myContactDao.delete(users);
        OpenHelperManager.releaseHelper();

        Intent i = new Intent(getActivity(), Login_SignUp.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        getActivity().finish();
        logoutViewModel =
                ViewModelProviders.of(this).get(logoutViewModel.class);

        View root = inflater.inflate(R.layout.fragment_logout, container, false);
        final TextView textView = root.findViewById(R.id.logout);

        logoutViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
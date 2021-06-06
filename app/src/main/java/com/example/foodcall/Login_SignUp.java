package com.example.foodcall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.foodcall.Database.DB_Helper;
import com.example.foodcall.Restaurant.MainActivity_Restaurant;
import com.example.foodcall.ui.login.LoginActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

public class Login_SignUp extends AppCompatActivity {

    FirebaseAuth mAuth;
    private static int SPLAST_TIME_OUT = 2000;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//
//                    RuntimeExceptionDao<User, Integer> myContactDao = helper.getContactRuntimeDao();
//                    List<User> contacts = myContactDao.queryForAll();
//
//                    Intent temp = new Intent(getApplicationContext(), SplashScreen.class);
//                    startActivity(temp);
//                    finish();
//
//                    for (User user : contacts) {
//                        if (user.getUID().contentEquals(mAuth.getCurrentUser().getUid())) {
//                            if (user.getCustomer()) {
//                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(i);
//                                finish();
//                            } else {
//                                Intent i = new Intent(getApplicationContext(), MainActivity_Restaurant.class);
//                                startActivity(i);
//                                finish();
//                            }
//                        }
//                    }
//                }
//            }, SPLAST_TIME_OUT);
            DB_Helper helper = OpenHelperManager.getHelper(this, DB_Helper.class);
            RuntimeExceptionDao<User, Integer> myContactDao = helper.getContactRuntimeDao();
            List<User> contacts = myContactDao.queryForAll();

            for (User user : contacts) {
                if (user.getUID().contentEquals(mAuth.getCurrentUser().getUid())) {
                    if (user.getCustomer()) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.putExtra("user_name", user.name);
                        i.putExtra("user_email", mAuth.getCurrentUser().getEmail());
                        startActivity(i);
                        finish();
                    } else {
                        Intent i = new Intent(getApplicationContext(), MainActivity_Restaurant.class);
                        i.putExtra("user_name", user.name);
                        i.putExtra("user_email", mAuth.getCurrentUser().getEmail());
                        startActivity(i);
                        finish();
                    }
                }
            }
        }

        setContentView(R.layout.activity_login__sign_up);

        Button signUp = findViewById(R.id.button5);
        Button signIn = findViewById(R.id.button3);

        signIn.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        signUp.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SignUp.class);
                startActivity(i);
            }
        });


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}

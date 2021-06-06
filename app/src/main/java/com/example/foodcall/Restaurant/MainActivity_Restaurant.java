package com.example.foodcall.Restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.foodcall.Database.DB_Helper;
import com.example.foodcall.Order.Item;
import com.example.foodcall.Login_SignUp;
import com.example.foodcall.Notification.Token;
import com.example.foodcall.R;
import com.example.foodcall.User;
import com.example.foodcall.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_Restaurant extends AppCompatActivity {

    public static final String TAG = "Inside Main_Restaurant";

    private ArrayList<String> mImageUrls = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mPrice = new ArrayList<>();

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main__restaurant);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        //Add restaurant item button
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddRestaurantItem.class);
                startActivity(i);
            }
        });


        Button more = findViewById(R.id.more_res_main);
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), More_Menu_RestaurantMain.class);
                startActivity(i);
            }
        });

        //Logout btn
        Button btn = findViewById(R.id.button4);
        btn.setOnTouchListener(new View.OnTouchListener() {

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
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                DB_Helper helper = OpenHelperManager.getHelper(getApplicationContext(), DB_Helper.class);
                RuntimeExceptionDao<User, Integer> myContactDao = helper.getContactRuntimeDao();

                List<User> users = myContactDao.queryForAll();
                myContactDao.delete(users);
                OpenHelperManager.releaseHelper();
                Intent i = new Intent(getApplicationContext(), Login_SignUp.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String tok = prefs.getString("device_token", "");
        updateToken(tok);
        initData();
    }

    private void initData() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRef = mFirebaseDatabase.getReference().child("users").child(uid).child("menu");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
                showData(dataSnapshot);
                initRecyclerView();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    public void updateToken(String token) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Token");
        Token mtoken = new Token(token);
        ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mtoken);
    }

    private void showData(DataSnapshot dataSnapshot) {
        mImageUrls.clear();
        mNames.clear();
        mPrice.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Item data = new Item();
            data.setName(ds.getValue(Item.class).getName());
            data.setPrice(ds.getValue(Item.class).getPrice());
//            data.name = ds.getValue(Item.class).getName();
//            data.price = ds.getValue(Item.class).getPrice();

            mImageUrls.add(ds.getKey());
            mNames.add(data.getName());
            mPrice.add(data.getPrice());

            Log.d(TAG, " : inside showData " + ds.getKey() + " " + data.getName() + " " + data.getPrice());
        }
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.recycler_view_restaurant);
        RecyclerView_Restaurant adapter = new RecyclerView_Restaurant(mImageUrls, mNames, mPrice, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}

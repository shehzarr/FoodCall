package com.example.foodcall.Restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.foodcall.Order.Order;
import com.example.foodcall.R;
import com.example.foodcall.Order.RecyclerView_Order_History;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Orders_Vendor extends AppCompatActivity {

    public static final String TAG = "Orders_Vendor";

    List<Order> total_orders = new ArrayList<>();
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_orders__vendor);



        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        Query myRef = mFirebaseDatabase.getReference().child("users")
                .child(uid).child("my_orders").orderByChild("my_orders");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChanged called for Users");
                total_orders.clear();
                showUserData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }

    private void showUserData(DataSnapshot dataSnapshot) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Order temp = new Order();
            temp = (ds.getValue(Order.class));
            Log.d(TAG, " : order Found: " + temp.toString());
            total_orders.add(temp);
        }
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: Orders_Vednor");
        RecyclerView recyclerView = findViewById(R.id.recycler_orderhistory);
        final RecyclerView_Order_History adapter = new RecyclerView_Order_History(total_orders, getApplicationContext());
        adapter.setVendor_trigger(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}

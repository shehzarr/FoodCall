package com.example.foodcall.Restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodcall.Order.Item;
import com.example.foodcall.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddRestaurantItem extends AppCompatActivity {

    String uid;
    public static final String TAG = "AddRestaurantItem";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restaurant_item);

        Button btn = findViewById(R.id.add_item);

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
                EditText temp = findViewById(R.id.item_name);
                String item_name = temp.getText().toString();
                temp = findViewById(R.id.item_price);
                String item_price = temp.getText().toString();

                Log.d(TAG, " : onClick for Add Item " + item_name + " " + item_price);

                if (!item_name.isEmpty() && !item_price.isEmpty()) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = mFirebaseDatabase.getReference();
                    uid = mAuth.getCurrentUser().getUid();

                    Item temp1 = new Item(item_name, item_price);
                    Log.d(TAG, "onClick : Add Item" + temp1.getName() + " " + temp1.getPrice());
                    myRef.child("users").child(uid).child("menu").push().setValue(temp1);
                    finish();
                } else
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                            .show();
            }
        });
    }
}

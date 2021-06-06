package com.example.foodcall.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodcall.Checkout.Checkout;
import com.example.foodcall.Order.Item;
import com.example.foodcall.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Menu extends AppCompatActivity {

    public static final String TAG = "Inside Menu Activity";

    ListView listView;
    ListAdapter l;
    ArrayList<menu_class> arraylist = new ArrayList<>();

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        listView = (ListView) findViewById(R.id.listview);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        final Adapter_Menu_Screen arrayAdapter = new Adapter_Menu_Screen(this, R.layout.activity_adapter_menu, arraylist);
        listView.setAdapter(arrayAdapter);

        Button btn = findViewById(R.id.button6);
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


        uid = extras.getString("uid");
        final String name = extras.getString("name");

        TextView obj = findViewById(R.id.res_name_for_menu);
        obj.setText(name);

        if (!uid.isEmpty()) {

            FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference myRef = mFirebaseDatabase.getReference().child("users")
                    .child(uid).child("menu");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    arraylist.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d(TAG, "item Found in Vendor Menu with key = " + ds.getKey());
                        menu_class data = new menu_class();
                        data.setItemName(ds.getValue(Item.class).getName());
                        data.setPrice(ds.getValue(Item.class).getPrice());
                        data.isSelected = false;

                        data.setItem_id(ds.getKey());
                        arraylist.add(data);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Menu load failed. Try again.",
                    Toast.LENGTH_SHORT).show();
            finish();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Checkout.class);

                ArrayList<String> selected_name = new ArrayList<>();
                ArrayList<String> selected_price = new ArrayList<>();
                ArrayList<String> selected_item_id = new ArrayList<>();

                for (menu_class menu : arraylist) {
                    if (menu.isSelected) {
                        selected_name.add(menu.itemName);
                        selected_price.add(menu.price);
                        selected_item_id.add(menu.item_id);
                    }
                }
                if (selected_name.size() > 0) {
                    Toast.makeText(getApplicationContext(), "Adding items to bucket...",
                            Toast.LENGTH_SHORT).show();
                    intent.putExtra("item_name", selected_name);
                    intent.putExtra("item_price", selected_price);
                    intent.putExtra("item_id", selected_item_id);
                    intent.putExtra("vendor_id", uid);
                    intent.putExtra("vendor_name", name);
                    Log.d(TAG, "Loading menu for vendor: " + uid);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please select item(s)",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public void feedData(ArrayList<menu_class> menu_classes) {
        Boolean c = false;
        menu_class obj1 = new menu_class("Cheese Burger", "300", c);
        menu_class obj2 = new menu_class("Chicken Burger", "270", c);
        menu_class obj3 = new menu_class("Nuggets", "350", c);
        menu_class obj4 = new menu_class("Sundae", "320", c);
        menu_class obj5 = new menu_class("Big Mac", "400", c);

        menu_classes.add(obj1);
        menu_classes.add(obj2);
        menu_classes.add(obj3);
        menu_classes.add(obj4);
        menu_classes.add(obj5);
    }

}

package com.example.foodcall.Checkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodcall.Database.DB_Helper;
import com.example.foodcall.Notification.Data_Notification;
import com.example.foodcall.Order.Item;
import com.example.foodcall.MainActivity;
import com.example.foodcall.Notification.APIService;
import com.example.foodcall.Notification.Client;
import com.example.foodcall.Notification.Response;
import com.example.foodcall.Notification.Sender;
import com.example.foodcall.Order.Order;
import com.example.foodcall.R;
import com.example.foodcall.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class Checkout extends AppCompatActivity {

    private static final String TAG = "Checkout";

    ArrayList<String> itemName = new ArrayList<>();
    ArrayList<String> itemPrice = new ArrayList<>();
    static ArrayList<Integer> count = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private DatabaseReference notificationRef;

    private ProgressDialog placing_order;

    String uid;
    String u_name;
    String u_address;
    String vid;
    String v_name;

    static TextView tv;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAA1POMuGg:APA91bGrcQ72R4w2xaLiPwVn0HXy-O5oi5uZy3B4MhRmIwsLPPq-xAdk7UNjJEMm_EUiVNMgCuElrt-lMYPKo1DZ9TL28fs39z2eTCxzYuqCK72fUSHH-40TE-MhRiW7j0XdLc7SWx5k";
    final private String contentType = "application/json";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    APIService apiService;
    Boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_checkout);

        mAuth = FirebaseAuth.getInstance();

        uid = mAuth.getCurrentUser().getUid();
        if (mAuth.getCurrentUser() != null) {
            DB_Helper helper = OpenHelperManager.getHelper(this, DB_Helper.class);
            RuntimeExceptionDao<User, Integer> myContactDao = helper.getContactRuntimeDao();
            List<User> contacts = myContactDao.queryForAll();

            for (User user : contacts) {
                if (user.getUID().contentEquals(mAuth.getCurrentUser().getUid())) {
                    TextView address = findViewById(R.id.textView8);
                    address.setText(user.getAddress() + ", " + user.getCity());
                    u_address = user.getAddress() + ", " + user.getCity();
                    u_name = user.getName();
                }
            }
        }

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        final ArrayList<String> item_name = extras.getStringArrayList("item_name");
        final ArrayList<String> item_price = extras.getStringArrayList("item_price");
        ArrayList<String> item_id = extras.getStringArrayList("item_id");

        vid = extras.getString("vendor_id");
        v_name = extras.getString("vendor_name");
        tv = findViewById(R.id.tot_price);

        int total = 0;

        //Setting data for recyclerView
        for (int j = 0; j < item_name.size(); j++) {
            itemName.add(item_name.get(j));
            itemPrice.add(item_price.get(j));

            String[] temp = item_price.get(j).split(" ");
            total = total + Integer.parseInt(temp[1]);
            count.add(1);
        }

        tv.setText(Integer.toString(total));

        Button confirm = findViewById(R.id.confirm_order);
        confirm.setOnTouchListener(new View.OnTouchListener() {

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
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Placing your order...",
//                        Toast.LENGTH_SHORT).show();

                placing_order = new ProgressDialog(Checkout.this);
                placing_order.setMessage("Placing your order");
                //Show temporary screen

                Order order = new Order();

                order.setOrder_total(tv.getText().toString());
                order.setOrderDate(new Date().toString());
                order.setCustomer(uid);
                order.setCustomer_name(u_name);
                order.setOrder_address(u_address);
                order.setRestaurant(vid);
                order.setRestaurant_name(v_name);

                List<Item> total_items = new ArrayList<>();

                for (int j = 0; j < item_name.size(); j++) {
                    Item temp = new Item();
                    temp.setName(item_name.get(j));
                    temp.setPrice(item_price.get(j));
                    temp.setQuantity(count.get(j));
                    count.set(j, 1);
                    total_items.add(temp);
                }
                order.setItems(total_items);

                Log.d(TAG, " : Order created with uid: " + uid + " _ vid: " + vid + " _ totalprice: "
                        + tv.getText().toString());

                mAuth = FirebaseAuth.getInstance();
                mFirebaseDatabase = FirebaseDatabase.getInstance();

                myRef = mFirebaseDatabase.getReference().child("all_orders");
                myRef.push().setValue(order);

                myRef = mFirebaseDatabase.getReference().child("users").child(vid).child("my_orders");
                myRef.push().setValue(order)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                placing_order.dismiss();
                                Toast.makeText(getApplicationContext(), "Order placed successfully"
                                        , Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                placing_order.dismiss();
                                Toast.makeText(getApplicationContext(), "Something went wrong. Try again!"
                                        , Toast.LENGTH_SHORT).show();
                            }
                        });

                myRef = mFirebaseDatabase.getReference().child("users").child(uid).child("my_orders");
                myRef.push().setValue(order);

                apiService = Client.getRetrofit("https://fcm.googleapis.com/").create(APIService.class);

                DatabaseReference allToken = FirebaseDatabase.getInstance().getReference("Token").child(vid);
                allToken.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String token = ds.getValue().toString();

                            Log.d(TAG," got Token for vendor : " + token);

                            Data_Notification dataNotification = new Data_Notification(uid, "You have got an order", "New Order", vid, R.drawable.logo);
                            Sender sender = new Sender(dataNotification, token);

                            apiService.sendNotification(sender)
                                    .enqueue(new Callback<Response>() {
                                        @Override
                                        public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
//                                            Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_SHORT)
//                                                    .show();
                                        }

                                        @Override
                                        public void onFailure(Call<Response> call, Throwable t) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("bool", "true");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        initRecycler();
    }

    public static void func_add(String added_price, int pos) {

        int hell = count.get(pos);
        hell++;
        count.set(pos, hell);

        int curr = Integer.parseInt(tv.getText().toString());
        int inc = Integer.parseInt(added_price);
        curr = curr + inc;
        tv.setText(Integer.toString(curr));
    }

    public static void func_sub(String added_price, int pos) {
        int hell = count.get(pos);
        hell--;
        count.set(pos, hell);

        int curr = Integer.parseInt(tv.getText().toString());
        int inc = Integer.parseInt(added_price);
        curr = curr - inc;
        tv.setText(Integer.toString(curr));
    }

    void initRecycler() {
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        initRecyclerView();
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview.");
        RecyclerView recyclerView = findViewById(R.id.itemsList);
        RecyclerView_Adapter_Checkout adapter = new RecyclerView_Adapter_Checkout(itemName, itemPrice, count, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }
}

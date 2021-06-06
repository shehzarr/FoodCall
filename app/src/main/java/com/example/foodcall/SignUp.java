package com.example.foodcall;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.foodcall.Database.DB_Helper;
import com.example.foodcall.Restaurant.MainActivity_Restaurant;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "SignUp Activity";

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    String name, phone, address, uid;
    String city_name = null;
    Boolean customer;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide Action Bar in this activity only
        this.getSupportActionBar().hide();
        //Stop Keyboard Popping
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);

        Spinner city = findViewById(R.id.city);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(SignUp.this,
                R.layout.spinner_layout, getResources().getStringArray(R.array.cities));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(myAdapter);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        Button signUp = findViewById(R.id.signup);

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
                EditText temp = findViewById(R.id.email);
                final String email = temp.getText().toString();
                temp = findViewById(R.id.password);
                final String password = temp.getText().toString();
                temp = findViewById(R.id.confirm_password);
                final String pass_confirm = temp.getText().toString();

                temp = findViewById(R.id.name);
                name = temp.getText().toString();
                temp = findViewById(R.id.phone_num);
                phone = temp.getText().toString();
                temp = findViewById(R.id.street_addr);
                address = temp.getText().toString();
                Spinner spinner = findViewById(R.id.city);
                city_name = spinner.getSelectedItem().toString();

                temp = findViewById(R.id.customer);
                String i = temp.getText().toString();
                if (i.contentEquals("Yes") || i.contentEquals("yes"))
                    customer = false;
                else
                    customer = true;

                Log.d(TAG, "Inside listener" + email + " " + password + " " + pass_confirm + "\n");
                Log.d(TAG, name + " " + phone + " " + address + " " + city_name + " " + customer);

                if (!password.isEmpty() && password.contentEquals(pass_confirm) && !email.isEmpty() &&
                        !name.isEmpty() && !phone.isEmpty() && !address.isEmpty() && !city_name.isEmpty()) {
                    Log.d("SignUpMe", email + " " + password + " " + pass_confirm);
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("SignUpMe", "User created");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        uid = user.getUid();
                                        String deviceToken = FirebaseInstanceId.getInstance().getId();

                                        myRef.child(uid).child("device_token")
                                                .setValue(deviceToken)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(getApplicationContext(), "Device token added"
                                                                , Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getApplicationContext(), "Device token" +
                                                                        "not added. Please retry login."
                                                                , Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                        updateUI(user);
                                    } else {
                                        updateUI(null);
                                    }
                                }
                            });
                } else
                    Toast.makeText(getApplicationContext(), "Fill all fields correctly please", Toast.LENGTH_SHORT)
                            .show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "You clicked the button", Toast.LENGTH_SHORT).show();
    }

    private void updateUI(final FirebaseUser user) {
        if (user == null) {
            Toast.makeText(getApplicationContext(), "Sorry . An error occured. Please try again.", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(getApplicationContext(), "Account created. Redirecting...", Toast.LENGTH_SHORT)
                    .show();

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    User value = (User) dataSnapshot.getValue(User.class);
                    Log.d(TAG, "Value is: " + value);

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

            String userId = user.getUid();

            User new_user = new User(name, phone, address, city_name, customer);
            myRef.child("users").child(userId).setValue(new_user);

            addToDB(userId, new_user.getName(), new_user.getPhone(), new_user.getAddress(),
                    new_user.getCity(), new_user.getCustomer());

            if (customer == true) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user_name", new_user.getName());
                intent.putExtra("user_email", mAuth.getCurrentUser().getEmail());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), MainActivity_Restaurant.class);
                intent.putExtra("user_name", new_user.getName());
                intent.putExtra("user_email", mAuth.getCurrentUser().getEmail());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    private void addToDB(String UID, String name, String phone, String address, String city, Boolean customer) {
        DB_Helper helper = OpenHelperManager.getHelper(getApplicationContext(), DB_Helper.class);
        RuntimeExceptionDao<User, Integer> myContactDao = helper.getContactRuntimeDao();

        myContactDao.create(new User(UID, name, phone, address, city, customer));
        Log.d(TAG, " : User added to local db.");
        OpenHelperManager.releaseHelper();
    }
}

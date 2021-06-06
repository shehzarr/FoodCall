package com.example.foodcall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.foodcall.Checkout.Checkout;
import com.example.foodcall.Notification.Token;
import com.example.foodcall.ui.login.LoginActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                Intent intent = new Intent(getApplicationContext(), Checkout.class);
//                startActivity(intent);
//
//
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        Intent i = getIntent();
        Bundle extras = i.getExtras();
        View headerView = navigationView.getHeaderView(0);

        //Setting username and email in nav_header_main
        TextView user_name = headerView.findViewById(R.id.name_nav);
        user_name.setText(extras.getString("user_name"));

        TextView user_email = headerView.findViewById(R.id.email_nav);
        user_email.setText(extras.getString("user_email"));

        // Passing each menu_class ID as a set of Ids because each
        // menu_class should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_orders, R.id.nav_myaddresses,
                R.id.logout, R.id.helpcenter, R.id.aboutus)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, Login_SignUp.class));
                    finish();
                }
            }
        };

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String tok = prefs.getString("device_token", "");
        updateToken(tok);
        //setNavigationViewListener();
    }

    public void updateToken(String token) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Token");
        Token mtoken = new Token(token);
        ref.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(mtoken);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_class; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        Log.d("Nav", "OnClick Called");
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        int id = item.getItemId();
//        if (id == R.id.logout) {
//            Log.d("Nav1", "Button selected. OnClick Called");
////            Toast.makeText(getApplicationContext(), "Logout clicked", Toast.LENGTH_SHORT).show();
//            FirebaseAuth mAuth = FirebaseAuth.getInstance();
//            mAuth.signOut();
//            Intent i = new Intent(getApplicationContext(), Login_SignUp.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//        }
//        if (id == R.id.nav_home) {
//
//
//            Log.d("Nav1", "Button selected. OnClick Called");
////            Toast.makeText(getApplicationContext(), "Logout clicked", Toast.LENGTH_SHORT).show();
//            Intent i = new Intent(getApplicationContext(), MainActivity.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//        }
//
//        if (id == R.id.nav_orders) {
//            Log.d("Nav1", "Orders Button selected. OnClick Called");
////            Toast.makeText(getApplicationContext(), "Logout clicked", Toast.LENGTH_SHORT).show();
////            Intent i = new Intent(getApplicationContext(), MainActivity);
////            startActivity(i);
//
//            FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//            OrdersFragment ordersFragment = new OrdersFragment();
//            FragmentManager manager = getSupportFragmentManager();
//            manager.beginTransaction().replace(R.id.nav_host_fragment, ordersFragment).commit();
//
//        }
//
//        //Implement for other items in nav bar
//
//        drawer.closeDrawer(GravityCompat.START);
//        return false;
//    }

//    private void setNavigationViewListener() {
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//    }
}

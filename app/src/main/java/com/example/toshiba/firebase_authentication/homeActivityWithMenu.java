package com.example.toshiba.firebase_authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.toshiba.firebase_authentication.Western.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.R.attr.name;

public class homeActivityWithMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private TextView UserName;
    private TextView UserEmail;

    private DatabaseReference databaseReference;

    private User currUser;

    NavigationView navigationView = null;
    Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_with_menu);

        // Retrieve User object from bundle.
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            currUser = (User)bundle.get("CURRENT_USER");
        }

        // Set the fragment initially
        MainFragment fragment = new MainFragment();
        fragment.setArguments(savedInstanceState);

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fcontainer, fragment);
        fragmentTransaction.commit();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present

        UserName = (TextView) findViewById(R.id.User_Name);
        UserEmail = (TextView) findViewById(R.id.OwlEmail);

        UserEmail.setText(currUser.getEmail());

        //udatabase = FirebaseDatabase.getInstance().getReference().child(User.getUid());
        /*
        udatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(owl_user)){
                    udatabase = FirebaseDatabase.getInstance().getReference().child(User.getUid()).child(owl_user).child("name");
                    udatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.getValue().toString();
                            UserName.setText(name);
                            UserEmail.setText(User.getEmail());
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */

        getMenuInflater().inflate(R.menu.home_activity_with_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            MainFragment fragment = new MainFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fcontainer,fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.Grade_calc) {
            GradeInfoFragment fragment = new GradeInfoFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fcontainer,fragment);
            fragmentTransaction.commit();

        } else if (id == R.id.schedule) {

        } else if (id == R.id.Settings) {

        } else if (id == R.id.About) {

        }else if (id == R.id.Signout){
            finish();
            startActivity(new Intent(homeActivityWithMenu.this,MainActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

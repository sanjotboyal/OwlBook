package com.example.toshiba.firebase_authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.toshiba.firebase_authentication.Western.OWL.CourseEntry;
import com.example.toshiba.firebase_authentication.Western.User;


public class LoadingActivity extends AppCompatActivity {
    private User currUser;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Retrieve User object from bundle.
        if (savedInstanceState == null) {
            bundle = getIntent().getExtras();
            currUser = (User)bundle.get("CURRENT_USER");
        }

        new CourseEntry(currUser, this).execute();

    }
}

package com.example.toshiba.firebase_authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        ImageView gif = (ImageView) findViewById(R.id.GifView);
                Ion.with(gif).load("android.resource://com.example.toshiba.firebase_authentication/" + R.drawable.loader);
    }
}

package com.example.toshiba.firebase_authentication.Western.OWL;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.toshiba.firebase_authentication.MainActivity;
import com.example.toshiba.firebase_authentication.Western.User;
import com.example.toshiba.firebase_authentication.homeActivityWithMenu;
import com.example.toshiba.firebase_authentication.userInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by j3chowdh on 6/23/2017.
 */

public class Owl extends AsyncTask<Void, Void, Boolean> {
    private String user;
    private String pass;

    private Context ctx;
    private ProgressDialog progressDialog;

    private DatabaseReference databaseReference;

    public Owl(String id, String password, Context context) {
        this.user = id;
        this.pass = password;

        this.ctx = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(ctx, "UWO", "Validating...", true);
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        // Owl system connect and login
        try {
            // Performs a connection to OWL
            Connection.Response loginForm = Jsoup.connect("https://owl.uwo.ca/portal").method(Connection.Method.GET).execute();
            Document document = Jsoup.connect("https://owl.uwo.ca/portal/relogin")
                    .data("cookieexists", "false")
                    .data("eid", user)
                    .data("pw", pass)
                    .cookies(loginForm.cookies())
                    .post();

            // Check if user successfully logged in.
            if (document.title().contains("Home")) {
                /* Create instance of User */
               // User user = new User(user, pass);
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Users").child(user).setValue(new User(user, pass));

                Log.d("[Owl -> doInBackground]", "Successfully added account to firebase.");
            } else {
                // ...
                return false;
            }
        } catch (Exception e) {
            Log.d("[Owl -> doInBackground]", "FAILED: " + e.toString());
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean v) {
        super.onPostExecute(v);

        if (v) {
            Toast.makeText(ctx, "Successfully logged in.", Toast.LENGTH_LONG).show();

            // Create an intent to bring up the next activity.
            Intent intent = new Intent(ctx, homeActivityWithMenu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        } else {
            Toast.makeText(ctx, "Invalid Western ID or password.", Toast.LENGTH_LONG).show();
        }

        progressDialog.dismiss();
    }
}

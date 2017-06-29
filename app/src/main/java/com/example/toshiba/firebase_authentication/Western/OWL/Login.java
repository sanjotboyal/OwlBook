package com.example.toshiba.firebase_authentication.Western.OWL;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.toshiba.firebase_authentication.LoadingActivity;
import com.example.toshiba.firebase_authentication.Western.User;
import com.example.toshiba.firebase_authentication.homeActivityWithMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Map;


public class Login extends AsyncTask<Void, Void, User> {
    private String user;
    private String pass;

    private Context ctx;
    private ProgressDialog progressDialog;

    private DatabaseReference databaseReference;

    private Intent intent;

    public boolean logged;

    private User currUser;

    public Login(String id, String password, Context context) {
        this.user = id;
        this.pass = password;

        this.ctx = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(ctx, "UWO", "Validating...", true);
    }

    @Override
    protected User doInBackground(Void... params) {
        // Login system connect and login
        try {
            // Performs a connection to OWL
            //Map<String, String> cookie = Jsoup.connect("https://owl.uwo.ca/portal").method(Connection.Method.GET).execute().cookies();

            final Connection.Response res = Jsoup.connect("https://owl.uwo.ca/portal/relogin")
                    .data("eid", user)
                    .data("pw", pass)
                    .method(Connection.Method.POST)
                    .execute();

            Document document = res.parse();

            /*
            Document document = Jsoup.connect("https://owl.uwo.ca/portal/relogin")
                    .data("cookieexists", "false")
                    .data("eid", user)
                    .data("pw", pass)
                    .post();
                    */

            // Check if user successfully logged in.
            if (document.title().contains("Home")) {
                Log.d("LOGGED IN SUCCESS HOME", "OKAY LOGGED IN LEGGO-FB");
                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                Log.d("LOGGED IN 2222", "FB SUCCESS FOUND");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user).exists()){
                            Log.d("HAS USER FOUND", "TRUE: " +logged);
                            databaseReference = databaseReference.child(user);
                            currUser = dataSnapshot.getValue(User.class);
                            Log.d("CURRENT USER-FB ", "Name of User: " +currUser.getPassword());
                            intent = new Intent(ctx, homeActivityWithMenu.class);
                        }else{
                            Log.d("NOT FOUND USER", "FALSE: " +logged);
                            /* Create instance of User */
                            currUser = new User(user, pass, res.cookies());
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("Users").child(user).setValue(currUser);
                            //currUser.setCookies(cookie);
                            intent = new Intent(ctx, LoadingActivity.class);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Log.d("[Login]", "Successfully added account to firebase. " + document.title());
                return currUser;
            } else {
                // ...
                return null;
            }
        } catch (Exception e) {
            Log.d("[Login]", "FAILED: " + e.toString());
            return null;
        }
        // Return the user variable from above.
    }

    @Override
    protected void onPostExecute(User u) {
        super.onPostExecute(u);

        // A new check if u == null then unable to login else perform next intent

        if (u != null) {
            Toast.makeText(ctx, "Successfully logged in.", Toast.LENGTH_LONG).show();
            Log.d("POST EXECUTE", "TRUE/FALSE: " +logged);

            // Pass User object to new activity.
            intent.putExtra("CURRENT_USER", u);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);


        } else {
            Toast.makeText(ctx, "Invalid Western ID or password.", Toast.LENGTH_LONG).show();
        }

        progressDialog.dismiss();
    }
}

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// TODO: Create a task for executing the check to FireBase, then on success of that call we return the object from doInBackground.

public class Login extends AsyncTask<Void, Void, Boolean> {
    private String user;
    private String pass;

    private Context ctx;
    private ProgressDialog progressDialog;

    private DatabaseReference databaseReference;

    private Intent intent;

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
    protected Boolean doInBackground(Void... params) {
        // Login system connect and login
        try {
            // Performs a connection to OWL
            final Connection.Response res = Jsoup.connect("https://owl.uwo.ca/portal/relogin")
                    .data("eid", user)
                    .data("pw", pass)
                    .method(Connection.Method.POST)
                    .execute();

            Document document = res.parse();

            // Check if user successfully logged in.
            if (document.title().contains("Home")) {
                Log.d("LOGGED IN SUCCESS HOME", "OKAY LOGGED IN LEGGO-FB");

                databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

                final AtomicBoolean done = new AtomicBoolean(false);

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(user).exists()){
                            Log.d("HAS USER FOUND", "TRUE:");
                            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user);

                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    currUser = dataSnapshot.getValue(User.class);
                                    Log.d("CURRENT USER-FB ", "Name of User: " + currUser.getName());
                                    done.set(true);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                            intent = new Intent(ctx, homeActivityWithMenu.class);
                        } else {
                            Log.d("NOT FOUND USER", "FALSE:");
                            /* Create instance of User */
                            currUser = new User(user, pass, res.cookies());
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.child("Users").child(user).setValue(currUser);

                            intent = new Intent(ctx, LoadingActivity.class);
                            done.set(true);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                while (!done.get()) {
                    //... xd
                }
                Log.d("[", "Successfully added Login]account to firebase. " + currUser.getPassword());
                return true;
            } else {
                // ...
                return false;
            }
        } catch (Exception e) {
            Log.d("[Login]", "FAILED: " + e.toString());
            return false;
        }
        // Return the user variable from above.
    }

    @Override
    protected void onPostExecute(Boolean b) {
        super.onPostExecute(b);

        // A new check if u == null then unable to login else perform next intent

        if (b) {
            Toast.makeText(ctx, "Successfully logged in.", Toast.LENGTH_LONG).show();
            Log.d("POST EXECUTE", "TRUE/FALSE: ");

            // Pass User object to new activity.
            intent.putExtra("CURRENT_USER", currUser);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);


        } else {
            Toast.makeText(ctx, "Invalid Western ID or password.", Toast.LENGTH_LONG).show();
        }

        progressDialog.dismiss();
    }
}

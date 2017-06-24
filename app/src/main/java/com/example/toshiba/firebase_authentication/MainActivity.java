package com.example.toshiba.firebase_authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import android.widget.*;

import com.example.toshiba.firebase_authentication.Western.OWL.Owl;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.w3c.dom.Text;

import static com.example.toshiba.firebase_authentication.R.id.login;

public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private Button login_btn;

    private ImageView westernlogo;

   // private ProgressDialog progressDialog;

    private FirebaseAuth auth;
    private DatabaseReference udatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize progress bar.
       // progressDialog = new ProgressDialog(this);

        // Initalize Firebase Authentication.
        auth = FirebaseAuth.getInstance();

        // Check if user is already logged in.
        if (auth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this,homeActivityWithMenu.class));
        }

        /* Setup login page. */
        email = (EditText) findViewById(R.id.email_box);
        password = (EditText) findViewById(R.id.password_box);
        login_btn = (Button) findViewById(R.id.login_btn);

        westernlogo = (ImageView) findViewById(R.id.logo);
        westernlogo.setImageResource(R.drawable.logo);

        // Login Button OnClickListener.
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Robust check.
                String user = email.getText().toString().trim();
                String pass = password.getText().toString();

                // Check if valid ID.
                if (user.contains("@")){
                    Toast.makeText(MainActivity.this, "Please enter only your Western ID!", Toast.LENGTH_LONG).show();
                    email.setText("");
                    return;
                } else if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)) {
                    Toast.makeText(MainActivity.this, "Please enter a Western valid ID and/or password.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Perform login.
                Login(user, pass);
            }
        });
    }

    // Login method.
    private void Login(String id, String pass) {
        //if everythings entered: show progress for registering
     //   progressDialog.setMessage("Logging In....");
     //   progressDialog.show();

        //Executes a background async task for owl jsoup connection verification
        new Owl(id, pass, this).execute();
    }
}

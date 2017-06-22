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
    private ProgressDialog progressDialog;

    private FirebaseAuth auth;
    private DatabaseReference udatabase;

    //Login User function
    private void loginUser(){

        final String User_email = email.getText().toString().trim();

        //Verifies if entries are legitimate
        if(!User_email.contains("@uwo.ca")){
            Toast.makeText(this,"Please entire a valid Western Email and Password",Toast.LENGTH_LONG).show();
            email.setText("");
            return;
        }

        String User_password = password.getText().toString().trim();



        //Checks if its empty and drops a toast notification
        if (TextUtils.isEmpty(User_email)){
            Toast.makeText(this,"Please Enter your Email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(User_password)){
            Toast.makeText(this,"Please Enter your Password", Toast.LENGTH_LONG).show();
            return;
        }



        //if everythings entered: show progress for registering
        progressDialog.setMessage("Logging In....");
        progressDialog.show();

        //Executes a background async task for owl jsoup connection verification
        new Jsoups().execute();

        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //progress bar
        progressDialog = new ProgressDialog(this);

        //auth firebase user
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null){
            finish();
            //startActivity(new Intent(this,homeActivity.class));
            startActivity(new Intent(this,homeActivityWithMenu.class));
        }

        //Setup Page
        email = (EditText) findViewById(R.id.email_box);
        password = (EditText) findViewById(R.id.password_box);
        login_btn = (Button) findViewById(R.id.login_btn);

        westernlogo = (ImageView) findViewById(R.id.logo);
        westernlogo.setImageResource(R.drawable.logo);

        //Login Function onclick activity
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    //Background Task

    //User Login With FireBase
    public class Jsoups extends AsyncTask<Void,Void,Void> {

        //Trim email and required information
        final String User_email = email.getText().toString().trim();

        int end=User_email.indexOf("@");
        String owl_user = User_email.substring(0,end);

        final String User_password = password.getText().toString().trim();

        String title;

        //In Background task
        @Override
        protected Void doInBackground(Void... params) {
            //owl system connect and login
                try{
                    //Performs a connection to OWL
                        Connection.Response loginForm = Jsoup.connect("https://owl.uwo.ca/portal").method(Connection.Method.GET).execute();
                        Document document = Jsoup.connect("https://owl.uwo.ca/portal/relogin").data("cookieexists", "false").data("eid", owl_user).data("pw", User_password).cookies(loginForm.cookies()).post();
                        title = document.title();

                }catch(Exception e){
                }
            return null;
        }

        @Override

        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //Checks if login worked
            //After that is successful on post execute it checks if the title contains home so OWL login is passed
            if(title.contains("Home")){
                progressDialog.dismiss();

                //Creates user first time:: this fails if user exists so it goes to the else
                auth.createUserWithEmailAndPassword(User_email, User_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser User = auth.getCurrentUser();

                            //real time data base and info-set
                            udatabase = FirebaseDatabase.getInstance().getReference();
                            userInfo userinfo = new userInfo(User_password,owl_user);
                            udatabase.child(User.getUid()).setValue(userinfo);
                            userinfo = null;

                            Toast.makeText(MainActivity.this, "Successfully Logged In", Toast.LENGTH_LONG).show();
                            finish();
                            //startActivity(new Intent(MainActivity.this, homeActivity.class));
                            startActivity(new Intent(MainActivity.this,homeActivityWithMenu.class));

                        } else {
                            //Signs in pre-existing user
                            auth.signInWithEmailAndPassword(User_email, User_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(MainActivity.this, "Successfully Logged In", Toast.LENGTH_LONG).show();
                                        //direct to login page
                                        finish();
                                        //startActivity(new Intent(MainActivity.this, homeActivity.class));
                                        startActivity(new Intent(MainActivity.this,homeActivityWithMenu.class));
                                    } else {
                                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    }
                });


            }else{
                Toast.makeText(MainActivity.this,"Authorization Failed!",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                email.setText("");
                password.setText("");
            }
        }
    }
}

package com.example.toshiba.firebase_authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//Implementing method for onclick listeners allows use of 'this' <keyword for onclicks instead of setting onclick listener individually for btns
public class LoginsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText login_email;
    private EditText login_password;
    private Button login_btn;
    private TextView signup;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logins);

        //connect to db
        auth = FirebaseAuth.getInstance();

        //initiate progressBar
        progressDialog = new ProgressDialog(this);

        //check if logged in from prior session
        if (auth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this,ProfileActivity.class));
        }

        login_email = (EditText) findViewById(R.id.login_emailbox);
        login_password= (EditText) findViewById(R.id.login_passwordbox);
        login_btn = (Button) findViewById(R.id.login_btn);
        signup = (TextView) findViewById(R.id.SignUp);

        //after implementing view onclick and overwrite method
        login_btn.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == login_btn){
            userLogin();
        }
        if (view == signup){
            //changing to signup page (current activity 'this' context, 'page activity togoto')
            finish(); //closes this page before opening other...
            startActivity(new Intent(this,MainActivity.class));
        }

    }
    public void userLogin(){
        String User_email = login_email.getText().toString().trim();
        String User_password = login_password.getText().toString().trim();

        //Checks if its empty and drops a toast notification
        if (TextUtils.isEmpty(User_email)){
            Toast.makeText(this,"Please Enter your Email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(User_password)){
            Toast.makeText(this,"Please Enter your Password", Toast.LENGTH_LONG).show();
        }


        //if everythings entered: show progress for registering
        progressDialog.setMessage("Logging in....");
        progressDialog.show();

        auth.signInWithEmailAndPassword(User_email,User_password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if (task.isSuccessful()){
                    Toast.makeText(LoginsActivity.this,"You have logged in Successfully", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(LoginsActivity.this,ProfileActivity.class));
                }else{
                    Toast.makeText(LoginsActivity.this,"Incorrect Username or Password", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}

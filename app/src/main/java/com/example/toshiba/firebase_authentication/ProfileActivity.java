package com.example.toshiba.firebase_authentication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private Button logout_btn;
    private Button jsoup_btn;
    private FirebaseAuth auth;

    private ListView view_list;
    private ArrayList<String> news = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        view_list = (ListView) findViewById(R.id.list_headlines);

        logout_btn = (Button) findViewById(R.id.logout_btn);

        jsoup_btn = (Button) findViewById(R.id.jsoup_btn);

        //connect db
        auth = FirebaseAuth.getInstance();

        //retrieve class call to scrape website
        new retrieve().execute();



        //checks for user login instance
        if(auth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,ProfileActivity.class));
        }

        //Create User instance if user is logged in
        FirebaseUser User = auth.getCurrentUser();
        Toast.makeText(this,"Welcome " +User.getEmail(), Toast.LENGTH_LONG).show();

        logout_btn.setOnClickListener(this);
        jsoup_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v==logout_btn){
            auth.signOut();
            finish();
            startActivity(new Intent(ProfileActivity.this,LoginsActivity.class));
        }
        if (v == jsoup_btn){
            finish();
            startActivity(new Intent(ProfileActivity.this,jsoup.class));
        }
    }

    //Async Task:: Runs on a background thread:: useful for short operations
    public class retrieve extends AsyncTask<Void,Void,Void>{


        @Override
        protected Void doInBackground(Void... params) {
            try {
                //connect to URL to scrape from
                Document doc = Jsoup.connect("https://news.google.com/news/section?cf=all&pz=1&ned=ca&geo=detect_metro_area&siidp=6fcf9326db9c9bd0e1be74acc7affc44b5cc&ict=ln").get();
                String title = doc.title();



                Elements news_headlines = doc.select(".titletext");

                for (Element news_headline: news_headlines) {
                    String newsline = news_headline.html();
                    news.add(newsline);
                }

                //looking for links on the site
               // Elements links = doc.select("");

            }catch(Exception e){
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            final ArrayAdapter<String> arrayAdapted = new ArrayAdapter<String>(ProfileActivity.this,android.R.layout.simple_list_item_1,news);
            view_list.setAdapter(arrayAdapted);
        }
    }
}

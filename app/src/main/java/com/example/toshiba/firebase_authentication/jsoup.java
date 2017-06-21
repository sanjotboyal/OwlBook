package com.example.toshiba.firebase_authentication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class jsoup extends AppCompatActivity {

    private ListView assignments;
    private ListView marks;
    private ArrayList<String> assignments_array = new ArrayList<>();
    private ArrayList<String> marks_array = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsoup);

        assignments = (ListView) findViewById(R.id.assignments);
        marks = (ListView) findViewById(R.id.marks);

        new Jsoups().execute();
    }

    public class Jsoups extends AsyncTask<Void,Void,Void>{

        String title;
        @Override
        protected Void doInBackground(Void... params) {

            try {
                //Authentication login using POST

                //parses from specific page >> cookies from this page
                Connection.Response loginForm = Jsoup.connect("https://owl.uwo.ca/portal/tool/d88b8cbe-1172-4ca5-8396-60b0ac3d2e7b/studentView.jsf").method(Connection.Method.GET).execute();

                //overall connection to login & authenticate with username and password
                Document document = Jsoup.connect("https://owl.uwo.ca/portal/relogin").data("cookieexists","false").data("eid","Sboyal2").data("pw","Lockerz12!").cookies(loginForm.cookies()).post();

                title = document.title();

                //find the elements for marks
                Elements assignments = document.select(".left");
                Elements marks = document.select(".center");

                //added assignment and marks to dynamic array after loop scraping the entire page

                for (int i=1; i<assignments.size();i++){
                    Element assignment = assignments.get(i);
                    assignments_array.add(assignment.html());
                }

                for(int i=4; i<marks.size();i++){
                    Element mark = marks.get(i);
                    String check = mark.html();

                    if (check.contains("<sp")){
                        int beg = mark.html().indexOf("(");
                        int end = mark.html().indexOf(")");
                        String marks_parse = mark.html().substring(beg+1,end);
                        marks_array.add(marks_parse);

                    }else {
                        marks_array.add(mark.html());
                    }
                    i +=2;
                }



            }catch(Exception e){
                Log.d("jsoup", "sanjot is garbage");
                Log.d("jsoup2", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setTitle(title);

            final ArrayAdapter<String> arrayAssignments = new ArrayAdapter<String>(jsoup.this,android.R.layout.simple_list_item_1,assignments_array);
            assignments.setAdapter(arrayAssignments);

            final ArrayAdapter<String> arrayMarks = new ArrayAdapter<String>(jsoup.this,android.R.layout.simple_list_item_1,marks_array);
            marks.setAdapter(arrayMarks);

        }
    }
}

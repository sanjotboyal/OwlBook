package com.example.toshiba.firebase_authentication.Western.OWL;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.toshiba.firebase_authentication.Western.Course;
import com.example.toshiba.firebase_authentication.Western.User;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import static android.R.id.list;

/**
 * Created by Toshiba on 2017-07-15.
 */

public class AverageCalculation extends AsyncTask<Void,Void,Void> {
    private Course currCourse;
    private LinkedHashMap<String,ArrayList<String>>  listofGrades= new LinkedHashMap<>();

    // FireBase DB reference
    private DatabaseReference databaseReference;

    public AverageCalculation(Course currCourse) {
        this.currCourse = currCourse;
        Log.d("Course rn: " + currCourse.getname(), "Credit: " + currCourse.getCredit());
    }

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<String> othergrades = new ArrayList<>();

        for(int i=0; i<currCourse.Assignments.size();i++){
            Iterator iterator = currCourse.Breakdown.keySet().iterator();

            while(iterator.hasNext()){
                String check = (String) iterator.next();
                if(currCourse.Assignments.get("A"+i).contains(check)){
                    if(listofGrades.containsKey(check)){
                        ArrayList<String> temp = listofGrades.get(check);
                        temp.add((currCourse.Assignments.get("A"+i)));
                        listofGrades.put(check,temp);
                        Log.d("Found Type: " + check, "Assignment was: " + temp.get(0) + ":" + temp.get(1));
                        temp.clear();
                    }else {
                        ArrayList<String> temps = new ArrayList<>();
                        temps.add((currCourse.Assignments.get("A" + i)));
                        listofGrades.put(check, temps);
                        Log.d("Found Type: " + check, "Assignment was: " + temps.get(0));
                        temps.clear();
                    }
                }else{
                    othergrades.add(currCourse.Assignments.get("A"+i));
                    listofGrades.put("Uncategorized",othergrades);
                }
            }
        }
        //apply value of such key to the mark value of grade hashmap

        //get average of course overall
        //set to course value

        //update that branch of firebase

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
}

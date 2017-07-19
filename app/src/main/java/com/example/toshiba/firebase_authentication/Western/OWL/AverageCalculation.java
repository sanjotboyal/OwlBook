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

import static android.R.attr.value;
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
        boolean uncategorized = true;

        ArrayList<String> temps = new ArrayList<>();

        for(int i=0; i<currCourse.Assignments.size();i++){
            Iterator iterator = currCourse.Breakdown.keySet().iterator();

            while(iterator.hasNext()){
                String check = (String) iterator.next();
                Log.d("TYPE CHECK: ", "okay: " + check);
                if(currCourse.Assignments.get("A"+i).toUpperCase().contains(check.toUpperCase())){
                    Log.d("FOUND: " +check, "Assignment is: " + currCourse.Assignments.get("A"+i));
                    uncategorized = false;

                    if(listofGrades.containsKey(check.toUpperCase())){
                        Log.d("Hashmap exsists!! for: " +check, "Size is: " + listofGrades.size());
                        temps = listofGrades.get(check.toUpperCase());
                        temps.add(currCourse.Assignments.get("A"+i));
                        listofGrades.put(check.toUpperCase(),temps);
                        temps = new ArrayList<>();
                    }else{
                        temps.add(currCourse.Assignments.get("A"+i));
                        listofGrades.put(check.toUpperCase(),temps);
                        temps = new ArrayList<>();
                    }

                   /* if(listofGrades.containsKey(check.toUpperCase())){
                        Log.d("FOUND: " +check, "Value is: " + listofGrades.get(check.toUpperCase()).get(0) + ": size: " + listofGrades.size());
                        ArrayList<String> temp = listofGrades.get(check.toUpperCase());
                        temp.add((currCourse.Assignments.get("A"+i)));
                        Log.d("Adding: " + currCourse.Assignments.get("A"+i) , "Temp size: " + temp.size());

                        listofGrades.put(check.toUpperCase(),temp);
                       // Log.d("Found Type: " + check, "Assignment was: " + temp.get(0));
                        temp.clear();
                    }else {*/
                       // Log.d("Found Type: " + check, "Assignment was: " + temps.get(0));
                        //temps.clear();

                   // }
                }
                Log.d("AFTER LOOP:", "BOOLEAN UNCAT : " + uncategorized);
            }if(uncategorized){
                    othergrades.add(currCourse.Assignments.get("A"+i));
                    listofGrades.put("Uncategorized",othergrades);
            }
        }

        Iterator myVeryOwnIterator = listofGrades.keySet().iterator();
        while(myVeryOwnIterator.hasNext()) {
            String key = (String) myVeryOwnIterator.next();
            for(int i= 0; i<listofGrades.get(key).size();i++){
                String value = listofGrades.get(key).get(i);
                Log.d("Linked List: " + key + ":" + i, "value is: " + value);
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

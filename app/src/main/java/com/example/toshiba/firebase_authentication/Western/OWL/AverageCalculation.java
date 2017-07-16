package com.example.toshiba.firebase_authentication.Western.OWL;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.toshiba.firebase_authentication.Western.Course;
import com.example.toshiba.firebase_authentication.Western.User;
import com.google.firebase.database.DatabaseReference;

/**
 * Created by Toshiba on 2017-07-15.
 */

public class AverageCalculation extends AsyncTask<Void,Void,Void> {
    private Course currCourse;

    // FireBase DB reference
    private DatabaseReference databaseReference;
    private Context ctx;

    public AverageCalculation(Course currCourse, Context ctx) {
        this.currCourse = currCourse;
        this.ctx = ctx;
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Loop through hashmap: look for tasks key:: if matched add to arraylist that corresponds with key (quiz)
        //apply value of such key to the mark value of grade hashmap

        //get average of course overall
        //set to course value

        //update that branch of firebase



        return null;
    }
}

package com.example.toshiba.firebase_authentication.Western.OWL;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.toshiba.firebase_authentication.Western.Course;
import com.example.toshiba.firebase_authentication.Western.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import static android.R.attr.data;
import static android.R.attr.key;
import static android.R.attr.value;
import static android.R.id.list;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Toshiba on 2017-07-15.
 */

public class AverageCalculation extends AsyncTask<Void,Void,Void> {
    // FireBase DB reference
    private DatabaseReference databaseReference;

    private Course currCourse;
    private String UserID;
    private int pos;

    public AverageCalculation(Course currCourse, String UserID, int pos) {
        this.currCourse = currCourse;
        this.UserID = UserID;
        this.pos = pos;
        Log.d("Course rn: " + currCourse.getname(), "Credit: " + currCourse.getCredit());
    }

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<String> othergrades = new ArrayList<>();
        boolean uncategorized = true;

        ArrayList<String> temps = new ArrayList<>();

        for(int i=0; i<currCourse.Assignments.size();i++){
            uncategorized = true;

            Iterator iterator = currCourse.Breakdown.keySet().iterator();

            while(iterator.hasNext()){
                String check = (String) iterator.next();
                Log.d("TYPE CHECK: ", "okay: " + check);
                if(currCourse.Assignments.get("A"+i).toUpperCase().contains(check.toUpperCase())){
                    Log.d("FOUND: " +check, "Assignment is: " + currCourse.Assignments.get("A"+i));
                    uncategorized = false;

                    if(currCourse.listofGrades.containsKey(check.toUpperCase())){
                        Log.d("Hashmap exsists!! for: " +check, "Size is: " + currCourse.listofGrades.size());
                        temps = currCourse.listofGrades.get(check.toUpperCase());
                        temps.add(currCourse.Assignments.get("A"+i));
                        currCourse.addCategoryAssignments(check.toUpperCase(),temps);
                        temps = new ArrayList<>();
                    }else{
                        temps.add(currCourse.Assignments.get("A"+i));
                        currCourse.addCategoryAssignments(check.toUpperCase(),temps);
                        temps = new ArrayList<>();
                    }
                }
                Log.d("AFTER LOOP:", "BOOLEAN UNCAT : " + uncategorized);
            }if(uncategorized){
                    Log.d("UNCATEGORIZED ARE HERE:", "ASSIGNMENT UNCAT: " + currCourse.Assignments.get("A"+i));
                    othergrades.add(currCourse.Assignments.get("A"+i));
                    currCourse.addCategoryAssignments("UNCATEGORIZED",othergrades);
            }
        }

        Iterator myVeryOwnIterator = currCourse.listofGrades.keySet().iterator();

        double overallSum = 0;
        double perfectSum = 0;
        while(myVeryOwnIterator.hasNext()) {
            String key = (String) myVeryOwnIterator.next();
            double markAve = 0;
            for(int i= 0; i<currCourse.listofGrades.get(key).size();i++){

                String value = currCourse.listofGrades.get(key).get(i);
                Log.d("BEFORE WE PARSE WE GOT:", "ASSIGNMENT: " + value);

                try{
                Fraction MarkFraction = Fraction.getFraction(value.substring(value.indexOf(":")+1));
                Double Mark = MarkFraction.doubleValue();
                Log.d("LETS SEE:", "MARK : " + Mark);
                markAve += (Mark*100);

                } catch (Exception e){
                    Log.d("EXCEPTION THO: ", "HERE: " +e.toString());

                    String mark = value.substring(value.indexOf((":") + 1));
                    double numerator = Double.parseDouble(mark.substring(0, mark.indexOf("/")));
                    double denominator = Double.parseDouble(mark.substring(mark.indexOf("/") + 1));
                    double Mark = numerator / denominator;
                    Log.d("LETS SEE:", "MARK : " + Mark);
                    markAve += (Mark * 100);
                }
            }
            double criteriaAve = (markAve/currCourse.listofGrades.get(key).size());
            Log.d("Average of :" + key, "Average: " + criteriaAve);

            if(!(key.contains("UNCATEGORIZED"))) {
                Log.d("KEY: " + key, "value is: " + currCourse.Breakdown.get(key.toUpperCase()));
                double weightedAve = criteriaAve * ((Double.parseDouble(currCourse.Breakdown.get(key.toUpperCase())))/100);
                perfectSum += Double.parseDouble(currCourse.Breakdown.get(key.toUpperCase()));
                overallSum += weightedAve;
            }
        }
        double currAverage;
        if(overallSum != 0) {
            currAverage = (overallSum / perfectSum) * 100;
            Log.d("YOUR AVERAGE :", "AVERAGE IS : " + currAverage);
        }else{
            currAverage = 0;
        }

        currCourse.setCurrAverage(currAverage);

        //update that branch of firebase
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(UserID).child("userCourseList").child(Integer.toString(pos));
        Log.d("CONNECTED TO FIREBASE:", "CONNECTED AND SETTING");
        databaseReference.setValue(currCourse);
        Log.d("CONNECTED TO FIREBASE:", "DONE UPDATING");

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}

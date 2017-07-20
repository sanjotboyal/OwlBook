package com.example.toshiba.firebase_authentication.Western.OWL;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.toshiba.firebase_authentication.Western.Course;
import com.example.toshiba.firebase_authentication.Western.User;
import com.google.firebase.database.DatabaseReference;

import org.apache.commons.lang3.math.Fraction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import static android.R.attr.value;
import static android.R.id.list;
import static android.os.Build.VERSION_CODES.M;

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
                }
                Log.d("AFTER LOOP:", "BOOLEAN UNCAT : " + uncategorized);
            }if(uncategorized){
                    othergrades.add(currCourse.Assignments.get("A"+i));
                    listofGrades.put("UNCATEGORIZED",othergrades);
            }
        }

        Iterator myVeryOwnIterator = listofGrades.keySet().iterator();

        double overallSum = 0;
        double perfectSum = 0;
        while(myVeryOwnIterator.hasNext()) {
            String key = (String) myVeryOwnIterator.next();
            double markAve = 0;
            for(int i= 0; i<listofGrades.get(key).size();i++){

                String value = listofGrades.get(key).get(i);
                Log.d("BEFORE WE PARSE WE GOT:", "ASSIGNMENT: " + value);

                try{
                Fraction MarkFraction = Fraction.getFraction(value.substring(value.indexOf(":")+1));
                Double Mark = MarkFraction.doubleValue();
                Log.d("LETS SEE:", "MARK : " + Mark);
                markAve += (Mark*100);

                } catch (Exception e){
                    String mark = value.substring(value.indexOf((":")+1));
                    double numerator = Double.parseDouble(mark.substring(0,mark.indexOf("/")));
                    double denominator = Double.parseDouble(mark.substring(mark.indexOf("/")+1));
                    double Mark = numerator/denominator;
                    Log.d("LETS SEE:", "MARK : " + Mark);
                    markAve += (Mark*100);
                }
            }
            double criteriaAve = (markAve/listofGrades.get(key).size());
            Log.d("Average of :" + key, "Average: " + criteriaAve);

            if(!(key.contains("UNCATEGORIZED"))) {
                Log.d("KEY: " + key, "value is: " + currCourse.Breakdown.get(key.toUpperCase()));
                double weightedAve = criteriaAve * ((Double.parseDouble(currCourse.Breakdown.get(key.toUpperCase())))/100);
                perfectSum += Double.parseDouble(currCourse.Breakdown.get(key.toUpperCase()));
                overallSum += weightedAve;
            }
        }
        double currAverage = (overallSum/perfectSum)*100;
        Log.d("YOUR AVERAGE :", "AVERAGE IS : " + currAverage);

        //update that branch of firebase

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

    }
}

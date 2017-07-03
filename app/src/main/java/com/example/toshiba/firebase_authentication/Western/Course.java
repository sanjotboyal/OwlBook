package com.example.toshiba.firebase_authentication.Western;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 *
 */

public class Course implements Parcelable {
    private String name;
    private String base_url;
    private String gradebook_URL;
    private String currAverage;

    public HashMap<String,String> Assignments = new HashMap<>();

    public Course(String name, String base_url){
        this.name = name;
        this.base_url = base_url;
        this.gradebook_URL = "";
        this.currAverage = "0%";
    }

    public Course() {

    }

    public void setGradebook_URL(String URL) {
        gradebook_URL = URL;
    }
    public String getGradebook_URL() {
        return gradebook_URL;
    }

    public String getname() {
        return name;
    }

    public String getbase_url() {
        return base_url;
    }

    public String getCurrAverage() {
        return currAverage;
    }

    public void setCurrAverage(String currAverage) {
        this.currAverage = currAverage;
    }

    public void addAssignment(String Assignment, String Grade) {
        Assignments.put(Assignment,Grade);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Course> CREATOR = new Parcelable.Creator<Course>() {
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    private Course(Parcel in) {

    }
}

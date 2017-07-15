package com.example.toshiba.firebase_authentication.Western;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 */

public class Course implements Parcelable {
    private String name;
    private String section;
    private double credit;
    private String teacher;
    private String base_url;
    private String gradebook_URL;
    private String currAverage;

    public Map<String,String> Assignments = new LinkedHashMap<>();
    public Map<String,String> Breakdown = new LinkedHashMap<>();


    public Course(String name, String section,String base_url){
        this.name = name;
        this.section = section;
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

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public String getCurrAverage() {
        return currAverage;
    }

    public void setCurrAverage(String currAverage) {
        this.currAverage = currAverage;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void addAssignment(String Assignment, String Grade) {
        Assignments.put(Assignment,Grade);
    }

    public void addCriteria(String Criteria,String Value){
        Breakdown.put(Criteria,Value);
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
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(name);
        out.writeString(teacher);
        out.writeString(section);
        out.writeString(base_url);
        out.writeString(gradebook_URL);
        out.writeString(currAverage);
        out.writeMap(Assignments);
        out.writeMap(Breakdown);
    }

    private Course(Parcel in) {
        this.name=in.readString();
        this.teacher = in.readString();
        this.section = in.readString();
        this.base_url = in.readString();
        this.gradebook_URL = in.readString();
        this.currAverage = in.readString();
        Assignments = new LinkedHashMap<>();
        in.readMap(Assignments,String.class.getClassLoader());

        Breakdown = new LinkedHashMap<>();
        in.readMap(Breakdown,String.class.getClassLoader());
    }
}

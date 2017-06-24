package com.example.toshiba.firebase_authentication.Western;

import com.example.toshiba.firebase_authentication.courses;

import java.util.ArrayList;

/**
 *
 */

public class User {
    private String password;
    private String UserID;
    private String name;
    private ArrayList<courses> UserCourseList;

    public User(String UserID, String password){
        this.password = password;
        this.UserID = UserID;

        UserCourseList = new ArrayList<>();
    }

    public User(){
        UserCourseList = new ArrayList<>();
    }

    public void addCourse(courses course){
        UserCourseList.add(course);
    }

    public void setUserID(String UserID){
        this.UserID = UserID;
    }
    public String getUserID(){
        return UserID;
    }

    public String getPassword(){
        return password;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public courses getCourse(int index) {
        return UserCourseList.get(index);
    }
}

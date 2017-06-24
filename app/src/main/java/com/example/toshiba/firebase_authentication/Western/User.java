package com.example.toshiba.firebase_authentication.Western;

import com.example.toshiba.firebase_authentication.courses;

import java.util.ArrayList;

/**
 *
 */

public class User {
    private String password;
    private String id;
    private String name;
    private ArrayList<courses> UserCourseList;

    public User(String UserID, String password){
        this.password = password;
        this.id = UserID;

        UserCourseList = new ArrayList<>();
    }

    public User(){
        UserCourseList = new ArrayList<>();
    }

    public void addCourse(courses course){
        UserCourseList.add(course);
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
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

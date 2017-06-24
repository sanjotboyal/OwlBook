package com.example.toshiba.firebase_authentication;

import java.util.ArrayList;

/**
 * Created by Toshiba on 2017-06-16.
 */


public class userInfo {
    private String password;
    private String UserID;
    private String name;

    public ArrayList<courses> UserCourseList = new ArrayList<>();


    public userInfo(String password){
        this.password=password;
    }

    public userInfo(){

    }

    public userInfo(String password,String UserID){
        this.password=password;
        this.UserID = UserID;
    }

    public void addCourse(courses course){
        UserCourseList.add(course);
    }

    public void setUserID(String UserID){
        this.UserID = UserID;
    }

    public String getPassword(){
        return password;
    }

    public String getUserID(){
        return UserID;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

}

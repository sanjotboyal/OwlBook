package com.example.toshiba.firebase_authentication.Western;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.toshiba.firebase_authentication.courses;

import java.util.ArrayList;
import java.util.Map;

/**
 * TODO: Cookie only contains two strings, parse cookie by using following names:
 * JSESSIONID
 * NSC_pxm.vxp.db--443
 */


public class User implements Parcelable {
    private String id;
    private String password;

    private String name;
    private String email;

    // Save cookies to user.
    private Map<String,String> cookies;

    private ArrayList<courses> UserCourseList;

    public User(String UserID, String password){
        this.id = UserID;
        this.password = password;

        //this.cookies = cookies;

        this.email = UserID.concat("@uwo.ca");

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

    public String getEmail() {
        return email;
    }

    public void setCookies(Map<String, String> cookies) {
        this. cookies = cookies;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public courses getCourse(int index) {
        return UserCourseList.get(index);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Fields that are parsed in Parcel.
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(id);
        out.writeString(password);

        out.writeString(email);
        out.writeMap(cookies);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private User(Parcel in) {
        this.id = in.readString();
        this.password = in.readString();

        this.email = in.readString();

        //in.readMap(cookies, getClass().getClassLoader());
    }
}

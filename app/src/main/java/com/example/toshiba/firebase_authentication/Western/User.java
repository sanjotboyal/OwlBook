package com.example.toshiba.firebase_authentication.Western;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * TODO: Cookie only contains two strings, parse cookie by using following names:
 * JSESSIONID
 * NSC_pxm.vxp.db--443 WHAT SHOULD WE CALL THIS
 */


public class User implements Parcelable {
    private String id;
    private String password;

    private String name;
    private String email;



    // Save cookies to user.
    private final String COOKIE_SESSION_ID = "JSESSIONID";
    private final String COOKIE_ENCRYPYION = "NSC_pxm.vxp.db--443";

    // hmm...
    private Map<String,String> cookies;

    private List<Course> UserCourseList;

    public User(String UserID, String password, Map<String, String> cookies){
        this.id = UserID;
        this.password = password;

        //this.cookies = cookies;

        this.email = UserID.concat("@uwo.ca");

        this.cookies = cookies;

        // Firebase restrictions on '.'.
        cookies.put( "NSC_pxm+vxp+db--443", cookies.remove( "NSC_pxm.vxp.db--443" ));

        UserCourseList = new ArrayList<>();
    }

    // hmm...
    public User() {
        //
    }

    /* Setters and getters */

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setEmail(String email) {
        this.email = email;
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

    public void setUserCourseList(List<Course> userCourseList) {
        UserCourseList = userCourseList;
    }

    public List<Course> getUserCourseList() {
        return UserCourseList;
    }

    /* Public methods */
    public void addCourse(Course course){
        UserCourseList.add(course);
    }

    public Course getCourse(int index) {
        return UserCourseList.get(index);
    }

    public void FirebaseFix() {
        cookies.put( "NSC_pxm+vxp+db--443", cookies.remove( "NSC_pxm.vxp.db--443" ));
    }

    public String getCOOKIE_SESSION_ID() {
        return COOKIE_SESSION_ID;
    }

    public String getCOOKIE_ENCRYPYION() {
        return COOKIE_ENCRYPYION;
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
        out.writeString(name);
        out.writeString(email);
        out.writeTypedList(UserCourseList);

        for(Map.Entry<String,String> entry : cookies.entrySet()){
            //out.writeString(entry.getKey());
            out.writeString(entry.getValue());
        }

        //out.writeMap(cookies);
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
        this.name = in.readString();
        this.email = in.readString();
        in.readTypedList(UserCourseList, Course.CREATOR);

        // Retrieve cookies...
        cookies = new HashMap<>();
        cookies.put(COOKIE_SESSION_ID, in.readString());
        cookies.put(COOKIE_ENCRYPYION, in.readString());
    }
}

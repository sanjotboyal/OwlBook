package com.example.toshiba.firebase_authentication.Western;

import java.util.HashMap;

/**
 *
 */

public class Course {
    private String name;
    private String base_url;
    private String gradebook_URL;

    public HashMap<String,String> Assignments = new HashMap<>();

    public Course(String name, String base_url){
        this.name = name;
        this.base_url = base_url;
        this.gradebook_URL = "";
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

    public void addAssignment(String Assignment, String Grade) {
        Assignments.put(Assignment,Grade);
    }
}

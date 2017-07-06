package com.example.toshiba.firebase_authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.toshiba.firebase_authentication.Western.Course;
import com.example.toshiba.firebase_authentication.Western.User;

public class CourseActivity extends AppCompatActivity {
    private Course currCourse;
    private Bundle bundle;
    private TextView courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        if (savedInstanceState == null) {
            bundle = getIntent().getExtras();
            currCourse = (Course)bundle.get("CURRENT_COURSE");
            Log.d("COURSE Activity", "Current Course:" + currCourse.getname());
        }
        courseName = (TextView) findViewById(R.id.textView);
        courseName.setText(currCourse.getname());

    }
}

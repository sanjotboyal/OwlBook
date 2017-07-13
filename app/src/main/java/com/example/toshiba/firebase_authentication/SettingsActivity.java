package com.example.toshiba.firebase_authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.toshiba.firebase_authentication.Western.User;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends AppCompatActivity {

    private Bundle bundle;
    private EditText Quiz_value;
    private EditText Labs_value;
    private EditText Midterm_value;
    private EditText FinalExam_value;
    private User currUser;

    private Button AddCriteria;
    private Button Save;


    private Spinner spinner;
    private ArrayList<String> courses = new ArrayList<>();

    private List<EditText> allEds = new ArrayList<>();
    EditText ed;
    EditText edval;
    ScrollView scrollLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null) {
            bundle = getIntent().getExtras();
            currUser = (User)bundle.get("CURRENT_USER");
            Log.d("homeActivityWithMenu", "user id:" + currUser.getId());
        }

        spinner = (Spinner) findViewById(R.id.coursesView);

        for(int i=0; i<currUser.getUserCourseList().size();i++){
            courses.add(currUser.getUserCourseList().get(i).getname());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        scrollLayout = (ScrollView) findViewById(R.id.scroll);


        Quiz_value = (EditText) findViewById(R.id.QuizValue);
        Labs_value = (EditText) findViewById(R.id.LabsValue);
        Midterm_value = (EditText) findViewById(R.id.MidtermValue);
        FinalExam_value = (EditText) findViewById(R.id.FinalExamValue);

        AddCriteria = (Button) findViewById(R.id.addcriteria);
        Save = (Button) findViewById(R.id.save_btn);

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = spinner.getSelectedItemPosition();
                Toast.makeText(SettingsActivity.this,"Selected: " + pos, Toast.LENGTH_LONG).show();
            }
        });




        final LinearLayout parentView = (LinearLayout) findViewById(R.id.breakdown);
        AddCriteria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout criteria = new LinearLayout(SettingsActivity.this);
                criteria.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                criteria.setOrientation(LinearLayout.HORIZONTAL);

                ed = new EditText(SettingsActivity.this);
                ed.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,1.0f));
                ed.setPadding(10,0,10,0);
                ed.setHint("Enter Criteria");
                ed.setTextSize(16.0f);
                criteria.addView(ed);

                edval = new EditText(SettingsActivity.this);
                edval.setLayoutParams(new LinearLayout.LayoutParams(150,
                        LinearLayout.LayoutParams.WRAP_CONTENT,1.0f));
                edval.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                edval.setInputType(InputType.TYPE_CLASS_NUMBER);
                edval.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
                edval.setHint("%");

                criteria.addView(edval);

                parentView.addView(criteria);

                allEds.add(ed);
                allEds.add(edval);


                //button cuts half?????
                View lastChild = scrollLayout.getChildAt(scrollLayout.getChildCount() - 1);
                int bottom = lastChild.getBottom() + scrollLayout.getPaddingBottom();
                //int sy = scrollLayout.getScrollY();
                //int sh = scrollLayout.getHeight();
                //int delta = bottom - (sy + sh);
                int delta = bottom;

                scrollLayout.smoothScrollBy(0, delta);
            }
        });



    }
}

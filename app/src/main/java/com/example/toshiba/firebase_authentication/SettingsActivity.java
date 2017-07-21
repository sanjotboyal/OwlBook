package com.example.toshiba.firebase_authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.toshiba.firebase_authentication.Western.OWL.AverageCalculation;
import com.example.toshiba.firebase_authentication.Western.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import static android.R.attr.key;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static com.example.toshiba.firebase_authentication.R.id.Quiz;


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
    private Spinner spinnerCredit;

    private ArrayList<String> courses = new ArrayList<>();
    private ArrayList<Double> credits = new ArrayList<>();

    private List<EditText> allEds = new ArrayList<>();
    EditText ed;
    EditText edval;
    ScrollView scrollLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //get user
        if (savedInstanceState == null) {
            bundle = getIntent().getExtras();
            currUser = (User) bundle.get("CURRENT_USER");
            Log.d("homeActivityWithMenu", "user id:" + currUser.getId());
        }

        for(int i=0; i<currUser.getUserCourseList().size();i++){
            courses.add(currUser.getUserCourseList().get(i).getname());
        }

        //Set Spinner Courses
        spinner = (Spinner) findViewById(R.id.coursesView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinnerCredit = (Spinner) findViewById(R.id.spinner_credit);
        credits.add(0.5);
        credits.add(1.0);
        credits.add(2.0);

        ArrayAdapter<Double> adapter_Credit = new ArrayAdapter<Double>(this,android.R.layout.simple_spinner_item,credits);
        adapter_Credit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCredit.setAdapter(adapter_Credit);

        //set views (parent and scroll)
        final LinearLayout parentView = new LinearLayout(SettingsActivity.this);
        parentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        parentView.setOrientation(LinearLayout.VERTICAL);
        parentView.setId(R.id.criteriaView);

        scrollLayout = (ScrollView) findViewById(R.id.scroll);

        //editText fields
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
                double credit = Double.parseDouble(spinnerCredit.getSelectedItem().toString());

                currUser.getUserCourseList().get(pos).setCredit(credit);

                String QuizValue = Quiz_value.getText().toString();
                if(!(QuizValue.equals("") || QuizValue.equals("0"))){
                    currUser.getUserCourseList().get(pos).addCriteria("QUIZ",QuizValue);
                }

                String LabsValue = Labs_value.getText().toString();
                if(!(LabsValue.equals("") || LabsValue.equals("0"))){
                    currUser.getUserCourseList().get(pos).addCriteria("LAB",LabsValue);
                }

                String MidtermValue = Midterm_value.getText().toString();
                if(!(MidtermValue.equals("") || MidtermValue.equals("0"))){
                    currUser.getUserCourseList().get(pos).addCriteria("MIDTERM",MidtermValue);
                }

                String FinalValue = FinalExam_value.getText().toString();
                if(!(FinalValue.equals("") || FinalValue.equals("0"))){
                    currUser.getUserCourseList().get(pos).addCriteria("FINAL EXAM",FinalValue);
                }


                if(allEds.size()>1){
                    for(int i =0; i<allEds.size();i++){
                        currUser.getUserCourseList().get(pos).addCriteria(allEds.get(i).getText().toString().toUpperCase(),allEds.get(i+1).getText().toString());
                        i++;
                    }
                }

                Iterator myVeryOwnIterator = currUser.getUserCourseList().get(pos).Breakdown.keySet().iterator();
                while(myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    String value = currUser.getUserCourseList().get(pos).Breakdown.get(key);
                    Log.d("Breakdown: " + key, "value is: " + value);
                }

                new AverageCalculation(currUser.getUserCourseList().get(pos),currUser.getId(),pos).execute();


                Toast.makeText(SettingsActivity.this,"Successfully Created Mark Criteria for: " +spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                //Toast.makeText(SettingsActivity.this,"Hashmap test" + currUser.getUserCourseList().get(pos).Assignments.get("A1"), Toast.LENGTH_LONG).show();

                clearForm((ViewGroup) findViewById(R.id.scroll));
                if(allEds.size()>0){
                    ViewGroup viewGroup = (ViewGroup) findViewById(R.id.criteriaView);
                    viewGroup.removeAllViews();
                    allEds.clear();
                }

                if(pos == currUser.getUserCourseList().size()-1){
                    //Build object again from firebase
                    //send bundle with intent to homepage which will update current averages
                    //maybe left as filler if background task isnt complete
                }
                else {
                    spinner.setSelection(pos + 1);
                    Toast.makeText(SettingsActivity.this, "Proceed to add criteria for: " + spinner.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
                }
              // Iterator myVeryOwnIterator = currUser.getUserCourseList().get(pos).Breakdown.keySet().iterator();

             /*   while(myVeryOwnIterator.hasNext()) {
                    String key = (String) myVeryOwnIterator.next();
                    String value = currUser.getUserCourseList().get(pos).Breakdown.get(key);
                    Toast.makeText(SettingsActivity.this,"key: " + key +" Value: " + value, Toast.LENGTH_LONG).show();
                }*/
            }
        });

        final LinearLayout parentView2 = (LinearLayout) findViewById(R.id.breakdown);


        AddCriteria.setOnClickListener(new View.OnClickListener() {
            int count=0;
            @Override
            public void onClick(View v) {
                if(findViewById(R.id.criteriaView) != null) {
                    if (findViewById(R.id.criteriaView).getVisibility() == View.GONE) {
                        findViewById(R.id.criteriaView).setVisibility(View.VISIBLE);
                    }
                }

                LinearLayout criteria = new LinearLayout(SettingsActivity.this);
                criteria.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                criteria.setOrientation(LinearLayout.HORIZONTAL);

                ed = new EditText(SettingsActivity.this);
                ed.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
                ed.setPadding(10,0,10,0);
                ed.setHint("Enter Criteria");
                ed.setTextSize(16.0f);

                criteria.addView(ed);

                edval = new EditText(SettingsActivity.this);

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                float logicalDensity = metrics.density;

                Log.d("logicaldensity", "factor is " + logicalDensity);

                int px = (int) Math.ceil(80 * logicalDensity);

                LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(px, LinearLayout.LayoutParams.WRAP_CONTENT);
                edval.setLayoutParams(lllp);
                edval.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                edval.setInputType(InputType.TYPE_CLASS_NUMBER);
                edval.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
                edval.setHint("%");
                
                edval.measure(0, 0);
                Log.d("wtf view", "hello " + edval.getMeasuredWidth());

                criteria.addView(edval);
                parentView.addView(criteria);
                count++;
                if(count ==1) {
                    parentView2.addView(parentView);
                }
                allEds.add(ed);
                allEds.add(edval);

                //button cuts half?????
                View lastChild = scrollLayout.getChildAt(scrollLayout.getChildCount() - 1);
                int bottom = lastChild.getBottom();
                int sy = scrollLayout.getScrollY();
                int sh = scrollLayout.getHeight();
                int delta = bottom - (sy + sh) ;


                //scrollLayout.fullScroll(ScrollView.FOCUS_DOWN);
                scrollLayout.smoothScrollBy(0, delta);
            }
        });
    }

    private void clearForm(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {

            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }
}

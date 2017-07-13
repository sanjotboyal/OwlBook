package com.example.toshiba.firebase_authentication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends AppCompatActivity {

    private EditText Quiz_value;
    private EditText Labs_value;
    private EditText Midterm_value;
    private EditText FinalExam_value;

    private List<EditText> allEds = new ArrayList<EditText>();
    EditText ed;
    EditText edval;
    ScrollView scrollLayout;

    private Button AddCriteria;
    private Button Save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        scrollLayout = (ScrollView) findViewById(R.id.scroll);


        Quiz_value = (EditText) findViewById(R.id.QuizValue);
        Labs_value = (EditText) findViewById(R.id.LabsValue);
        Midterm_value = (EditText) findViewById(R.id.MidtermValue);
        FinalExam_value = (EditText) findViewById(R.id.FinalExamValue);



        AddCriteria = (Button) findViewById(R.id.addcriteria);
        Save = (Button) findViewById(R.id.save_btn);

        final LinearLayout parent = (LinearLayout) findViewById(R.id.breakdown);


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

                parent.addView(criteria);

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

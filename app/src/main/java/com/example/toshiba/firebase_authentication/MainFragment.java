package com.example.toshiba.firebase_authentication;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.animation.Animator;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.toshiba.firebase_authentication.MainActivity;
import com.example.toshiba.firebase_authentication.R;
import com.example.toshiba.firebase_authentication.Western.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import static android.R.attr.name;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private FirebaseAuth auth;

    //Dynamic array for all parsing jsoup links
    private ArrayList<String> user_login = new ArrayList<>();


    //FireBase DB reference
    private DatabaseReference udatabase;

    private User currUser;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Retrieve user information.
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            currUser = (User)bundle.get("CURRENT_USER");
            Log.d("[MainFragment]", "Successfully recieved User: " + currUser.getId());
        }

        //auth = FirebaseAuth.getInstance();

        //set current user instance
        //FirebaseUser User = auth.getCurrentUser();
        //Toast.makeText(getActivity(),"Welcome " +User.getEmail(), Toast.LENGTH_LONG).show();

        //Executes bg task of retrieving info
        new FireBaseRetrieval().execute();

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    //FireBase bg connection
    private class FireBaseRetrieval extends AsyncTask<Void,Void,Void>{

        // FirebaseUser User = auth.getCurrentUser();

        @Override
        protected Void doInBackground(Void... params) {

            /*
            //Connects straight to child "password of object"
            udatabase = FirebaseDatabase.getInstance().getReference().child(User.getUid()).child("password");

            udatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Adds password to user info array
                    String pass = dataSnapshot.getValue().toString();
                    user_login.add(pass);

                    //Calls owl parsing retrieval after firebase db is done
                    new owlRetrieval().execute();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            */
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


        }
    }

    //Owl Jsoup Connection
    private class owlRetrieval extends AsyncTask<Void,Void,Void>{

        FirebaseUser User = auth.getCurrentUser();
        String Email = User.getEmail();
        int end = Email.indexOf("@");
        String owl_user = Email.substring(0,end);

        @Override
        protected Void doInBackground(Void... params) {
            //Connects and verifies owl and gets info
            try {
                //Connects and parse page after login
                Connection.Response res = Jsoup.connect("https://owl.uwo.ca/portal/relogin").data("eid",owl_user).data("pw",user_login.get(0)).method(Connection.Method.POST).execute();
                Document document = Jsoup.connect("https://owl.uwo.ca/portal/relogin").cookies(currUser.getCookies()).get();

                //Saves login cookies
                Map<String,String>loginCookies = res.cookies();

                //Gets link for all sites
                String allsiteUrl = document.select("a#allSites[href]").first().attr("abs:href");

                //Gets link where all courses are contained
                Document courseframe= Jsoup.connect(allsiteUrl).cookies(loginCookies).get();
                String coursesURL = courseframe.select("div.title > a[href]").first().attr("abs:href");

                //Connects to Full Course List
                Document listofCourses = Jsoup.connect(coursesURL).cookies(loginCookies).get();
                Elements course_lists = listofCourses.select("tr");

                //User object instance
                userInfo CurrentUser = new userInfo();
                //Checks through all rows to find things that are actually courses (goes through all courses)
                for (Element course_list:course_lists){
                    String course = course_list.toString();
                    //Found courses: parses name and Base-url
                    if(course.contains("course")){
                        int nstart = course.indexOf("top\">")+6;
                        int nend = course.indexOf("</a>")-1;
                        String CourseName = course.substring(nstart,nend);

                        int lstart = course.indexOf("href=\"")+6;
                        int lend = course.indexOf("target")-2;
                        String CourseBaseURL = course.substring(lstart,lend);

                        //Set Objects for courses
                        courses ListCourses = new courses(CourseName,CourseBaseURL);

                        //Using Base-URL: finds gradebook URL:
                        Document gradebookURLs= Jsoup.connect(CourseBaseURL).cookies(loginCookies).get();
                        Elements GradebookPage = gradebookURLs.select("a.toolMenuLink[href],li.submenuitem");

                        //Gradebook URL Find- smart selection
                        for (Element Gradebook1page : GradebookPage) {
                            if (Gradebook1page.toString().contains("Gradebook")) {
                                String url = Gradebook1page.attr("abs:href");
                                Document directgradebook = Jsoup.connect(url).cookies(loginCookies).get();
                                String directGradesURL = directgradebook.select("div.title > a[href]").attr("abs:href");

                                //Sets it in courses object
                                ListCourses.setGradebook_URL(directGradesURL);
                            }
                        }

                        Document AssignmentGradeBook = Jsoup.connect(ListCourses.getGradebook_URL()).cookies(loginCookies).get();
                        // Document AssignmentGradeBook = Jsoup.connect("https://owl.uwo.ca/portal/tool/21b98a44-5379-4c2c-a7de-55ca60e03b00/studentView.jsf").cookies(loginCookies).get();

                        Elements assignments = AssignmentGradeBook.select("tr.internal,tr.external");

                        for (int i=0; i<assignments.size();i++) {
                            Element assignment = assignments.get(i);
                            String Raw = assignment.toString();

                            int astart = Raw.indexOf("left\">") + 6;

                            int aend;
                            if(Raw.contains("attach")){
                                aend = StringUtils.ordinalIndexOf(Raw, "</td>", 2);
                            } else{
                                aend = Raw.indexOf("</td>");
                            }

                            String Assign = Raw.substring(astart, aend);


                            String Mark;

                            if (Raw.contains("Not counted towards")) {
                                int mstart = Raw.indexOf("grade\">(") + 8;
                                int mend = Raw.indexOf(")</span>");
                                Mark = Raw.substring(mstart, mend);

                            } else {
                                int mstart = StringUtils.ordinalIndexOf(Raw, "center\">", 2) + 8;
                                if(Raw.contains("attach")) {
                                    int mend = StringUtils.ordinalIndexOf(Raw, "</td>", 4);
                                    Mark = Raw.substring(mstart, mend);
                                }else {
                                    int mend = StringUtils.ordinalIndexOf(Raw, "</td>", 3);
                                    Mark = Raw.substring(mstart, mend);
                                }
                            }


                            ListCourses.addAssignment("A"+i,Assign + ":" + Mark);
                        }

                        //Adds courses object to array list UNDER user
                        CurrentUser.addCourse(ListCourses);
                        ListCourses = null;

                    }
                }

                //Connects to firebase db under the Unique USER ID

                udatabase = FirebaseDatabase.getInstance().getReference().child(User.getUid());

                String nameURL = CurrentUser.UserCourseList.get(0).getGradebook_URL();

                Document nameURLs= Jsoup.connect(nameURL).cookies(loginCookies).get();
                String user_nameunparsed = nameURLs.select("h3").first().html();
                String user_name = user_nameunparsed.substring(user_nameunparsed.indexOf("for")+4);
                CurrentUser.setName(user_name);
                //Creates hashmap to update info under userName
                Map<String,Object> CourseUpdates = new HashMap<String,Object>();
                CourseUpdates.put(owl_user,CurrentUser);
                Log.d("FIRE 2- AFTER ","UPDATE-UPDATE");
                udatabase.updateChildren(CourseUpdates);
                Log.d("FIRE 3-AFTER ","UPDATE-UPDATE");

                CurrentUser = null;


            }catch(Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);



        }
    }
}

package com.example.toshiba.firebase_authentication.Western.OWL;

import android.os.AsyncTask;
import android.util.Log;

import com.example.toshiba.firebase_authentication.Western.Course;
import com.example.toshiba.firebase_authentication.Western.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by j3chowdh on 6/25/2017.
 */

public class CourseEntry extends AsyncTask<Void,Void,Void>  {
    private User currUser;

    // FireBase DB reference
    private DatabaseReference databaseReference;

    public CourseEntry(User user) {
        this.currUser = user;
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Connects and verifies owl and gets info
        try {
            Log.d("[CourseEntry", "Starting doInBackground...");
            // Use the current user's cookies.
            Map<String, String> cookies = currUser.getCookies();
            //Connection.Response res = Jsoup.connect("https://owl.uwo.ca/portal/relogin").data("eid",owl_user).data("pw",user_login.get(0)).method(Connection.Method.POST).execute();
            Document document = Jsoup.connect("https://owl.uwo.ca/portal")
                    .cookies(cookies).get();

            Log.d("[CourseEntry]", "SUCCESS : " + document.title());

            // Gets link for all sites.
            String allsiteUrl = document.select("a#allSites[href]").first().attr("abs:href");
            Log.d("[CourseEntry]", "Successfully connected to allsiteurl.");

            // Gets link where all courses are contained.
            Document courseframe= Jsoup.connect(allsiteUrl).cookies(cookies).get();
            String coursesURL = courseframe.select("div.title > a[href]").first().attr("abs:href");
            Log.d("[CourseEntry]", "Successfully connected to courseurl.");

            // Connects to Full Course List.
            Document listofCourses = Jsoup.connect(coursesURL).cookies(cookies).get();
            Elements course_lists = listofCourses.select("tr");

            // Checks through all rows to find things that are actually courses (goes through all courses).
            for (Element course_list : course_lists){
                String course = course_list.toString();

                Log.d("[COURSE]", course);

                //Found courses: parses name and Base-url
                if(course.contains("course")){
                    int nstart = course.indexOf("top\">") + 6;
                    int nend = course.indexOf("</a>") - 1;
                    String CourseName = course.substring(nstart,nend);

                    int lstart = course.indexOf("href=\"") + 6;
                    int lend = course.indexOf("target") - 2;
                    String CourseBaseURL = course.substring(lstart,lend);

                    // Set Objects for courses.
                    Course ListCourses = new Course(CourseName, CourseBaseURL);

                    // Using Base-URL: finds gradebook URL:
                    Document gradebookURLs = Jsoup.connect(CourseBaseURL).cookies(cookies).get();
                    Elements GradebookPage = gradebookURLs.select("a.toolMenuLink[href],li.submenuitem");

                    //Gradebook URL Find- smart selection
                    for (Element Gradebook1page : GradebookPage) {
                        if (Gradebook1page.toString().contains("Gradebook")) {
                            Log.d("[CourseEntry]", "trying gradebookpage");
                            String url = Gradebook1page.attr("abs:href");
                            Document directgradebook = Jsoup.connect(url).cookies(cookies).get();
                            String directGradesURL = directgradebook.select("div.title > a[href]").attr("abs:href");

                            //Sets it in courses object
                            ListCourses.setGradebook_URL(directGradesURL);
                            Log.d("[CourseEntry]", "added setGradebook_URL");
                        }
                    }

                    Document AssignmentGradeBook = Jsoup.connect(ListCourses.getGradebook_URL()).cookies(cookies).get();
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
                    currUser.addCourse(ListCourses);
                }
            }

            String nameURL = currUser.getCourse(0).getGradebook_URL();

            Document nameURLs= Jsoup.connect(nameURL).cookies(cookies).get();
            String user_nameunparsed = nameURLs.select("h3").first().html();

            String user_name = user_nameunparsed.substring(user_nameunparsed.indexOf("for")+4);
            currUser.setName(user_name);

            // Creates hashmap to update info under userName.
            //Map<String,Object> CourseUpdates = new HashMap<String,Object>();

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currUser.getId());

            currUser.FirebaseFix();

            Log.d("xd", "yo " + currUser.UserCourseList.size());

            databaseReference.setValue(currUser);
            Log.d("FIRE 3-AFTER ","UPDATE-UPDATE");
        }catch(Exception e){
            Log.d("[CourseEntry]", "FAILED: " + e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);



    }
}

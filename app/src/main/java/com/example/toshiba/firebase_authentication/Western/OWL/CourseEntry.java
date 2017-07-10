package com.example.toshiba.firebase_authentication.Western.OWL;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.toshiba.firebase_authentication.Western.Course;
import com.example.toshiba.firebase_authentication.Western.User;
import com.example.toshiba.firebase_authentication.homeActivityWithMenu;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Map;

import static android.R.attr.name;
import static java.security.AccessController.getContext;


public class CourseEntry extends AsyncTask<Void,Void,User>  {
    private User currUser;

    // FireBase DB reference
    private DatabaseReference databaseReference;
    private Intent intent;
    private Context ctx;
    public CourseEntry(User user, Context ctx) {
        this.currUser = user;
        this.ctx = ctx;
    }

    @Override
    protected User doInBackground(Void... params) {
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
            Elements course_lists = listofCourses.select("h4 > a[href]");

            // Checks through all rows to find things that are actually courses (goes through all courses).
            for (Element course_list : course_lists){
                //Found courses: parses name and Base-url
                if(course_list.toString().contains("FW")){
                    String Course_Name = course_list.html();
                    int endName = StringUtils.ordinalIndexOf(Course_Name, " ", 2);
                    String CourseName = Course_Name.substring(0,endName);

                    int endSection = StringUtils.ordinalIndexOf(Course_Name, " ", 3);
                    String CourseSection = Course_Name.substring(endName+1,endSection);

                    String CourseBaseURL = course_list.attr("abs:href");
                    Log.d("[COURSE PARSE: ", "Name: " + CourseName +": " + CourseSection);
                    Log.d("[COURSE PARSE: ", "URL: " + CourseBaseURL);

                    // Set Objects for courses.
                    Course ListCourses = new Course(CourseName, CourseSection, CourseBaseURL);

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
                    int i=0;
                    for (Element assignment:assignments){

                        String Assign = assignment.getElementsByClass("left").html();
                        Log.d("[GRADE-ENTRY ","NAME OF ASSIGNMENT: " + Assign);

                        String Mark = assignment.getElementsByClass("center").get(1).text();
                        Log.d("[GRADE-ENTRY ","MARK: " + Mark);

                        ListCourses.addAssignment("A"+i,Assign + ":" + Mark);
                        i+=1;
                    }

                    //Adds courses object to array list UNDER user
                    currUser.addCourse(ListCourses);
                }
            }

            String nameURL = currUser.getCourse(0).getGradebook_URL();

            Document nameURLs= Jsoup.connect(nameURL).cookies(cookies).get();
            String user_nameunparsed = nameURLs.select("h3").first().html();
            Log.d("UNPARSED: ", "User_name after selection: " + user_nameunparsed);

            String user_name = user_nameunparsed.substring(user_nameunparsed.indexOf("for")+4);
            currUser.setName(user_name);

            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(currUser.getId());

            currUser.FirebaseFix();

            databaseReference.setValue(currUser);
            Log.d("FIRE 3-AFTER ","UPDATE-UPDATE");

            return currUser;

        }catch(Exception e){
            Log.d("[CourseEntry]", "FAILED: " + e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(User u) {
        super.onPostExecute(u);
        intent = new Intent(ctx, homeActivityWithMenu.class);
        Log.d("onPostExecute", "user id is: " + u.getId());
        intent.putExtra("CURRENT_USER", u);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }
}

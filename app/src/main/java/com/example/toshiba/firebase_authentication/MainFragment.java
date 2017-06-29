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
import com.example.toshiba.firebase_authentication.Western.OWL.CourseEntry;
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
    private User currUser;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Retrieve user information.
        Bundle bundle = getArguments();
        if (bundle != null) {
            currUser = (User)bundle.get("CURRENT_USER");
            Log.d("[MainFragment]", "Successfully recieved User: " + currUser.getId());

            for(Map.Entry<String,String> entry : currUser.getCookies().entrySet()){
                Log.d("[MainFragment]", "Successfully recieved Cookie: " + entry.getKey() + " : " + entry.getValue());
            }
        }


        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}

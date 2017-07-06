package com.example.toshiba.firebase_authentication.Western;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.toshiba.firebase_authentication.CourseActivity;
import com.example.toshiba.firebase_authentication.MainActivity;
import com.example.toshiba.firebase_authentication.R;
import com.example.toshiba.firebase_authentication.homeActivityWithMenu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.R.attr.finishOnTaskLaunch;
import static android.R.attr.key;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context ctx;
    View view;
    ViewHolder viewHolder;
    List<Course> values;
    Intent intent;
    //ArrayList<String> assignments;

    public RecyclerViewAdapter(Context ctx, List<Course> values) {
        this.ctx = ctx;
        this.values = values;

    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(ctx).inflate(R.layout.single_item,parent,false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.course_name.setText(values.get(position).getname());
        holder.mark.setText(values.get(position).getCurrAverage());

        final int pos = position;

        Set<Map.Entry<String,String>> assigned = values.get(position).Assignments.entrySet();
        int length = assigned.size();

        if (length >0) {
            Map.Entry<String, String>[] ListofAssignments = new Map.Entry[length];
            assigned.toArray(ListofAssignments);
            Log.d("[LAST ASSN Course: ","Name: " + values.get(position).getname());
            Log.d("[LAST ASSIGNED: create:", ListofAssignments[length - 1].getKey() + ":XD:" + ListofAssignments[length - 1].getValue());

            holder.assignment1.setText(ListofAssignments[length - 1].getValue());
            holder.assignment2.setText(ListofAssignments[length - 2].getValue());
            holder.assignment3.setText(ListofAssignments[length - 3].getValue());


        }else{
            Log.d("[LAST ASSIGNED: create:", "NO assignments: " + values.get(position).getname());
            holder.assignment1.setText("No Assignments");
            holder.assignment2.setText("");
            holder.assignment3.setText("");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ctx, CourseActivity.class);
                intent.putExtra("CURRENT_COURSE",values.get(pos));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }
        });



  /*      for(int i=0; i<values.size();i++){
            Iterator myVeryOwnIterator = values.get(i).Assignments.keySet().iterator();
            while(myVeryOwnIterator.hasNext()) {
                String key=(String)myVeryOwnIterator.next();
                String value=(String)values.get(i).Assignments.get(key);
                assignments.add(value);
            }
            holder.assignment1.setText(assignments.get(assignments.size()-1));
            holder.assignment2.setText(assignments.get(assignments.size()-2));
            holder.assignment3.setText(assignments.get(assignments.size()-3));
        }*/

    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView course_name;
        TextView mark;
        TextView assignment1;
        TextView assignment2;
        TextView assignment3;

        //ListView assignmentsView;

        public ViewHolder(View itemView) {
            super(itemView);
            course_name = (TextView) itemView.findViewById(R.id.courseView);
            mark = (TextView) itemView.findViewById(R.id.markView);
            assignment1 = (TextView) itemView.findViewById(R.id.assignment1);
            assignment2 = (TextView) itemView.findViewById(R.id.assignment2);
            assignment3 = (TextView) itemView.findViewById(R.id.assignment3);


            //assignmentsView = (ListView) itemView.findViewById(R.id.listView);
        }
    }

}

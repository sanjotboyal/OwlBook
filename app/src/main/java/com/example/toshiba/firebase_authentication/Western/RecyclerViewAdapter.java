package com.example.toshiba.firebase_authentication.Western;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import com.example.toshiba.firebase_authentication.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.R.attr.key;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context ctx;
    View view;
    ViewHolder viewHolder;
    List<Course> values;
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

        Map.Entry<String,String> entry = values.get(0).Assignments.entrySet().iterator().next();
        String key= entry.getKey();
        String value=entry.getValue();

        Log.d("[MAIN FRAGMENT: create:", key + ":XD:" + value);
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
       /* TextView assignment1;
        TextView assignment2;
        TextView assignment3;*/

        //ListView assignmentsView;

        public ViewHolder(View itemView) {
            super(itemView);
            course_name = (TextView) itemView.findViewById(R.id.courseView);
            mark = (TextView) itemView.findViewById(R.id.markView);
            /*assignment1 = (TextView) itemView.findViewById(R.id.assignment1);
            assignment2 = (TextView) itemView.findViewById(R.id.assignment2);
            assignment3 = (TextView) itemView.findViewById(R.id.assignment3);*/

            //assignmentsView = (ListView) itemView.findViewById(R.id.listView);
        }
    }

}

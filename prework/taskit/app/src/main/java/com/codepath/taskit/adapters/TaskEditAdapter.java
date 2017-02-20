package com.codepath.taskit.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.codepath.taskit.R;
import com.codepath.taskit.data.dbflow.Task;

import java.util.List;

/**
 * Created by darewreck_PC on 2/19/2017.
 */

public class TaskEditAdapter extends ArrayAdapter<Task> {
    public TaskEditAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task, parent, false);
        }

        return convertView;

    /*
        if(convertView == null) {

            switch(position) {
                case 1: {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_edit_label_value, parent, false);
                    TextView txtLbl = (TextView) convertView.findViewById(R.id.label);
                    TextView txtValue = (TextView) convertView.findViewById(R.id.value);
                    txtLbl.setText("Task Name");
                    txtValue.setText(task.getName());
                    break;
                }
                case 2: {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.task, parent, false);
                }
                case 3: {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_edit_label_value, parent, false);
                    TextView txtLbl = (TextView) convertView.findViewById(R.id.label);
                    TextView txtValue = (TextView) convertView.findViewById(R.id.value);
                    txtLbl.setText("Task Notes");
                    txtValue.setText(task.getNotes());
                    break;
                }
                case 4: {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_edit_label_value, parent, false);
                    TextView txtLbl = (TextView) convertView.findViewById(R.id.label);
                    TextView txtValue = (TextView) convertView.findViewById(R.id.value);
                    txtLbl.setText("Priority Level");
                    txtValue.setText(task.getPriority());
                    break;
                }
                case 5: {
                    convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_edit_label_value, parent, false);
                    TextView txtLbl = (TextView) convertView.findViewById(R.id.label);
                    TextView txtValue = (TextView) convertView.findViewById(R.id.value);
                    txtLbl.setText("Status");
                    txtValue.setText(task.getStatus());
                    break;
                }
                default: {

                }
            }
        }


        TextView taskName = (TextView) convertView.findViewById(R.id.taskName);
        TextView taskPriority = (TextView) convertView.findViewById(R.id.taskPriority);

        taskName.setText(task.getName());
        taskPriority.setText(String.valueOf(task.getPriority()));

        return convertView;
        */
    }
}

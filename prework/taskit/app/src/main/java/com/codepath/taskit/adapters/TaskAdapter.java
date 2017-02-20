package com.codepath.taskit.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.codepath.taskit.R;
import com.codepath.taskit.data.dbflow.Task;

import java.util.List;

/**
 * Created by darewreck_PC on 2/18/2017.
 */

public class TaskAdapter extends ArrayAdapter<Task> {
    public TaskAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Task task = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.tasklist, parent, false);
        }

        TextView taskName = (TextView) convertView.findViewById(R.id.taskName);
        TextView taskPriority = (TextView) convertView.findViewById(R.id.taskPriority);

        taskName.setText(task.getName());
        taskPriority.setText(String.valueOf(task.getPriority()));

        return convertView;
    }
}

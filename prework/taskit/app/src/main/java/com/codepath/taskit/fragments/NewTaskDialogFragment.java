package com.codepath.taskit.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.taskit.R;
import com.codepath.taskit.data.dbflow.Task;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewTaskDialogFragment.NewTaskDialogListener} interface
 * to handle interaction events.
 * Use the {@link NewTaskDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewTaskDialogFragment extends DialogFragment {

    public NewTaskDialogFragment() {
    }

    public interface NewTaskDialogListener {
        void onFinishNewTaskDialog(Task task);
    }

    public static NewTaskDialogFragment newInstance() {
        NewTaskDialogFragment fragment = new NewTaskDialogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_edit_task_dialog, container, false);

        // set the listener for Navigation
        Toolbar actionBar = (Toolbar) v.findViewById(R.id.menu);

        if (actionBar!=null) {
            final NewTaskDialogFragment window = this;
            actionBar.setTitle("Create New Task");
            actionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onToolBarMenuSelect(v, item);
                }
            });
        }

        actionBar.inflateMenu(R.menu.task_new_menu);


        return v;
    }

    private boolean onToolBarMenuSelect(final View v, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_task: {
                EditText et_taskName = (EditText) v.findViewById(R.id.task_name_value);
                DatePicker et_taskDueDate = (DatePicker) v.findViewById(R.id.task_due_date_value);
                EditText et_taskNotes = (EditText) v.findViewById(R.id.task_note_value);

                Spinner et_taskPriorityLevel = (Spinner) v.findViewById(R.id.task_priority_value);
                Spinner et_taskStatus = (Spinner) v.findViewById(R.id.task_status_value);

                String priorityLevel = et_taskPriorityLevel.getSelectedItem().toString();
                String status = et_taskStatus.getSelectedItem().toString();

                String taskName = et_taskName.getText().toString();
                String taskNotes = et_taskNotes.getText().toString();

                int year    = et_taskDueDate.getYear() ;
                int month   = et_taskDueDate.getMonth() + 1;
                int day     = et_taskDueDate.getDayOfMonth();

                Date date = new Date(year, month, day);
                Task task = new Task();
                task.setName(taskName);
                task.setNotes(taskNotes);
                task.setPriority(priorityLevel);
                task.setStatus(status);
                task.setDueDate(date);

                NewTaskDialogListener listener = (NewTaskDialogListener) getActivity();
                listener.onFinishNewTaskDialog(task);

                dismiss();
                return true;
            }
            case R.id.action_cancel_task: {
                dismiss();
                return true;
            }
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

}

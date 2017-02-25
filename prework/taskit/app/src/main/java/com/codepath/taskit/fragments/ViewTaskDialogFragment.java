package com.codepath.taskit.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.taskit.R;
import com.codepath.taskit.data.dbflow.Task;

import org.parceler.Parcels;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewTaskDialogFragment.ViewTaskDialogListener} interface
 * to handle interaction events.
 * Use the {@link ViewTaskDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewTaskDialogFragment extends DialogFragment {

    public ViewTaskDialogFragment() {
    }

    public interface ViewTaskDialogListener {
        void editTask(Task task, int position);
        void deleteTask(Task task, int position);
    }

    public static ViewTaskDialogFragment newInstance(Task task,int position) {
        ViewTaskDialogFragment fragment = new ViewTaskDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("task", Parcels.wrap(task));
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_edit_task_dialog, container, false);
        final int position = getArguments().getInt("position");

        // set the listener for Navigation
        Toolbar actionBar = (Toolbar) v.findViewById(R.id.menu);

        //set contents
        final Task task = (Task) Parcels.unwrap( getArguments().getParcelable("task"));
        EditText et_taskName = (EditText) v.findViewById(R.id.task_name_value);
        DatePicker et_taskDueDate = (DatePicker) v.findViewById(R.id.task_due_date_value);
        EditText et_taskNotes = (EditText) v.findViewById(R.id.task_note_value);
        Spinner et_taskPriorityLevel = (Spinner) v.findViewById(R.id.task_priority_value);
        Spinner et_taskStatus = (Spinner) v.findViewById(R.id.task_status_value);

        et_taskName.setText(task.getName().toString());
        et_taskName.requestFocus();
        et_taskName.setSelection(task.getName().length());

        Date dueDate = task.getDueDate();
        et_taskDueDate.updateDate(dueDate.getYear(), dueDate.getMonth()-1, dueDate.getDay());

        et_taskNotes.setText(task.getNotes().toString());
        List<String> prorityValues = Arrays.asList(getResources().getStringArray(R.array.task_priority_values));
        int priorityIndex = prorityValues.indexOf(task.getPriority());
        et_taskPriorityLevel.setSelection(priorityIndex);

        List<String> statusValue = Arrays.asList(getResources().getStringArray(R.array.task_status_values));
        int statusIndex = statusValue.indexOf(task.getStatus());
        et_taskStatus.setSelection(statusIndex);

        et_taskName.setEnabled(false);
        et_taskDueDate.setEnabled(false);
        et_taskNotes.setEnabled(false);
        et_taskPriorityLevel.setEnabled(false);
        et_taskStatus.setEnabled(false);

        if (actionBar!=null) {
            final ViewTaskDialogFragment window = this;
            actionBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return onToolBarMenuSelect(v, item, task, position);
                }
            });
        }

        actionBar.inflateMenu(R.menu.task_edit_menu);
        return v;
    }

    private boolean onToolBarMenuSelect(final View v, MenuItem item, Task task,int taskPosition) {
        switch (item.getItemId()) {
            case R.id.action_edit_task: {
                ViewTaskDialogListener listener = (ViewTaskDialogListener) getActivity();
                listener.editTask(task, taskPosition);
                dismiss();
                return true;
            }
            case  R.id.action_delete_task: {
                ViewTaskDialogFragment.ViewTaskDialogListener listener = (ViewTaskDialogFragment.ViewTaskDialogListener) getActivity();
                listener.deleteTask(task, taskPosition);
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

package com.codepath.taskit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.taskit.R;
import com.codepath.taskit.data.dbflow.Task;

import org.parceler.Parcels;

import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SendEmailTaskDialogFragment.NewTaskDialogListener} interface
 * to handle interaction events.
 * Use the {@link SendEmailTaskDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendEmailTaskDialogFragment extends DialogFragment {

    public SendEmailTaskDialogFragment() {
    }

    public interface NewTaskDialogListener {
        void onFinishNewTaskDialog(Task task);
    }

    public static SendEmailTaskDialogFragment newInstance(List<Task> tasklist) {
        SendEmailTaskDialogFragment fragment = new SendEmailTaskDialogFragment();
        Bundle args = new Bundle();
        String list = "";
        for(Task task:tasklist) {
            list += "Name:" + task.getName() + "\n";
            list += "Due Date:" + task.getDueDate().toString() + "\n";
            list += "Notes:" + task.getNotes() + "\n";
            list += "Priority:" + task.getPriority() + "\n";
            list += "Status:" + task.getStatus() + "\n";
            list += "\n\n";
        }
        args.putString("taskList",list );
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_email_task, container, false);

        final String emailContent = getArguments().getString("taskList");

        EditText et_email = (EditText) v.findViewById(R.id.et_email);
        final String email = et_email.getText().toString();

        Button btn_cancel = (Button)v.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button btn_send = (Button)v.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);

                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
                i.putExtra(Intent.EXTRA_SUBJECT, "List of Task");
                i.putExtra(Intent.EXTRA_TEXT   , emailContent);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        return v;
    }

    private boolean onToolBarMenuSelect(final View v, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_task: {
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

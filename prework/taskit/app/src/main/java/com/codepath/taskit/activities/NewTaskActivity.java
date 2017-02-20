package com.codepath.taskit.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.taskit.R;
import com.codepath.taskit.data.dbflow.Task;
import com.codepath.taskit.types.ActionEnum;

import org.parceler.Parcels;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class NewTaskActivity extends AppCompatActivity {
    private Task task;
    private int taskPosition;

    private EditText et_taskName;
    private DatePicker et_taskDueDate;
    private EditText et_taskNotes;
    private Spinner et_taskPriorityLevel;
    private Spinner et_taskStatus;
    private ActionEnum action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.menu);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(R.drawable.taskit);


        this.et_taskName = (EditText) findViewById(R.id.task_name_value);
        this.et_taskDueDate = (DatePicker) findViewById(R.id.task_due_date_value);
        this.et_taskNotes = (EditText) findViewById(R.id.task_note_value);
        this.et_taskPriorityLevel = (Spinner) findViewById(R.id.task_priority_value);
        this.et_taskStatus = (Spinner) findViewById(R.id.task_status_value);

        task =  (Task) Parcels.unwrap(getIntent().getParcelableExtra("todoItem"));

        if(task != null)
        {
            this.taskPosition = getIntent().getExtras().getInt("taskPosition");
            this.et_taskName.setText(task.getName().toString());
            this.et_taskName.requestFocus();
            this.et_taskName.requestFocus();
            this.et_taskName.setSelection(task.getName().length());

            Date dueDate = task.getDueDate();
            this.et_taskDueDate.updateDate(dueDate.getYear(), dueDate.getMonth()-1, dueDate.getDay());

            this.et_taskNotes.setText(task.getNotes().toString());
            List<String> prorityValues = Arrays.asList(getResources().getStringArray(R.array.task_priority_values));
            int prorityIndex = prorityValues.indexOf(task.getPriority());
            this.et_taskPriorityLevel.setSelection(prorityIndex);

            List<String> statusValue = Arrays.asList(getResources().getStringArray(R.array.task_status_values));
            int statusIndex = statusValue.indexOf(task.getStatus());
            this.et_taskStatus.setSelection(statusIndex);
            action = ActionEnum.saveTask;
        }
        else {
            action = ActionEnum.addTask;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_new_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_task:
            {
                Intent data = new Intent();

                EditText et_taskName = (EditText) findViewById(R.id.task_name_value);
                DatePicker et_taskDueDate = (DatePicker) findViewById(R.id.task_due_date_value);
                EditText et_taskNotes = (EditText) findViewById(R.id.task_note_value);


                String priorityLevel = this.et_taskPriorityLevel.getSelectedItem().toString();
                String status = this.et_taskStatus.getSelectedItem().toString();

                String taskName = et_taskName.getText().toString();
                String taskNotes = et_taskNotes.getText().toString();

                int year    = et_taskDueDate.getYear() ;
                int month   = et_taskDueDate.getMonth() + 1;
                int day     = et_taskDueDate.getDayOfMonth();

                Date date = new Date(year, month, day);

                if(task == null)
                {
                    task = new Task();
                }
                task.setName(taskName);
                task.setNotes(taskNotes);
                task.setPriority(priorityLevel);
                task.setStatus(status);
                task.setDueDate(date);

                data.putExtra("todoItem", Parcels.wrap(task));
                data.putExtra("action",action);
                data.putExtra("taskPosition", this.taskPosition);
                setResult(RESULT_OK, data);


                this.finish();
                return true;
            }
            case R.id.action_cancel_task:
            {
                Intent data = new Intent();
                data.putExtra("action", ActionEnum.cancel);
                setResult(RESULT_OK, data);
                this.finish();
                return true;
            }
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}

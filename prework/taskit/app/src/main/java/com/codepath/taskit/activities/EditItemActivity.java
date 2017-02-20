package com.codepath.taskit.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.codepath.taskit.R;
import com.codepath.taskit.data.dbflow.Task;
import com.codepath.taskit.types.ActionEnum;

import org.parceler.Parcels;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EditItemActivity extends AppCompatActivity {

    private EditText et_taskName;
    private DatePicker et_taskDueDate;
    private EditText et_taskNotes;
    private Spinner et_taskPriorityLevel;
    private Spinner et_taskStatus;

    private int itemPosition;
    private Task task;

    private ListView taskListView;

    private int EDIT_TASK_REQUEST_CODE = 30;
    private int SAVE_TASK_REQUEST_CODE = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.menu);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.drawable.taskit);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });
        this.taskListView = (ListView) findViewById(R.id.task_lv);

        task =  (Task) Parcels.unwrap(getIntent().getParcelableExtra("todoItem"));
        this.itemPosition = getIntent().getExtras().getInt("taskPosition");

        this.et_taskName = (EditText) findViewById(R.id.task_name_value);
        this.et_taskDueDate = (DatePicker) findViewById(R.id.task_due_date_value);
        this.et_taskNotes = (EditText) findViewById(R.id.task_note_value);
        this.et_taskPriorityLevel = (Spinner) findViewById(R.id.task_priority_value);
        this.et_taskStatus = (Spinner) findViewById(R.id.task_status_value);

        this.et_taskName.setText(task.getName().toString());
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

        this.et_taskName.setEnabled(false);
        this.et_taskDueDate.setEnabled(false);
        this.et_taskNotes.setEnabled(false);
        this.et_taskPriorityLevel.setEnabled(false);
        this.et_taskStatus.setEnabled(false);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_edit_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void onSaveClick(View v)
    {

        Intent data = new Intent();
        /*
        task.setName(this.editText.getText().toString());
        data.putExtra("todoItem", Parcels.wrap(task));
        data.putExtra("todoItemPosition", this.itemPosition);
        */
        setResult(RESULT_OK, data);
        this.finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit_task:
            {
                Intent intent = new Intent(EditItemActivity.this, NewTaskActivity.class);

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

                task.setName(taskName);
                task.setNotes(taskNotes);
                task.setPriority(priorityLevel);
                task.setStatus(status);
                task.setDueDate(date);

                intent.putExtra("todoItem", Parcels.wrap(task));
                intent.putExtra("taskPosition","taskPosition" );
                intent.putExtra("action", ActionEnum.editTask);
                startActivityForResult (intent, EDIT_TASK_REQUEST_CODE);
                return true;
            }
            case R.id.action_delete_task:
            {
                Intent data = new Intent();
                data.putExtra("action", ActionEnum.deleteTask);
                data.putExtra("taskId", task.getId());
                data.putExtra("taskPosition", itemPosition);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TASK_REQUEST_CODE) {
            ActionEnum action = (ActionEnum)data.getExtras().get("action");
            if(action == ActionEnum.saveTask) {
                setResult(RESULT_OK,data );
                this.finish();
            }
        }
    }
}

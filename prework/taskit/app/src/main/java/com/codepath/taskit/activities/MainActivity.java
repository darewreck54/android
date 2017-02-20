package com.codepath.taskit.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.codepath.taskit.R;
import com.codepath.taskit.adapters.ITaskDbAdapter;
import com.codepath.taskit.adapters.TaskDbFlowAdapter;
import com.codepath.taskit.data.dbflow.Task;
import com.codepath.taskit.types.ActionEnum;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.parceler.Parcels;

import java.util.List;

import com.codepath.taskit.adapters.TaskAdapter;

public class MainActivity extends AppCompatActivity {
    private ITaskDbAdapter taskDb;

    private List<Task> tasks;
    private ArrayAdapter<Task> itemsAdapter;

    ListView lvItems;
    EditText etEditText;

    private final int ADD_TASK_REQUEST_CODE = 20;
    private final int EDIT_REQUEST_CODE = 30;

    Button addButton;
    private static String TAG = MainActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.taskDb = new TaskDbFlowAdapter();
//this.taskDb.clearTable();
        this.lvItems = (ListView) findViewById(R.id.lvItems);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.menu);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(R.drawable.taskit);
        //getActionBar().setIcon(R.drawable.taskit);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                // User chose the "Settings" item, show the app settings UI...
                Intent intent = new Intent(MainActivity.this, NewTaskActivity.class);
                startActivityForResult (intent, ADD_TASK_REQUEST_CODE);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    private void init() {
        FlowManager.init(new FlowConfig.Builder(getApplicationContext()).build());
        populateTaskList();
        setupListItemViewListener();
    }
    private void populateTaskList() {
        this.tasks = this.taskDb.queryTaskList();
        this.itemsAdapter = new TaskAdapter(this, this.tasks);
        this.lvItems.setAdapter(this.itemsAdapter);
    }

    private void setupListItemViewListener() {
        this.lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                intent.putExtra("todoItem", Parcels.wrap(tasks.get(position)));
                intent.putExtra("taskPosition", position);
                startActivityForResult (intent, EDIT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ADD_TASK_REQUEST_CODE) {
            ActionEnum action = (ActionEnum)data.getExtras().get("action");
            if(action == ActionEnum.addTask) {
                Task task = (Task) Parcels.unwrap(data.getParcelableExtra("todoItem"));
                task = this.taskDb.addTask(task);
                this.tasks.add(task);

                itemsAdapter.notifyDataSetChanged();
            }
            else if(action == ActionEnum.cancel) {
                //do nothing
            }

        }
        else if(resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            ActionEnum action = (ActionEnum)data.getExtras().get("action");

            if(action == ActionEnum.deleteTask) {
                long taskId = data.getExtras().getLong("taskId");
                int position = data.getExtras().getInt("taskPosition");
                tasks.remove(position);
                taskDb.deleteTask(taskId);
                itemsAdapter.notifyDataSetChanged();
            }
            else if(action == ActionEnum.saveTask) {
               int position = data.getExtras().getInt("taskPosition");
                Task task = (Task) Parcels.unwrap(data.getParcelableExtra("todoItem"));
                tasks.set(position, task);
                taskDb.updateTask(task.getId(), task);
                itemsAdapter.notifyDataSetChanged();

            }
        }

    }
}

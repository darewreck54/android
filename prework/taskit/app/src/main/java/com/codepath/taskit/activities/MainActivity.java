package com.codepath.taskit.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
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
import android.widget.Toast;

import com.codepath.taskit.R;
import com.codepath.taskit.adapters.ITaskDbAdapter;
import com.codepath.taskit.adapters.TaskDbFlowAdapter;
import com.codepath.taskit.data.dbflow.Task;
import com.codepath.taskit.fragments.EditTaskDialogFragment;
import com.codepath.taskit.fragments.NewTaskDialogFragment;
import com.codepath.taskit.fragments.ViewTaskDialogFragment;
import com.codepath.taskit.types.ActionEnum;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.parceler.Parcels;

import java.util.List;

import com.codepath.taskit.adapters.TaskAdapter;

public class MainActivity extends AppCompatActivity implements NewTaskDialogFragment.NewTaskDialogListener,ViewTaskDialogFragment.ViewTaskDialogListener, EditTaskDialogFragment.EditTaskDialogListener {
    private ITaskDbAdapter taskDb;

    private List<Task> tasks;
    private ArrayAdapter<Task> itemsAdapter;

    ListView lvItems;
    private static String TAG = MainActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.taskDb = new TaskDbFlowAdapter();
        this.lvItems = (ListView) findViewById(R.id.lvItems);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.menu);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setIcon(R.drawable.taskit);
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
                showNewTaskDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showNewTaskDialog() {
        FragmentManager fm = getSupportFragmentManager();
        NewTaskDialogFragment fragment = NewTaskDialogFragment.newInstance("some title");
        fragment.show(fm, "test");
    }

    private void viewTaskDialog(Task task, int position) {
        FragmentManager fm = getSupportFragmentManager();
        ViewTaskDialogFragment fragment = ViewTaskDialogFragment.newInstance(task, position);
        fragment.show(fm, "test");
    }

    private void editTaskDialog(Task task, int position) {
        FragmentManager fm = getSupportFragmentManager();
        EditTaskDialogFragment fragment = EditTaskDialogFragment.newInstance(task, position);
        fragment.show(fm, "test");
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
                viewTaskDialog(tasks.get(position), position);
             }
        });
    }

    @Override
    public void onFinishNewTaskDialog(Task task) {
        task = this.taskDb.addTask(task);
        this.tasks.add(task);
        itemsAdapter.notifyDataSetChanged();
        Toast.makeText(this, task.getName() + " created!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteTask(Task task, int position) {
        long taskId = task.getId();
        tasks.remove(position);
        taskDb.deleteTask(taskId);
        itemsAdapter.notifyDataSetChanged();
        Toast.makeText(this, task.getName() + " deleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void editTask(Task task, int position) {
        editTaskDialog(task, position);
    }
    @Override
    public void saveTask(Task task, int position) {
        tasks.set(position, task);
        taskDb.updateTask(task.getId(), task);
        itemsAdapter.notifyDataSetChanged();
        Toast.makeText(this, task.getName() + " was edited!", Toast.LENGTH_SHORT).show();
    }
}

package com.codepath.taskit.adapters;

import com.codepath.taskit.data.dbflow.Task;
import com.codepath.taskit.data.dbflow.Task_Table;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Date;
import java.util.List;

/**
 * Created by darewreck_PC on 2/18/2017.
 */

public class TaskDbFlowAdapter implements ITaskDbAdapter {
    public List<Task> queryTaskList() {
        final List<Task> taskList = SQLite.select().from(Task.class).queryList();
        return taskList;
    }

    public Task addTask(String name, String notes, String priority, String status)  {
        Task task = new Task(name, new Date(), notes, priority,status);
        task.save();
        return task;
    }

    public void updateTask(long taskId, Task updateTask)
    {
        Task task = SQLite.select().from(Task.class).where(Task_Table.id.is(taskId)).querySingle();
        task.update(updateTask);
        task.save();
    }

    public void deleteTask(long taskId)
    {
        Task task = SQLite.select().from(Task.class).where(Task_Table.id.is(taskId)).querySingle();
        task.delete();
    }

    public void clearTable() {
        Delete.table(Task.class);
    }

    public Task addTask(Task task) {
        task.save();
        return task;
    }

}

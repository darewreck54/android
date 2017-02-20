package com.codepath.taskit.adapters;

import com.codepath.taskit.data.dbflow.Task;
import java.util.List;

/**
 * Created by darewreck_PC on 2/18/2017.
 */

public interface ITaskDbAdapter {
    List<Task> queryTaskList();
    Task addTask(String name, String notes, String priority, String status);
    Task addTask(Task task);
    void updateTask(long taskId, Task updateTask);
    void deleteTask(long taskId);
    void clearTable();
}

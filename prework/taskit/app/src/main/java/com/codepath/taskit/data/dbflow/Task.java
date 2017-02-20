package com.codepath.taskit.data.dbflow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.parceler.Parcel;

import java.util.Date;

/**
 * Created by darewreck_PC on 2/18/2017.
 */

@Parcel(analyze={Task.class})
@Table(database = TodoDatabase.class)
public class Task extends BaseModel {
    @Column
    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private String name;

    @Column
    private Date dueDate;

    @Column
    private String notes;

    @Column
    private String priority;

    @Column
    private String status;

    public Task (){
        super();
        this.name = "";
        this.notes = "";
    }
    public Task(String name, Date dueDate, String notes, String priority, String status) {
        super();
        this.name = name;
        this.dueDate = dueDate;
        this.notes = notes;
        this.priority = priority;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void update(Task task)
    {
        this.name = name;
        this.notes = notes;
        this.priority = priority;
        this.status = status;
    }
}

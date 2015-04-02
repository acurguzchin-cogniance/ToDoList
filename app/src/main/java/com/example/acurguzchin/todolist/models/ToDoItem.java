package com.example.acurguzchin.todolist.models;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by acurguzchin on 01.04.15.
 */
public class ToDoItem {
    private String task;
    private Date createdOn;

    public ToDoItem(String task) {
        this(task, new Date(System.currentTimeMillis()));
    }

    public ToDoItem(String task, Date createdOn) {
        this.task = task;
        this.createdOn = createdOn;
    }

    public String getTask() {
        return task;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String dateString = sdf.format(createdOn);
        return "(" + dateString + ")" + task;
    }
}

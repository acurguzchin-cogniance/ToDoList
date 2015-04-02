package com.example.acurguzchin.todolist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.acurguzchin.todolist.R;
import com.example.acurguzchin.todolist.models.ToDoItem;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by acurguzchin on 01.04.15.
 */
public class ToDoItemAdapter extends ArrayAdapter<ToDoItem> {
    private final int resource;

    public ToDoItemAdapter(Context context, int resource, List<ToDoItem> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout todoView;

        ToDoItem item = getItem(position);

        String task = item.getTask();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        String dateString = sdf.format(item.getCreatedOn());

        if (convertView == null) {
            todoView = new LinearLayout(getContext());
            LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            li.inflate(resource, todoView, true);
        }
        else {
            todoView = (LinearLayout) convertView;
        }

        TextView taskView = (TextView) todoView.findViewById(R.id.row);
        TextView dateView = (TextView) todoView.findViewById(R.id.rowDate);
        taskView.setText(task);
        dateView.setText(dateString);

        return todoView;
    }
}

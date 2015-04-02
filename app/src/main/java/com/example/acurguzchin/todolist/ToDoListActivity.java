package com.example.acurguzchin.todolist;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.acurguzchin.todolist.adapters.ToDoItemAdapter;
import com.example.acurguzchin.todolist.fragments.NewItemFragment;
import com.example.acurguzchin.todolist.fragments.ToDoListFragment;
import com.example.acurguzchin.todolist.models.ToDoItem;

import java.util.ArrayList;

/**
 * Created by acurguzchin on 30.03.15.
 */
public class ToDoListActivity extends Activity implements NewItemFragment.OnNewItemAddedListener {
    private ArrayList<ToDoItem> todoItems;
    private ToDoItemAdapter aa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(ToDoListActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        FragmentManager fm = getFragmentManager();
        ToDoListFragment toDoListFragment = (ToDoListFragment) fm.findFragmentById(R.id.ToDoListFragment);

        todoItems = new ArrayList<>();
        aa = new ToDoItemAdapter(this, R.layout.todolist_item, todoItems);
        toDoListFragment.setListAdapter(aa);
    }

    @Override
    public void onNewItemAdded(String task) {
        todoItems.add(0, new ToDoItem(task));
        aa.notifyDataSetChanged();
    }
}

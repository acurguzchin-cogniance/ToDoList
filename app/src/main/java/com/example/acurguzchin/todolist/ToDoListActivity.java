package com.example.acurguzchin.todolist;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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
public class ToDoListActivity extends Activity implements NewItemFragment.OnNewItemAddedListener, LoaderManager.LoaderCallbacks<Cursor> {
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

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public void onNewItemAdded(String task) {
        ContentResolver contentResolver = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(ToDoContentProvider.KEY_TASK, task);
        contentResolver.insert(ToDoContentProvider.CONTENT_URI, values);

        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ToDoContentProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        int keyTaskIndex = cursor.getColumnIndex(ToDoContentProvider.KEY_TASK);
        todoItems.clear();
        while (cursor.moveToNext()) {
            ToDoItem todoItem = new ToDoItem(cursor.getString(keyTaskIndex));
            todoItems.add(todoItem);
        }
        aa.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

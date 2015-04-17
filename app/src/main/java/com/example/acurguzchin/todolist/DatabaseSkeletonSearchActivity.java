package com.example.acurguzchin.todolist;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by acurguzchin on 17.04.15.
 */
public class DatabaseSkeletonSearchActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static String QUERY_EXTRA_KEY = "QUERY_EXTRA_KEY";
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_1,
                null,
                new String[] { ToDoContentProvider.KEY_TASK },
                new int[] { android.R.id.text1 },
                0
        );

        getLoaderManager().initLoader(0, null, this);
        parseIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String query = parseIntent(intent);
        if (query != null) {
            performSearch(query);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        String query = "0";
        if (bundle != null) {
            query = bundle.getString(QUERY_EXTRA_KEY);
        }

        String[] projection = {
                ToDoContentProvider.KEY_ID,
                ToDoContentProvider.KEY_TASK
        };

        String where = ToDoContentProvider.KEY_TASK + " LIKE \"%" + query + "%\"";
        String[] whereArgs = null;

        String sortOrder = ToDoContentProvider.KEY_TASK + " COLLATE LOCALIZED ASC";

        return new CursorLoader(this, ToDoContentProvider.CONTENT_URI, projection, where, whereArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Uri selectedUri = ContentUris.withAppendedId(ToDoContentProvider.CONTENT_URI, id);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(selectedUri);
        startActivity(intent);
    }

    private String parseIntent(Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
            return intent.getStringExtra(SearchManager.QUERY);
        }
        else {
            return null;
        }
    }

    private void performSearch(String searchQuery) {
        Bundle bundle = new Bundle();
        bundle.putString(QUERY_EXTRA_KEY, searchQuery);
        getLoaderManager().restartLoader(0, bundle, this);
    }
}

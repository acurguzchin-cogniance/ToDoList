package com.example.acurguzchin.todolist;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by acurguzchin on 15.04.15.
 */
public class ToDoContentProvider extends ContentProvider {
    public static final Uri CONTENT_URI = Uri.parse("content://com.example.acurguzchin.todolist/todoitems");

    public static final String KEY_ID = "_id";
    public static final String KEY_TASK = "task";
    public static final String KEY_CREATED_ON = "created_on";

    private MySQLiteOpenHelper mySQLiteOpenHelper;

    private final static int ALL_ROWS = 1;
    private final static int SINGLE_ROW = 2;

    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("com.example.acurguzchin.todolist", "todoitems", ALL_ROWS);
        uriMatcher.addURI("com.example.acurguzchin.todolist", "todoitems/#", SINGLE_ROW);
    }

    @Override
    public boolean onCreate() {
        mySQLiteOpenHelper = new MySQLiteOpenHelper(
                getContext(),
                MySQLiteOpenHelper.DATABASE_NAME, null,
                MySQLiteOpenHelper.DATABASE_VERSION
        );
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();

        String groupBy = null;
        String having = null;

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MySQLiteOpenHelper.DATABASE_TABLE);

        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowId = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(KEY_ID + "=" + rowId);
             default: break;
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        if (uriMatcher.match(uri) == ALL_ROWS) {
            return "vnd.android.cursor.dir/vnd.example.todos";
        }
        else if (uriMatcher.match(uri) == SINGLE_ROW) {
            return "vnd.android.cursor.item/vnd.example.todos";
        }
        else {
            throw new IllegalArgumentException("Unsupported URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();
        String nullColumnHack = null;
        long id = db.insert(MySQLiteOpenHelper.DATABASE_TABLE, nullColumnHack, values);
        if (id > -1) {
            Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(insertedId, null);
            return insertedId;
        }
        else {
            return null;
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();

        if (uriMatcher.match(uri) == SINGLE_ROW) {
            String rowId = uri.getPathSegments().get(1);
            selection = KEY_ID + "=" + rowId +
                    (
                            (TextUtils.isEmpty(selection)) ? "" : "AND (" + selection + ")"
                    );
        }

        if (selection == null) {
            selection = "1";
        }

        int deleteCount = db.delete(MySQLiteOpenHelper.DATABASE_TABLE, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mySQLiteOpenHelper.getWritableDatabase();

        if (uriMatcher.match(uri) == SINGLE_ROW) {
                String rowId = uri.getPathSegments().get(1);
                selection = KEY_ID + "=" + rowId +
                        (
                                TextUtils.isEmpty(selection) ? "" : " AND (" + selection + ")"
                        );
        }

        int updateCount = db.update(MySQLiteOpenHelper.DATABASE_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }

    private static class MySQLiteOpenHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "todoDatabase.db";
        private static final String DATABASE_TABLE = "todo_items";
        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_CREATE =
                "create table " + DATABASE_TABLE +
                " (" + KEY_ID + " integer primary key autoincrement, " +
                    KEY_TASK + " text not null, " +
                    KEY_CREATED_ON + "long);";

        public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF IT EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }
}

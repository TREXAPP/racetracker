package com.trex.racetracker;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashSet;

import static com.trex.racetracker.DatabaseHelper.*;
import static java.sql.Types.NULL;
import static java.util.Objects.isNull;

/**
 * Created by Igor on 18.4.2017.
 */
/*
 * Define an implementation of ContentProvider that stubs out
 * all methods
 */
public class Provider extends ContentProvider {

    DatabaseHelper database;
    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.trex.racetracker.provider";//specific for our our app, will be specified in maninfed
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);
/*
    // used for the UriMacher
    private static final int ENTRIES = 10;
    private static final int ENTRY_ID = 20;

    private static final String AUTHORITY = "com.trex.racetracker.contentprovider";

    private static final String BASE_PATH = "racetracker";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/todos";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/todo";

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, TODOS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TODO_ID);
    }
*/
 /*
 * Always return true, indicating that the
 * provider loaded correctly.
 */
    @Override
    public boolean onCreate() {
        database = DatabaseHelper.getInstance(getContext());
        return false;
    }


    /*
     * query() always returns no results
     *

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        return null;
    }
 */
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String table = getTableName(uri);
        if (database.equals(NULL)) {
            database = DatabaseHelper.getInstance(getContext());
        }
        SQLiteDatabase db = database.getReadableDatabase();
        return db.query(table,  projection, selection, selectionArgs, null, null, sortOrder);
       // return db.query(distinct, table, projection, selection, selectionArgs, groupBy, having, sortOrder, limit);
    }


    /*
 * Return no type for MIME type
 */
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    /*
 * insert() always returns null (no URI)
 */
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues initialValues) {
        String table = getTableName(uri);
        if (database == null) {
            database = DatabaseHelper.getInstance(getContext());
        }
        SQLiteDatabase db = database.getWritableDatabase();
        long value = db.insert(table, null, initialValues);
        //getContext().getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(CONTENT_URI, String.valueOf(value));
    }


    /*
 * delete() always returns "no rows affected" (0)
 */
    @Override
    public int delete(@NonNull Uri uri, @Nullable String where, @Nullable String[] args) {
        String table = getTableName(uri);
        if (database.equals(NULL)) {
            database = DatabaseHelper.getInstance(getContext());
        }
        SQLiteDatabase db = database.getWritableDatabase();
        //getContext().getContentResolver().notifyChange(uri, null);
        return db.delete(table, where, args);
    }


    /*
 * update() always returns "no rows affected" (0)
 */
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String whereClause, @Nullable String[] whereArgs) {
        String table = getTableName(uri);
        if (database.equals(NULL)) {
            database = DatabaseHelper.getInstance(getContext());
        }
        SQLiteDatabase db = database.getWritableDatabase();
        //getContext().getContentResolver().notifyChange(uri, null);
        return db.update(table, values, whereClause, whereArgs);
    }

    public static String getTableName(Uri uri){
        String value = uri.getPath();
        value = value.replace("/", "");//we need to remove '/'
        return value;
    }

}

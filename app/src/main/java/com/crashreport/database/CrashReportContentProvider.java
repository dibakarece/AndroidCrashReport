package com.crashreport.database;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;

public class CrashReportContentProvider extends ContentProvider {

    private CrashReportDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new CrashReportDbHelper(context);
        return (dbHelper == null) ? false : true;
    }

    @Override
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) {
        Cursor retCursor = null;

        try {
            SQLiteDatabase readableDatabase = dbHelper.getReadableDatabase();
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

            qb.setTables(DBConstant.CRASHREPORT_TABLE_NAME);
            retCursor = qb.query(readableDatabase, projection, selection, selectionArgs, null, null, sortOrder);

            Context c = getContext();
            if (c != null) {
                ContentResolver cr = c.getContentResolver();
                retCursor.setNotificationUri(cr, uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retCursor;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sort) {
        return query(uri, projection, selection, selectionArgs, sort, null);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        count = db.delete(DBConstant.CRASHREPORT_TABLE_NAME, where, whereArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return DBConstant.CRASHREPORT_CONTENT_TYPE;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        Uri result = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowID = db.insert(DBConstant.CRASHREPORT_TABLE_NAME, null, initialValues);
        if (rowID > 0) {
            result = ContentUris.withAppendedId(DBConstant.CRASHREPORT_CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(result, null);
        }
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        count = db.update(DBConstant.CRASHREPORT_TABLE_NAME, values, where, whereArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

}
/*
 *  This file contains Good Sample Code subject to the Good Dynamics SDK Terms and Conditions.
 *  (c) 2013 Good Technology Corporation. All rights reserved.
 */

package com.crashreport.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CrashReportDbHelper extends SQLiteOpenHelper {

    public CrashReportDbHelper(Context context) {
        super(context, DBConstant.CRASHREPORT_DB_NAME, null, DBConstant.CRASHREPORT_DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(getSQLForCrashTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists" + DBConstant.CRASHREPORT_TABLE_NAME + ";");
        onCreate(db);
    }

    private String getSQLForCrashTable() {
        return "CREATE TABLE IF NOT EXISTS " + DBConstant.CRASHREPORT_TABLE_NAME + " ("
                + DBConstant.CRASHREPORT_FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DBConstant.CRASHREPORT_FIELD_DATE + " TEXT,"
                + DBConstant.CRASHREPORT_FIELD_DETAIL + " TEXT);";
    }


}

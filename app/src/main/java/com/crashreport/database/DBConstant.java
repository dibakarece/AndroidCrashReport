package com.crashreport.database;

import android.content.ContentResolver;
import android.net.Uri;


public final class DBConstant {

    public static final String AUTHORITY = "com.crashreport.database.provider";

    public static final String CRASHREPORT_DB_NAME = "CrashReport.db";
    public static final String CRASHREPORT_TABLE_NAME = "CrashReport";
    public static final String CRASHREPORT_FIELD_ID = "_id";
    public static final String CRASHREPORT_FIELD_DATE = "date";
    public static final String CRASHREPORT_FIELD_DETAIL = "detail";

    public static final int CRASHREPORT_DB_VERSION = 1;
    public static final Uri CRASHREPORT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CRASHREPORT_TABLE_NAME);
    public static final String CRASHREPORT_CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + CRASHREPORT_TABLE_NAME;
    public static final String[] PROJECTTION = new String[]{CRASHREPORT_FIELD_ID, CRASHREPORT_FIELD_DATE, CRASHREPORT_FIELD_DETAIL};
}


package com.crashreport.handler;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.crashreport.database.DBConstant;


public class CrashPostIntentService extends IntentService {
    public static final String TAG = CrashPostIntentService.class.getSimpleName();
    public Context context;

    public CrashPostIntentService() {
        super("CrashPostIntentService");
        context = CrashPostIntentService.this;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            Log.i(TAG, "Hello Andorid");
            Bundle bundle = intent.getExtras();
            storeCrash(bundle.getString(DBConstant.CRASHREPORT_FIELD_DATE), bundle.getString(DBConstant.CRASHREPORT_FIELD_DETAIL));
        }
    }

    private void storeCrash(final String date, final String details) {
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBConstant.CRASHREPORT_FIELD_DATE, date);
            contentValues.put(DBConstant.CRASHREPORT_FIELD_DETAIL, details);
            Uri uri = context.getContentResolver().insert(DBConstant.CRASHREPORT_CONTENT_URI, contentValues);
            Log.i(TAG, uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, e.getMessage());
        }
    }
}

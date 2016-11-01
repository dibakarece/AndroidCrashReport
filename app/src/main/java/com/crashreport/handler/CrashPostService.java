package com.crashreport.handler;

import android.app.IntentService;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crashreport.CrashDetails;
import com.crashreport.database.DBConstant;

import java.util.ArrayList;


public class CrashPostService extends Service {
    public static final String TAG = CrashPostService.class.getSimpleName();
    public Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = CrashPostService.this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        viewCrashes();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void viewCrashes() {
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

            private ArrayList<CrashDetails> list = null;

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    while (true) {
                        Cursor cursor = context.getContentResolver().query(DBConstant.CRASHREPORT_CONTENT_URI, DBConstant.PROJECTTION, null, null, null);
                        if (cursor != null && cursor.getCount() >= 1) {
                            Log.i(TAG, "Count: " + cursor.getCount());
                        }
                        Thread.sleep(10 * 1000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, e.getMessage());
                }
                return null;
            }
        };
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}

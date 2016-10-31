package com.crashreport;


import android.app.Application;

import com.crashreport.handler.CrashHandler;

public class CrashApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CrashHandler.getInstance(CrashApp.this).startCrashTracking();
    }
}

package com.crashreport;


import android.app.Application;
import android.content.Intent;

import com.crashreport.handler.CrashHandler;
import com.crashreport.handler.CrashPostService;

public class CrashApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CrashHandler.getInstance(CrashApp.this).startCrashTracking();
//        startService(new Intent(CrashApp.this, CrashPostService.class));
    }
}

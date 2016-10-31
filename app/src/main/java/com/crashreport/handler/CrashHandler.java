package com.crashreport.handler;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.crashreport.database.DBConstant;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    public static final String TAG = CrashHandler.class.getSimpleName();
    public static CrashHandler instance;
    private Context context;

    public static CrashHandler getInstance(Context mContext) {
        synchronized (CrashHandler.class) {
            if (instance == null) {
                instance = new CrashHandler();
                instance.context = mContext;
            }
        }
        return instance;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        try {
            processReport(throwable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startCrashTracking() {
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private void processReport(Throwable e) throws Exception {

        StringBuilder finalReportBuffer = new StringBuilder();
        finalReportBuffer.append("===============/*/ Crash Report /*/=============");
        finalReportBuffer.append("\n\n====/*/ Crash Report Generated on : ").append(new Date().toString());
        finalReportBuffer.append("\n\n===============/*/ Device Information: /*/=============\n\n").append(getDeviceInfo());
        finalReportBuffer.append("\n\n===============/*/ Crash Causes /*/=============\n\n");
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        finalReportBuffer.append(result.toString());
        finalReportBuffer.append("\n\n===============/*/ Crash Tracked /*/=============\n\n");

        Log.e(TAG, finalReportBuffer.toString());
//        storeCrash(finalReportBuffer.toString(), new Date().toString());

        Intent intent = new Intent(context, CrashPostIntentService.class);
//        intent.putExtra(DBConstant.CRASHREPORT_FIELD_DATE, new Date().toString());
//        intent.putExtra(DBConstant.CRASHREPORT_FIELD_DETAIL, finalReportBuffer.toString());
        context.startService(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    private String getDeviceInfo() {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            StringBuilder reportBuffer = new StringBuilder();
            reportBuffer.append("\nVERSION		: ").append(packageInfo.versionName);
            reportBuffer.append("\nPACKAGE      : ").append(packageInfo.packageName);

            reportBuffer.append("\nFILE-PATH    : ").append("");
            reportBuffer.append("\nPHONE-MODEL  : ").append(Build.MODEL);
            reportBuffer.append("\nANDROID_VERS : ").append(Build.VERSION.RELEASE);
            reportBuffer.append("\nBOARD        : ").append(Build.BOARD);
            reportBuffer.append("\nBRAND        : ").append(Build.BRAND);
            reportBuffer.append("\nDEVICE       : ").append(Build.DEVICE);
            reportBuffer.append("\nDISPLAY      : ").append(Build.DISPLAY);
            reportBuffer.append("\nFINGER-PRINT : ").append(Build.FINGERPRINT);
            reportBuffer.append("\nHOST         : ").append(Build.HOST);
            reportBuffer.append("\nID           : ").append(Build.ID);
            reportBuffer.append("\nMODEL        : ").append(Build.MODEL);
            reportBuffer.append("\nPRODUCT      : ").append(Build.PRODUCT);
            reportBuffer.append("\nMANUFACTURER : ").append(Build.TAGS);
            reportBuffer.append("\nTAGS         : ").append(Build.MODEL);
            reportBuffer.append("\nTIME         : ").append(Build.TIME);
            reportBuffer.append("\nTYPE         : ").append(Build.TYPE);
            reportBuffer.append("\nUSER         : ").append(Build.USER);

//            infoStringBuffer.append("\nTOTAL-INTERNAL-MEMORY     : ").append(getTotalInternalMemorySize() + " mb");
//            infoStringBuffer.append("\nAVAILABLE-INTERNAL-MEMORY : ").append(getAvailableInternalMemorySize() + " mb");

            return reportBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    private void storeCrash(final String details, final String date) {
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


//    ===============/*/ Crash Report /*/=============
//
//            ====/*/ Crash Report Generated on : Mon Oct 31 19:24:34 GMT+05:30 2016
//
//                                                             ===============/*/ Device Information: /*/=============
//
//
//                                                             VERSION		: 1.0
//                                                             PACKAGE      : com.crashreport
//                                                             FILE-PATH    :
//                                                             PHONE-MODEL  : XT1033
//                                                             ANDROID_VERS : 5.1
//                                                             BOARD        : MSM8226
//                                                             BRAND        : motorola
//                                                             DEVICE       : falcon_umtsds
//                                                             DISPLAY      : LPBS23.13-56-2
//                                                             FINGER-PRINT : motorola/falcon_asia_ds/falcon_umtsds:5.1/LPBS23.13-56-2/2:user/release-keys
//                                                             HOST         : ilclbld30
//                                                             ID           : LPBS23.13-56-2
//                                                             MODEL        : XT1033
//                                                             PRODUCT      : falcon_asia_ds
//                                                             MANUFACTURER : release-keys
//                                                             TAGS         : XT1033
//                                                             TIME         : 1456513470000
//                                                             TYPE         : user
//                                                             USER         : hudsoncm
//
//                                                             ===============/*/ Crash Causes /*/=============
//
//                                                             java.lang.RuntimeException: Unable to instantiate service com.crashreport.handler.CrashPostIntentService: java.lang.InstantiationException: class com.crashreport.handler.CrashPostIntentService has no zero argument constructor
//                                                                 at android.app.ActivityThread.handleCreateService(ActivityThread.java:2776)
//                                                                 at android.app.ActivityThread.access$1800(ActivityThread.java:155)
//                                                                 at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1400)
//                                                                 at android.os.Handler.dispatchMessage(Handler.java:102)
//                                                                 at android.os.Looper.loop(Looper.java:135)
//                                                                 at android.app.ActivityThread.main(ActivityThread.java:5343)
//                                                                 at java.lang.reflect.Method.invoke(Native Method)
//                                                                 at java.lang.reflect.Method.invoke(Method.java:372)
//                                                                 at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:905)
//                                                                 at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:700)
//                                                              Caused by: java.lang.InstantiationException: class com.crashreport.handler.CrashPostIntentService has no zero argument constructor
//                                                                 at java.lang.Class.newInstance(Class.java:1597)
//                                                                 at android.app.ActivityThread.handleCreateService(ActivityThread.java:2773)
//                                                                 at android.app.ActivityThread.access$1800(ActivityThread.java:155) 
//                                                                 at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1400) 
//                                                                 at android.os.Handler.dispatchMessage(Handler.java:102) 
//                                                                 at android.os.Looper.loop(Looper.java:135) 
//                                                                 at android.app.ActivityThread.main(ActivityThread.java:5343) 
//                                                                 at java.lang.reflect.Method.invoke(Native Method) 
//                                                                 at java.lang.reflect.Method.invoke(Method.java:372) 
//                                                                 at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:905) 
//                                                                 at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:700) 
//                                                              Caused by: java.lang.NoSuchMethodException: <init> []
//                                                                 at java.lang.Class.getConstructor(Class.java:531)
//                                                                 at java.lang.Class.getDeclaredConstructor(Class.java:510)
//                                                                 at java.lang.Class.newInstance(Class.java:1595)
//                                                                 at android.app.ActivityThread.handleCreateService(ActivityThread.java:2773) 
//                                                                 at android.app.ActivityThread.access$1800(ActivityThread.java:155) 
//                                                                 at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1400) 
//                                                                 at android.os.Handler.dispatchMessage(Handler.java:102) 
//                                                                 at android.os.Looper.loop(Looper.java:135) 
//                                                                 at android.app.ActivityThread.main(ActivityThread.java:5343) 
//                                                                 at java.lang.reflect.Method.invoke(Native Method) 
//                                                                 at java.lang.reflect.Method.invoke(Method.java:372) 
//                                                                 at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:905) 
//                                                                 at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:700) 
//
//
//                                                             ===============/*/ Crash Tracked /*/=============

}

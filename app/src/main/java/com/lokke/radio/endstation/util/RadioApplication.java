package com.lokke.radio.endstation.util;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;


public class RadioApplication extends Application {
    //    private static final String ONESIGNAL_APP_ID = "d4f84244-5eca-45e4-92fb-e52e40b41c55";
    private static final String ONESIGNAL_APP_ID = "fde169c5-4bcf-497b-ae92-d7fc412568f0";
//    @Override
//    public void onCreate() {
//        super.onCreate();

    //        // Logging set to help debug issues, remove before releasing your app.
//        if (BuildConfig.DEBUG)
//            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
//
//        // OneSignal Initialization
//        OneSignal.startInit(this)
//                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
//                .unsubscribeWhenNotificationsAreDisabled(true)
//                .init();
//    }
    @Override
    public void onCreate() {
        super.onCreate();

        // Verbose Logging set to help debug issues, remove before releasing your app.
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);

        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID);

        MobileAds.initialize(this.getApplicationContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Log.d(Constants.TAG, "onInitializationComplete: " + initializationStatus);
            }
        });


        // requestPermission will show the native Android notification permission prompt.
        // NOTE: It's recommended to use a OneSignal In-App Message to prompt instead.

//        OneSignal.getNotifications().requestPermission(true, new Continuation<Boolean>() {
//            @NonNull
//            @Override
//            public CoroutineContext getContext() {
//                return null;
//            }
//
//            @Override
//            public void resumeWith(@NonNull Object o) {
//
//            }
//        });
//        OneSignal.getNotifications().requestPermission(Continue.with(r -> {
//            if (r.isSuccess()) {
//                if (r.getData()) {
//                    // `requestPermission` completed successfully and the user has accepted permission
//                } else {
//                    // `requestPermission` completed successfully but the user has rejected permission
//                }
//            } else {
//                // `requestPermission` completed unsuccessfully, check `r.getThrowable()` for more info on the failure reason
//            }
//        }
//        )
//        );
    }

}

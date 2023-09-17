package com.lokke.radio.endstation.ui.radio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;


import com.lokke.radio.endstation.data.network.responses.Radio;
import com.lokke.radio.endstation.util.Constants;

import org.greenrobot.eventbus.EventBus;

public class RadioManager {

    private static RadioManager instance = null;

    private RadioService service;

    private Context context;

    private boolean serviceBound;

    MetadataListener callback;

    private static final String TAG = "RadioManager";

    private RadioManager(Context context) {
        this.context = context;
        serviceBound = false;
    }

    public static RadioManager with(Context context) {
        if (instance == null) instance = new RadioManager(context);


        return instance;
    }

    public RadioService getService() {
        if (instance != null) {
            return service;
        }
        return null;
    }

    public boolean isPlaying() {
        return service.isPlaying();
    }

    public void play(Radio radio) {
        service.play(radio);
    }

    public void playOrPause(Radio radio) {
        service.playOrPause(radio);
    }

    public void stopPlayer() {
        if (service != null)
            service.stopPlayer();
    }

    public void bind(MetadataListener callback) {
        Log.d(Constants.TAG, getClass().getSimpleName() + " bind: ");
        Log.d(Constants.TAG, getClass().getSimpleName() + " bind : " + callback);
        Log.d(Constants.TAG, getClass().getSimpleName() + " bind : " + serviceConnection);
        this.callback = callback;
        Intent intent = new Intent(context, RadioService.class);
        boolean isBind = context.getApplicationContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.d(Constants.TAG, getClass().getSimpleName() + " context.bindService: " + isBind);
        if (service != null)
            EventBus.getDefault().post(service.getStatus());
    }

    public void unbind() {
        Log.d(Constants.TAG, getClass().getSimpleName() + "unbind: called");
        // Check if the service is bound before attempting to unbind it
        if (serviceBound) {
            context.getApplicationContext().unbindService(serviceConnection);
            // Stop the service explicitly
            Intent intent = new Intent(context, RadioService.class);
            context.getApplicationContext().stopService(intent);
            serviceBound = false;
        }
//        Intent intent = new Intent(context, RadioService.class);
//        context.stopService(intent);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder binder) {
            Log.d(TAG, "onServiceDisconnected: ");
            service = ((RadioService.LocalBinder) binder).getService();
            serviceBound = true;
            service.setCallbacks(callback);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d(TAG, "onServiceDisconnected: ");
            serviceBound = false;
        }
    };

}

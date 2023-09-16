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

    private static RadioService service;

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

    public static RadioService getService() {
        return service;
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
        this.callback = callback;
        Intent intent = new Intent(context, RadioService.class);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        if (service != null)
            EventBus.getDefault().post(service.getStatus());
    }

    public void unbind() {
        Log.d(Constants.TAG, getClass().getSimpleName() + "unbind: called");
        context.unbindService(serviceConnection);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder binder) {
            service = ((RadioService.LocalBinder) binder).getService();
            serviceBound = true;
            service.setCallbacks(callback);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBound = false;
        }
    };

}

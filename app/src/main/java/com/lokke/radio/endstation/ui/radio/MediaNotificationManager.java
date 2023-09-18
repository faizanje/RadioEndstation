package com.lokke.radio.endstation.ui.radio;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lokke.radio.endstation.R;
import com.lokke.radio.endstation.ui.main.MainActivity;


public class MediaNotificationManager {
    public static final int NOTIFICATION_ID = 555;
    private final String PRIMARY_CHANNEL = "PRIMARY_CHANNEL_ID";
    private final String PRIMARY_CHANNEL_NAME = "PRIMARY";

    private RadioService service;


    String mRadioName;

    private Resources resources;

    private NotificationManagerCompat notificationManager;
    private static final String TAG = "NotificationManager";

    public MediaNotificationManager(RadioService service) {
        Log.d(TAG, "MediaNotificationManager: service wala");

        this.service = service;
        this.resources = service.getResources();

        notificationManager = NotificationManagerCompat.from(service);
    }


    public void startNotify(String playbackStatus, String title, String albumUri) {

        final Bitmap[] largeIcon = new Bitmap[1];

        try {


            Glide.with(service.getApplicationContext())
                    .asBitmap()
//                    .load(service.getRadio().getImage())
                    .load(albumUri != null ? albumUri : service.getRadio().getImage())
                    .into(new CustomTarget<Bitmap>(100,100) {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            largeIcon[0] = resource;
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            largeIcon[0] = BitmapFactory.decodeResource(resources, R.drawable.app_icon);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            largeIcon[0] = BitmapFactory.decodeResource(resources, R.drawable.app_icon);
        }


        int icon = R.drawable.exo_icon_pause;
        Intent playbackAction = new Intent(service, RadioService.class);
        playbackAction.setAction(RadioService.ACTION_PAUSE);
        PendingIntent action = PendingIntent.getService(service, 1, playbackAction, getFlags());

        if (playbackStatus.equals(PlaybackStatus.PAUSED)) {

            icon = R.drawable.exo_icon_play;
            playbackAction.setAction(RadioService.ACTION_PLAY);
            action = PendingIntent.getService(service, 2, playbackAction, getFlags());

        }

        Intent stopIntent = new Intent(service, RadioService.class);
        stopIntent.setAction(RadioService.ACTION_STOP);
        PendingIntent stopAction = PendingIntent.getService(service, 3, stopIntent, getFlags());


        Intent intent = new Intent(service, MainActivity.class);

        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        PendingIntent pendingIntent = PendingIntent.getActivity(service, 0, intent, getFlags());

        notificationManager.cancel(NOTIFICATION_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL, PRIMARY_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(service, PRIMARY_CHANNEL)
                .setAutoCancel(false)
//                .setContentTitle(service.getRadio().getName())
                .setContentTitle(title)
                .setContentText("ON AIR")
                .setLargeIcon(largeIcon[0])
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.app_icon)
                .addAction(icon, "pause", action)
                .addAction(R.drawable.ic_close, "stop", stopAction)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis())
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(service.getMediaSession().getSessionToken())
                        .setShowActionsInCompactView(0, 1)
                        .setShowCancelButton(true)
                        .setCancelButtonIntent(stopAction));

        service.startForeground(NOTIFICATION_ID, builder.build());
    }

    private int getFlags() {
        return isAndroidSOrGreater() ? PendingIntent.FLAG_IMMUTABLE : PendingIntent.FLAG_UPDATE_CURRENT;
    }

    public void cancelNotify() {
        service.stopForeground(true);
    }

    public boolean isAndroidSOrGreater(){
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S;
    }
}

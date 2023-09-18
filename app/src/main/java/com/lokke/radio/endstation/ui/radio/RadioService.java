package com.lokke.radio.endstation.ui.radio;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.MetadataOutput;
import com.google.android.exoplayer2.metadata.icy.IcyInfo;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.lokke.radio.endstation.data.network.responses.Radio;
import com.lokke.radio.endstation.util.Constants;


import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RadioService extends Service implements Player.EventListener, AudioManager.OnAudioFocusChangeListener, MetadataOutput {

    public static final String ACTION_PLAY = "com.mcakir.radio.player.ACTION_PLAY";
    public static final String ACTION_PAUSE = "com.mcakir.radio.player.ACTION_PAUSE";
    public static final String ACTION_STOP = "com.mcakir.radio.player.ACTION_STOP";

    private final IBinder iBinder = new LocalBinder();
    MetadataListener callbacks;
    List<MetadataListener> callbacksList;

    private SimpleExoPlayer exoPlayer;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    private boolean onGoingCall = false;
    private TelephonyManager telephonyManager;

    private WifiManager.WifiLock wifiLock;

    private AudioManager audioManager;

    private MediaNotificationManager notificationManager;

    private String status;


    private Radio radio;
    private String streamUrl;
    public String radioName;


    String currentTimeText, currentAlbumArtUrl;

    private static final String TAG = "asd";


    public class LocalBinder extends Binder {
        public RadioService getService() {
            return RadioService.this;
        }
    }

    private final BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            pause();
        }
    };

    private final PhoneStateListener phoneStateListener = new PhoneStateListener() {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {

            if (state == TelephonyManager.CALL_STATE_OFFHOOK
                    || state == TelephonyManager.CALL_STATE_RINGING) {

                if (!isPlaying()) return;

                onGoingCall = true;
                stop();

            } else if (state == TelephonyManager.CALL_STATE_IDLE) {

                if (!onGoingCall) return;

                onGoingCall = false;
                resume();
            }
        }
    };

    private final MediaSessionCompat.Callback mediasSessionCallback = new MediaSessionCompat.Callback() {
        @Override
        public void onPause() {
            super.onPause();

            pause();
        }

        @Override
        public void onStop() {
            super.onStop();
            Log.d(TAG, "MediaSessionCompat: onStop");
            stop();

            notificationManager.cancelNotify();
        }

        @Override
        public void onPlay() {
            super.onPlay();
            resume();
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, getClass().getSimpleName() + " onBind");
        return iBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, getClass().getSimpleName() + " onCreate: ");


        init();
    }

    private void init() {
        onGoingCall = false;

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        notificationManager = new MediaNotificationManager(this);

        wifiLock = ((WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mcScPAmpLock");

        mediaSession = new MediaSessionCompat(this, getClass().getSimpleName());
        transportControls = mediaSession.getController().getTransportControls();
        mediaSession.setActive(true);
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, radioName)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, radioName)
                .build());
        mediaSession.setCallback(mediasSessionCallback);


        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);


        exoPlayer = new SimpleExoPlayer.Builder(getApplicationContext()).build();
        exoPlayer.addListener(this);

        registerReceiver(becomingNoisyReceiver, new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY));

        status = PlaybackStatus.IDLE;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();

        if (TextUtils.isEmpty(action))
            return START_NOT_STICKY;

        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {

            stop();

            return START_NOT_STICKY;
        }

        if (action.equalsIgnoreCase(ACTION_PLAY)) {

            transportControls.play();

        } else if (action.equalsIgnoreCase(ACTION_PAUSE)) {

            transportControls.pause();

        } else if (action.equalsIgnoreCase(ACTION_STOP)) {

            transportControls.stop();

        }

        return START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, getClass().getSimpleName() + " + onUnbind: called");
        if (status.equals(PlaybackStatus.IDLE)) {
            stopSelf();
        }


        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(final Intent intent) {
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: RadioService");

        pause();


        exoPlayer.release();
        exoPlayer.removeListener(this);
        exoPlayer = null;

        if (telephonyManager != null)
            telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);

        notificationManager.cancelNotify();

        mediaSession.release();

        unregisterReceiver(becomingNoisyReceiver);

        super.onDestroy();

    }

    @Override
    public void onAudioFocusChange(int focusChange) {

        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:

                exoPlayer.setVolume(0.8f);

                resume();

                break;

            case AudioManager.AUDIOFOCUS_LOSS:

                stop();

                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:

                if (isPlaying()) pause();

                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:

                if (isPlaying())
                    exoPlayer.setVolume(0.1f);

                break;
        }

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        switch (playbackState) {
            case Player.STATE_BUFFERING:
                status = PlaybackStatus.LOADING;
                break;
            case Player.STATE_ENDED:
                status = PlaybackStatus.STOPPED;
                break;
            case Player.STATE_READY:
                status = playWhenReady ? PlaybackStatus.PLAYING : PlaybackStatus.PAUSED;
                break;
            case Player.STATE_IDLE:
            default:
                status = PlaybackStatus.IDLE;
                break;
        }

        Log.d(TAG, "onPlayerStateChanged: status" + status);
        if (!status.equals(PlaybackStatus.IDLE)) {
            notificationManager.startNotify(status, currentTimeText,currentAlbumArtUrl);
        }

        EventBus.getDefault().post(status);
    }

    @Override
    public void onTimelineChanged(@NonNull  Timeline timeline, int reason) {

    }

    @Override
    public void onTracksChanged(@NonNull TrackGroupArray trackGroups, @NonNull TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerError(@NonNull ExoPlaybackException error) {

        EventBus.getDefault().post(PlaybackStatus.ERROR);
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(@NonNull PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    public void play(Radio radio) {
        this.radio = radio;
        Log.d(Constants.TAG, "play: " + radio.toString());
        this.streamUrl = radio.getStreamingLink();
        Log.e(TAG, "play: url ----> " + streamUrl);
        Log.e(TAG, "exoplayer:" + exoPlayer);

        if (wifiLock != null && !wifiLock.isHeld()) {

            wifiLock.acquire();

        }
//        if (exoPlayer == null) {
//            init();
//        }
        exoPlayer.addMetadataOutput(this);
        exoPlayer.prepare(buildMediaSource(streamUrl));
        exoPlayer.setPlayWhenReady(true);
    }

    private MediaSource buildMediaSource(String url) {
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, "Radio Endstation App 3.0.3");

        if (streamUrl.contains(".m3u8")) {
            return new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(url));
        } else {
            return new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(url));
        }

    }

    public void setCallbacks(MetadataListener callbacks) {
        this.callbacks = callbacks;
    }


    @Override
    public void onMetadata(Metadata metadata) {
        Log.e("Metadata is --->>>", metadata.toString());
        for (int i = 0; i < metadata.length(); i++) {
            Metadata.Entry entry = metadata.get(i);
            // Log.e("onMetadata: IcyInfo", entry.toString());
            if (entry instanceof IcyInfo) {
                currentTimeText = ((IcyInfo) entry).title;
                currentAlbumArtUrl = ((IcyInfo) entry).url;

                callbacks.onMetadataUpdated(((IcyInfo) entry).title, ((IcyInfo) entry).url);
            } else {
                try {
                    currentTimeText = getArtistAndContent(metadata.toString());
                    currentAlbumArtUrl = getAlbumArt(metadata.toString());

                    callbacks.onMetadataUpdated(getArtistAndContent(metadata.toString()), getAlbumArt(metadata.toString()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        notificationManager.startNotify(status, currentTimeText,currentAlbumArtUrl);
    }

    private String getArtistAndContent(String metaString) throws Exception {

        String firstString = "";
        String secondString = "";
        Pattern p = Pattern.compile("(TIT2.*value)=(.*)(TPE1)");
        Matcher m;

        m = p.matcher(metaString);
        if (m.find()) {
            firstString = m.group(2).trim();
            if (firstString.endsWith(",")) {
                firstString = firstString.substring(0, firstString.length() - 1);
            }
            Log.e("First value", " -> " + firstString);
        }

        Pattern p1 = Pattern.compile("(TPE1.*value)=(.*)(WXXX)");
        Matcher m1;

        m1 = p1.matcher(metaString);
        if (m1.find()) {
            secondString = Objects.requireNonNull(m1.group(2)).trim();
            if (secondString.endsWith(",")) {
                secondString = secondString.substring(0, secondString.length() - 1);
            }
            Log.e("Second value", " -> " + secondString);
        }

        String fullString = "null";

        try {
            fullString = firstString + " - " + secondString;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fullString;
    }

    private String getAlbumArt(String metaString) throws Exception {

        if (!metaString.contains("amgArtworkURL"))
            return "";

        String albumArt = "";
        Pattern p = Pattern.compile("(.*amgArtworkURL=\")(.*)(\"\\slength)");
        Matcher m;

        m = p.matcher(metaString);
        if (m.find()) {
            albumArt = m.group(2).trim();
            Log.e("Album Art", " -> " + albumArt);
        }

        String url = "";

        try {
            url = albumArt;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return url;
    }


    public void resume() {

        if (radio != null)
            play(radio);
    }


    public void pause() {

        exoPlayer.setPlayWhenReady(false);

        audioManager.abandonAudioFocus(this);
        wifiLockRelease();
    }

    public void stop() {

        exoPlayer.stop();

        audioManager.abandonAudioFocus(this);
        wifiLockRelease();
    }

    public void playOrPause(Radio radio) {
        Log.d(Constants.TAG, "playOrPause: RadioService");
        if (!isPlaying()) {
            Log.d(Constants.TAG, "playOrPause: RadioService !isPlaying true");
            play(radio);
        } else {
            Log.d(Constants.TAG, "playOrPause: RadioService !isPlaying false");
            pause();
        }
    }


    public String getStatus() {
        return status;
    }

    public MediaSessionCompat getMediaSession() {

        return mediaSession;
    }

    public boolean isPlaying() {

        return this.status.equals(PlaybackStatus.PLAYING);
    }

    private void wifiLockRelease() {

        if (wifiLock != null && wifiLock.isHeld()) {

            wifiLock.release();
        }
    }


    public Radio getRadio() {
        Log.d(TAG, "getRadio: " + radio.toString());
        return radio;
    }

    public void stopPlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            notificationManager.cancelNotify();
        }
    }
}

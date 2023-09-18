package com.lokke.radio.endstation.ui.main;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lokke.radio.endstation.R;
import com.lokke.radio.endstation.data.network.responses.Radio;
import com.lokke.radio.endstation.data.network.responses.Response;
import com.lokke.radio.endstation.data.repositories.MainActivityRepository;
import com.lokke.radio.endstation.ui.player.TimerDialog;
import com.lokke.radio.endstation.ui.radio.MetadataListener;
import com.lokke.radio.endstation.ui.radio.RadioManager;
import com.lokke.radio.endstation.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivityViewModel extends ViewModel {


    private RadioManager radioManager;
    private MutableLiveData<Response> reportResponseLiveData = new MutableLiveData<>();
    public MutableLiveData<Radio> radioObjectLiveData;
    private MainActivityRepository repository;
    private MutableLiveData<String> timerText = new MutableLiveData<>();
    public Radio radio;
    private boolean isTimerSet = false;
    Handler handler = new Handler();
    Runnable r = () -> {
        isTimerSet = false;
        timerText.setValue("none");
        stopPlayer();
    };

    public MainActivityViewModel(Context context, MainActivityRepository repository) {
        radioManager = RadioManager.with(context);
//        radioManager = new RadioManager(context);
        this.repository = repository;
        radioObjectLiveData = repository.getRadio();
    }

    public String setGreetingText() {

        Calendar cal = Calendar.getInstance();
        int calendarHour = cal.get(Calendar.HOUR_OF_DAY);

        if (calendarHour >= 12 && calendarHour <= 16) {
            return "Good Afternoon,";
        } else if (calendarHour >= 17 && calendarHour <= 20) {
            return "Good Evening,";
        } else if (calendarHour >= 21 && calendarHour <= 23) {
            return "Good Night,";
        } else {
            return "Good Morning,";
        }

    }

    public String setDateText() {
        return new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(new Date());
    }


    public void onFacebookClicked(Context context) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(context);
        facebookIntent.setData(Uri.parse(facebookUrl));
        context.startActivity(facebookIntent);
    }

    //method to get the right URL to use in the intent
    public static String getFacebookPageURL(Context context) {
        String FB_PAGE_USERNAME = context.getResources().getString(R.string.fb_page_username);
        String FACEBOOK_PAGE_ID = context.getResources().getString(R.string.fb_page_id);
        ;
        String FACEBOOK_URL = "https://m.facebook.com/" + FB_PAGE_USERNAME;

        PackageManager packageManager = context.getPackageManager();
        try {
            boolean activated = packageManager.getApplicationInfo("com.facebook.katana", 0).enabled;
            if (activated) {
                return "fb://page/" + FACEBOOK_PAGE_ID;
            } else {
                return FACEBOOK_URL;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

    public void onInstagramClicked(Context context) {
        String instagramUser = context.getString(R.string.instagram_username);
        Uri uri = Uri.parse("http://instagram.com/_u/" + instagramUser);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            context.startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/" + instagramUser)));
        }
    }

    public void onTwitterClicked(Context context) {

        String twitter_user_name = context.getResources().getString(R.string.twitter_username);
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + twitter_user_name)));
        } catch (Exception e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/#!/" + twitter_user_name)));
        }
    }

    public void onWhatsAppClicked(Context context) {
        String contact = context.getResources().getString(R.string.whatsApp_number); // use country code with your phone number
        String url = "https://chat.whatsapp.com/" + contact;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            context.startActivity(browserIntent);
            e.printStackTrace();
        }
    }

    public void onReportStreamClicked(View view) {

        final String appPackageName = view.getContext().getPackageName();
        try {
            view.getContext().startActivity(new Intent(
                    Intent.ACTION_VIEW, Uri
                    .parse("market://details?id="
                            + appPackageName)));
        } catch (android.content.ActivityNotFoundException exception) {
            view.getContext().startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id="
                            + appPackageName)));
        }

        /*AppUtil.showReportDialog(view.getContext(), new AppUtil.AlertDialogListener() {
            @Override
            public void onPositive() {
                MutableLiveData<Response> flag = repository.reportRadio();
                flag.observe((LifecycleOwner) view.getContext(), response -> reportResponseLiveData.setValue(response));
            }

            @Override
            public void onCancel() {

            }
        });*/
    }

    public void onSetTimerClicked(View view) {
        TimerDialog timerDialog = new TimerDialog(view.getContext(), (time, timeText) -> {
            timerText.setValue(timeText);
            if (time == 0) {
                if (isTimerSet) {
                    isTimerSet = false;
                    handler.removeCallbacksAndMessages(null);

                }
            } else {
                if (isTimerSet) {
                    handler.removeCallbacksAndMessages(null);
                }
                isTimerSet = true;
                handler.postDelayed(r, time);
            }

        });
        timerDialog.show();
    }

    public void onShareClicked(View view, String radioName) {
        final String appPackageName = view.getContext().getPackageName();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Oi! Schau dir " + radioName + " an. Die App findest du hier: https://play.google.com/store/apps/details?id="
                + appPackageName);
        view.getContext().startActivity(Intent.createChooser(shareIntent, "Share Via:"));
    }

    public void onPlayClicked(View view) {

        Log.d(Constants.TAG, "radioManager != null: " + (radioManager != null));
        Log.d(Constants.TAG, "radio != null: " + (radio != null));
        Log.d(Constants.TAG, "radio != null && radioManager != null: " + (radio != null && radioManager != null));
        if (radio != null && radioManager != null) {
            radioManager.playOrPause(radio);
        }

    }

    public LiveData<Response> getReportResponseLiveData() {
        return reportResponseLiveData;
    }

    public LiveData<Radio> getRadioLiveData() {
        return radioObjectLiveData;
    }

    public MutableLiveData<String> getTimerText() {
        return timerText;
    }


    public boolean isPlaying() {
        return radioManager.isPlaying();
    }


    public void bind(MetadataListener callback) {
        Log.d(Constants.TAG, getClass().getSimpleName() + " bind: called");
        radioManager.bind(callback);
    }

    public void unbind() {
        Log.d(Constants.TAG, getClass().getSimpleName() + "unbind: called");
        radioManager.unbind();
    }

    public void stopPlayer() {
        radioManager.stopPlayer();
    }


}

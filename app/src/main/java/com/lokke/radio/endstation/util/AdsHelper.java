package com.lokke.radio.endstation.util;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.lokke.radio.endstation.BuildConfig;
import com.lokke.radio.endstation.R;


public class AdsHelper {
    //This holds
    public static int ADS_LOAD_COUNT = 0;
    InterstitialAd mInterstitialAd;
    public static int SHOW_ADS_WHEN_TAB_COUNT = 1; //change this to the desired number you want.

    public void loadBannerAd(Context context, AdView adView) {

        try {
//            layoutAdContainer.setVisibility(View.GONE);
//            AdView adView = new AdView(context);
//            layoutAdContainer.addView(adView);
//
//            if (BuildConfig.DEBUG)
//                adView.setAdUnitId(context.getResources().getString(R.string.test_banner_ad_id));
//            else adView.setAdUnitId(context.getResources().getString(R.string.live_banner_ad_id));
//
//            adView.setAdSize(AdSize.BANNER);
//            adView.setAdListener(new AdListener() {
//                @Override
//                public void onAdClosed() {
//                    super.onAdClosed();
//                }
//
//                @Override
//                public void onAdFailedToLoad(LoadAdError errorCode) {
//                    super.onAdFailedToLoad(errorCode);
//                }
//
//                @Override
//                public void onAdOpened() {
//                    super.onAdOpened();
//                }
//
//                @Override
//                public void onAdLoaded() {
//                    super.onAdLoaded();
//                    layoutAdContainer.setVisibility(View.VISIBLE);
//                }
//            });

            AdRequest adRequest;
            adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadInterstitialAd(Context context) {
//        String interstitialAdsId;
//        if (BuildConfig.DEBUG)
//            interstitialAdsId = context.getResources().getString(R.string.interestial_test_ad_id);
//        else interstitialAdsId = context.getResources().getString(R.string.interestial_live_ad_id);


        AdRequest adRequest;
        adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, "ca-app-pub-3940256099942544/1033173712", adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                // The mInterstitialAd reference will be null until
                // an ad is loaded.
                mInterstitialAd = interstitialAd;
//                ADS_LOAD_COUNT = 0;
                Log.d(Constants.TAG, "onAdLoaded: ");
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                Log.d(Constants.TAG, "onAdFailedToLoad: ");
                ADS_LOAD_COUNT = 0;
                mInterstitialAd = null;
            }
        });

    }

    public void showInterstitialAd(Activity activity) {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(activity);
        } else {
            Log.d(Constants.TAG, "The interstitial ad wasn't ready yet.");
        }
    }
}

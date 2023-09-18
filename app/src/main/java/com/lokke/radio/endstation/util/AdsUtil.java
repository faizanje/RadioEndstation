package com.lokke.radio.endstation.util;
import static android.content.ContentValues.TAG;
import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
public class AdsUtil {

    InterstitialAd mInterstitialAd;
    //This holds
//
//    public static int ADS_LOAD_COUNT = 0;
//    public static int SHOW_ADS_WHEN_TAB_COUNT = 1; //change this to the desired number you want.

    public static void showBannerAd(AdView adView, AdRequest adRequest ){
        adView.loadAd(adRequest);
    }
    public void loadInstertitialAd(Context context, AdRequest adRequest){

        InterstitialAd.load(context,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

//    public static void loadBannerAd(Context context, final LinearLayout layoutAdContainer) {
//
//        try {
//            layoutAdContainer.setVisibility(View.GONE);
//            AdView adView = new AdView(context);
//            layoutAdContainer.addView(adView);
//
//            if (BuildConfig.DEBUG)
//                adView.setAdUnitId(context.getResources().getString(R.string.test_banner_ad_id));
//            else
//                adView.setAdUnitId(context.getResources().getString(R.string.live_banner_ad_id));
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
////                @Override
////                public void onAdLeftApplication() {
////                    super.onAdLeftApplication();
////                }
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
//
//            AdRequest adRequest;
//            adRequest = new AdRequest.Builder().build();
//            adView.loadAd(adRequest);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void loadInterstitialAd(Context context, InterstitialAd mInterstitialAd) {
//
////        if (BuildConfig.DEBUG)
////            mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.interestial_test_ad_id));
////        else
////            mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.interestial_live_ad_id));
//
//        AdRequest adRequest;
//        adRequest = new AdRequest.Builder().build();
//
//        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
//                new InterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded( InterstitialAd interstitialAd) {
//                        // The mInterstitialAd reference will be null until
//                        // an ad is loaded.
////                        mInterstitialAd = interstitialAd;
//                        Log.i(TAG, "onAdLoaded");
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad( LoadAdError loadAdError) {
//                        // Handle the error
//                        Log.d(TAG, loadAdError.toString());
////                        mInterstitialAd = null;
//                    }
//                });
//
////        mInterstitialAd.loadAd(adRequest);
////        mInterstitialAd.setAdListener(new AdListener() {
////            @Override
////            public void onAdClosed() {
////                super.onAdClosed();
////                ADS_LOAD_COUNT = 0;
////                mInterstitialAd.loadAd(adRequest);
////            }
//
//            @Override
//            public void onAdFailedToLoad(LoadAdError errorCode) {
//                super.onAdFailedToLoad(errorCode);
//                ADS_LOAD_COUNT = 0;
//                mInterstitialAd.loadAd(adRequest);
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                super.onAdLeftApplication();
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//            }
//
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//            }
//        });
//
//    }
//
    public static void showInterstitialAd(InterstitialAd mInterstitialAd, Activity activity) {

        if (mInterstitialAd != null) {
            mInterstitialAd.show(activity);
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.");
        }
    }
}

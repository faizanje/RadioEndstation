package com.lokke.radio.endstation.util;


import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.lokke.radio.endstation.BuildConfig;
import com.lokke.radio.endstation.R;


public class AdsUtil {

    //This holds

    public static int ADS_LOAD_COUNT = 0;
    public static int SHOW_ADS_WHEN_TAB_COUNT = 1; //change this to the desired number you want.

    public static void loadBannerAd(Context context, final LinearLayout layoutAdContainer) {

        try {
            layoutAdContainer.setVisibility(View.GONE);
            AdView adView = new AdView(context);
            layoutAdContainer.addView(adView);

            if (BuildConfig.DEBUG)
                adView.setAdUnitId(context.getResources().getString(R.string.test_banner_ad_id));
            else
                adView.setAdUnitId(context.getResources().getString(R.string.live_banner_ad_id));

            adView.setAdSize(AdSize.BANNER);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(LoadAdError errorCode) {
                    super.onAdFailedToLoad(errorCode);
                }

                @Override
                public void onAdLeftApplication() {
                    super.onAdLeftApplication();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    layoutAdContainer.setVisibility(View.VISIBLE);
                }
            });

            AdRequest adRequest;
            adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadInterstitialAd(Context context,InterstitialAd mInterstitialAd) {

        if (BuildConfig.DEBUG)
            mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.interestial_test_ad_id));
        else
            mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.interestial_live_ad_id));

        AdRequest adRequest;
        adRequest = new AdRequest.Builder().build();

        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                ADS_LOAD_COUNT = 0;
                mInterstitialAd.loadAd(adRequest);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError errorCode) {
                super.onAdFailedToLoad(errorCode);
                ADS_LOAD_COUNT = 0;
                mInterstitialAd.loadAd(adRequest);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }
        });

    }

    public static void showInterstitialAd(InterstitialAd mInterstitialAd) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
}

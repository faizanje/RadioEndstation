package com.lokke.radio.endstation.ui.about;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.lokke.radio.endstation.R;

import com.lokke.radio.endstation.databinding.ActivityAboutBinding;
import com.lokke.radio.endstation.util.AdsUtil;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {

    InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        AdView adView = binding.adView;
        AdRequest adRequest = new AdRequest.Builder().build();
//
////        adView.setAdSize(AdSize.BANNER);
//        adView.loadAd(adRequest);
        AdsUtil.showBannerAd(adView,adRequest);
        AdsUtil adsUtil = new AdsUtil();
        adsUtil.loadInstertitialAd(this,adRequest);

//        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");


        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });


//        mInterstitialAd=new InterstitialAd(this);
//        AdsUtil.loadInterstitialAd(this,mInterstitialAd);

        setSupportActionBar(binding.toolbarAbout);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(
                    getPackageName(), 0);
            String version = pInfo.versionName;
            binding.versionTextView.setText(version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        AdsUtil.showInterstitialAd(mInterstitialAd, AboutActivity.this);

//        if (mInterstitialAd != null) {
//            mInterstitialAd.show(AboutActivity.this);
//        } else {
//            Log.d("TAG", "The interstitial ad wasn't ready yet.");
//        }
        super.onBackPressed();
    }
}
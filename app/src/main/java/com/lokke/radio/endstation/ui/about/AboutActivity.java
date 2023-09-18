package com.lokke.radio.endstation.ui.about;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.lokke.radio.endstation.R;
import com.lokke.radio.endstation.databinding.ActivityAboutBinding;
import com.lokke.radio.endstation.util.AdsHelper;

public class AboutActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    AdsHelper adsHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        adsHelper = new AdsHelper();

        adsHelper.loadInterstitialAd(this);
        AdView adView = binding.adView;

        adsHelper.loadBannerAd(this,adView);

//        setSupportActionBar(binding.toolbarAbout);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("");
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adsHelper.showInterstitialAd(AboutActivity.this);
                onBackPressed();
            }
        });


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
        adsHelper.loadInterstitialAd(this);
        adsHelper.showInterstitialAd(this);
        super.onBackPressed();
    }
}
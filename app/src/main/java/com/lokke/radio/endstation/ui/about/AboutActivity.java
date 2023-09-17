package com.lokke.radio.endstation.ui.about;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.gms.ads.InterstitialAd;
import com.lokke.radio.endstation.R;
import com.lokke.radio.endstation.databinding.ActivityAboutBinding;
import com.lokke.radio.endstation.util.AdsUtil;

public class AboutActivity extends AppCompatActivity {

    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAboutBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_about);

        mInterstitialAd=new InterstitialAd(this);
        AdsUtil.loadInterstitialAd(this,mInterstitialAd);

        setSupportActionBar(binding.toolbarAbout);
        getSupportActionBar().setHomeButtonEnabled(true);
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
        AdsUtil.showInterstitialAd(mInterstitialAd);
        super.onBackPressed();
    }
}
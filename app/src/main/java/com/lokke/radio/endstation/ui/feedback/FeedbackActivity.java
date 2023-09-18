package com.lokke.radio.endstation.ui.feedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.lokke.radio.endstation.R;
import com.lokke.radio.endstation.data.repositories.MainActivityRepository;
import com.lokke.radio.endstation.databinding.ActivityFeedbackBinding;
//import com.lokke.radio.endstation.util.AdsUtil;
import com.lokke.radio.endstation.util.AdsUtil;
import com.lokke.radio.endstation.util.AppUtil;

public class FeedbackActivity extends AppCompatActivity {

    private ActivityFeedbackBinding mBinding;
    private FeedbackViewModel mViewModel;
     InterstitialAd mInterstitialAd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityFeedbackBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setUpToolbar();

//        mInterstitialAd = new InterstitialAd(this);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdsUtil adsUtil = new AdsUtil();
        adsUtil.loadInterstitialAd(this,adRequest);


        MainActivityRepository repository = new MainActivityRepository(getApplication());
        FeedbackFactory factory = new FeedbackFactory(repository);
        mViewModel = new ViewModelProvider(this,factory).get(FeedbackViewModel.class);
        mBinding.setViewModel(mViewModel);

        mViewModel.getButtonClick().observe(this, feedback -> {
            if (feedback != null) {

                mViewModel.init(feedback);
                mViewModel.getResponseMutableLiveData().observe(this, response -> {
                    AppUtil.hideKeyboard(this);
                    mBinding.emailTextField.getEditText().setText("");
                    mBinding.subjectTextField.getEditText().setText("");
                    mBinding.messageTextField.getEditText().setText("");
                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    private void setUpToolbar() {
        setSupportActionBar(mBinding.toolbarLayout.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mBinding.toolbarLayout.toolbarTitle.setText(R.string.feedback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        AdsUtil.showInterstitialAd(mInterstitialAd, FeedbackActivity.this);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }
}
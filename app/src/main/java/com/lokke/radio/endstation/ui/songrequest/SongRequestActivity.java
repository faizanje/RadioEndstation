package com.lokke.radio.endstation.ui.songrequest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.lokke.radio.endstation.R;
import com.lokke.radio.endstation.data.repositories.MainActivityRepository;

import com.lokke.radio.endstation.databinding.ActivitySongRequestBinding;
import com.lokke.radio.endstation.ui.feedback.FeedbackFactory;
import com.lokke.radio.endstation.ui.feedback.FeedbackViewModel;
import com.lokke.radio.endstation.util.AdsUtil;
import com.lokke.radio.endstation.util.AppUtil;

public class SongRequestActivity extends AppCompatActivity {

    private ActivitySongRequestBinding mBinding;
    private SongRequestViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySongRequestBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setUpToolbar();

        MainActivityRepository repository = new MainActivityRepository(getApplication());
        SongRequestFactory factory = new SongRequestFactory(repository);
        mViewModel = new ViewModelProvider(this, factory).get(SongRequestViewModel.class);
        mBinding.setViewModel(mViewModel);

        mViewModel.getButtonClick().observe(this, feedback -> {
            if (feedback != null) {

                mViewModel.init(feedback);
                mViewModel.getResponseMutableLiveData().observe(this, response -> {
                    AppUtil.hideKeyboard(this);
                    mBinding.nameTextField.getEditText().setText("");
                    mBinding.request1TextField.getEditText().setText("");
                    mBinding.request2TextField.getEditText().setText("");
                    mBinding.messageTextField.getEditText().setText("");
                    Log.d("faizan", "response: " + response);
//                    Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    private void setUpToolbar() {
        setSupportActionBar(mBinding.toolbarLayout.toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mBinding.toolbarLayout.toolbarTitle.setText(R.string.song_request);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
    }
}
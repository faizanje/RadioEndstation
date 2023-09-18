package com.lokke.radio.endstation.ui.songrequest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.lokke.radio.endstation.R;
import com.lokke.radio.endstation.data.repositories.MainActivityRepository;
import com.lokke.radio.endstation.databinding.ActivitySongRequestBinding;
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
        mViewModel = new ViewModelProvider(this,factory).get(SongRequestViewModel.class);
        mBinding.setViewModel(mViewModel);

       /* mBinding.scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mBinding.scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mBinding.scrollView.fullScroll(View.FOCUS_DOWN);
                    }
                });
            }
        });*/


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
                    Toast.makeText(this, "Your request has been successfully", Toast.LENGTH_SHORT).show();


                    onBackPressed();
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
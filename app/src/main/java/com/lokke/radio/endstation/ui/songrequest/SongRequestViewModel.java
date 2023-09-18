package com.lokke.radio.endstation.ui.songrequest;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lokke.radio.endstation.data.network.responses.Response;
import com.lokke.radio.endstation.data.repositories.MainActivityRepository;
import com.lokke.radio.endstation.ui.feedback.Feedback;

public class SongRequestViewModel extends ViewModel {

    MainActivityRepository mRepository;

    View.OnFocusChangeListener onFocusName;
    View.OnFocusChangeListener onFocusRequest1;
    View.OnFocusChangeListener onFocusRequest2;

    SongRequest songRequest;

    MutableLiveData<SongRequest> buttonClick = new MutableLiveData<>();

    MutableLiveData<Response> mResponseMutableLiveData;

    public SongRequestViewModel(MainActivityRepository repository) {
        mRepository = repository;

        songRequest = new SongRequest();
        onFocusName = (view, focused) -> {
            if (!focused) songRequest.isNameValid(true);
        };

        onFocusRequest1 = (view, focused) -> {
            if (!focused) songRequest.isRequest1Valid(true);
        };

        onFocusRequest2 = (view, focused) -> {
            if (!focused) songRequest.isRequest2Valid(true);
        };
    }

    public void onSubmit(View view) {
        if (songRequest.isValid()) {
            buttonClick.setValue(songRequest);
        }
    }

    public void init(SongRequest songRequest) {
//        if (mResponseMutableLiveData != null) return;
        mResponseMutableLiveData = mRepository.sendSongRequest(songRequest);
    }

    public SongRequest getSongRequest() {
        return songRequest;
    }

    public MutableLiveData<SongRequest> getButtonClick() {
        return buttonClick;
    }

    public View.OnFocusChangeListener getOnFocusName() {
        return onFocusName;
    }

    public View.OnFocusChangeListener getOnFocusRequest1() {
        return onFocusRequest1;
    }

    public View.OnFocusChangeListener getOnFocusRequest2() {
        return onFocusRequest2;
    }

    public MutableLiveData<Response> getResponseMutableLiveData() {
        return mResponseMutableLiveData;
    }
}

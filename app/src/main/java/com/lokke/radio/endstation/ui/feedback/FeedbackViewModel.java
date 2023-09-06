package com.lokke.radio.endstation.ui.feedback;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lokke.radio.endstation.data.network.responses.Response;
import com.lokke.radio.endstation.data.repositories.MainActivityRepository;

public class FeedbackViewModel extends ViewModel {
    MainActivityRepository mRepository;

    View.OnFocusChangeListener onFocusEmail;
    View.OnFocusChangeListener onFocusSubject;
    View.OnFocusChangeListener onFocusMessage;

    Feedback mFeedback;

    MutableLiveData<Feedback> buttonClick = new MutableLiveData<>();

    MutableLiveData<Response> mResponseMutableLiveData;

    public FeedbackViewModel(MainActivityRepository repository) {
        mRepository = repository;

        mFeedback = new Feedback();
        onFocusEmail = (view, focused) -> {
            if (!focused) mFeedback.isEmailValid(true);
        };

        onFocusSubject = (view, focused) -> {
            if (!focused) mFeedback.isSubjectValid(true);
        };

        onFocusMessage = (view, focused) -> {
            if (!focused) mFeedback.isMessageValid(true);
        };
    }

    public void onSubmit(View view) {
        if (mFeedback.isValid()) {
            buttonClick.setValue(mFeedback);
        }
    }

    public void init(Feedback feedback) {
        if (mResponseMutableLiveData != null) return;
        mResponseMutableLiveData = mRepository.sendFeedback(feedback);
    }


    public Feedback getFeedback() {
        return mFeedback;
    }

    public View.OnFocusChangeListener getOnFocusEmail() {
        return onFocusEmail;
    }

    public View.OnFocusChangeListener getOnFocusSubject() {
        return onFocusSubject;
    }

    public View.OnFocusChangeListener getOnFocusMessage() {
        return onFocusMessage;
    }

    public MutableLiveData<Feedback> getButtonClick() {
        return buttonClick;
    }

    public MutableLiveData<Response> getResponseMutableLiveData() {
        return mResponseMutableLiveData;
    }
}

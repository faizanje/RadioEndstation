package com.lokke.radio.endstation.ui.feedback;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lokke.radio.endstation.data.repositories.MainActivityRepository;

public class FeedbackFactory extends ViewModelProvider.NewInstanceFactory {
    MainActivityRepository mRepository;

    public FeedbackFactory(MainActivityRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new FeedbackViewModel(mRepository);
    }
}

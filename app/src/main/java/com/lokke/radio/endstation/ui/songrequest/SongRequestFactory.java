package com.lokke.radio.endstation.ui.songrequest;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lokke.radio.endstation.data.repositories.MainActivityRepository;

public class SongRequestFactory implements ViewModelProvider.Factory {
    MainActivityRepository mRepository;

    public SongRequestFactory(MainActivityRepository repository) {
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SongRequestViewModel(mRepository);
    }
}

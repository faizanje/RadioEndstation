package com.lokke.radio.endstation.ui.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.lokke.radio.endstation.data.repositories.MainActivityRepository;

public class MainFactory implements ViewModelProvider.Factory {
    Context mContext;
    MainActivityRepository mRepository;

    public MainFactory(Context context, MainActivityRepository repository) {
        mContext = context;
        mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(mContext,mRepository);
    }
}

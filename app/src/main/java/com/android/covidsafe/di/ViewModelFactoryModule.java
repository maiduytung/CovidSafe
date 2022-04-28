package com.android.covidsafe.di;

import androidx.lifecycle.ViewModelProvider;

import com.android.covidsafe.viewmodels.ViewModelProviderFactory;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelProviderFactory(ViewModelProviderFactory factory);
}
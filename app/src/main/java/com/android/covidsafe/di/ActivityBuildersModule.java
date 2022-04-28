package com.android.covidsafe.di;

import com.android.covidsafe.MainActivity;
import com.android.covidsafe.SplashActivity;
import com.android.covidsafe.di.auth.AuthFragmentBuildersModule;
import com.android.covidsafe.di.auth.AuthModule;
import com.android.covidsafe.di.auth.AuthScope;
import com.android.covidsafe.di.auth.AuthViewModelModule;
import com.android.covidsafe.di.main.MainFragmentBuildersModule;
import com.android.covidsafe.di.main.MainModule;
import com.android.covidsafe.di.main.MainScope;
import com.android.covidsafe.di.main.MainViewModelModule;
import com.android.covidsafe.ui.auth.AuthActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector
    abstract SplashActivity contributeSplashActivity();

    @AuthScope
    @ContributesAndroidInjector(
            modules = {AuthFragmentBuildersModule.class, AuthViewModelModule.class, AuthModule.class}
    )
    abstract AuthActivity contributeAuthActivity();

    @MainScope
    @ContributesAndroidInjector(
            modules = {MainFragmentBuildersModule.class, MainViewModelModule.class, MainModule.class}
    )
    abstract MainActivity contributeMainActivity();

}
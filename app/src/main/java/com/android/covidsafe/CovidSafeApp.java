package com.android.covidsafe;

import android.os.Build;

import com.android.covidsafe.di.DaggerAppComponent;
import com.google.android.material.color.DynamicColors;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

public class CovidSafeApp extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION. SDK_INT >= Build.VERSION_CODES.S ) {
            DynamicColors.applyToActivitiesIfAvailable(this);
        }
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerAppComponent.builder().application(this).build();
//        return null;
    }
}

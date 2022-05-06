package com.android.covidsafe.di;

import android.app.Application;

import com.android.covidsafe.repository.SecureSharedPref;
import com.android.covidsafe.repository.SecureSharedPrefImp;
import com.android.covidsafe.utilities.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
        NetworkModule.class,
        DatabaseModule.class
})
class AppModule {

    @Singleton
    @Provides
    SecureSharedPref provideSecureSharedPref(Application application) {
        return new SecureSharedPrefImp(application, "SecureSharedPref");
    }
}

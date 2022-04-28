package com.android.covidsafe.di;

import android.app.Application;

import com.android.covidsafe.BuildConfig;
import com.android.covidsafe.api.APIService;
import com.android.covidsafe.api.CertificationService;
import com.android.covidsafe.api.HealthDeclarationService;
import com.android.covidsafe.api.ReportService;
import com.android.covidsafe.api.VaccinationService;
import com.android.covidsafe.api.VaccineRegistrationService;
import com.android.covidsafe.repository.ISharedPreferences;
import com.android.covidsafe.utilities.AccessTokenAuthenticator;
import com.android.covidsafe.utilities.AccessTokenInterceptor;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.utilities.LiveDataCallAdapterFactory;

import javax.inject.Singleton;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Singleton
    @Provides
    static AccessTokenInterceptor provideAccessTokenInterceptor(ISharedPreferences sharedPreferences) {
        return new AccessTokenInterceptor(sharedPreferences);
    }

    @Singleton
    @Provides
    static AccessTokenAuthenticator provideAccessTokenAuthenticator(Application application, ISharedPreferences sharedPreferences, Lazy<APIService> webLazyWrapper) {
        return new AccessTokenAuthenticator(application, sharedPreferences, webLazyWrapper);
    }

    @Singleton
    @Provides
    static OkHttpClient provideHttpClient(AccessTokenAuthenticator auth, AccessTokenInterceptor interceptor) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        return httpClient
                .authenticator(auth)
                .addInterceptor(interceptor)
                .build();
    }

    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build();
    }

    @Singleton
    @Provides
    static APIService provideAPIService(Retrofit retrofit) {
        return retrofit.create(APIService.class);
    }

    @Singleton
    @Provides
    static HealthDeclarationService provideHealthDeclarationService(Retrofit retrofit) {
        return retrofit.create(HealthDeclarationService.class);
    }

    @Singleton
    @Provides
    static VaccineRegistrationService provideVaccineRegistrationService(Retrofit retrofit) {
        return retrofit.create(VaccineRegistrationService.class);
    }

    @Singleton
    @Provides
    static ReportService provideReportService(Retrofit retrofit) {
        return retrofit.create(ReportService.class);
    }

    @Singleton
    @Provides
    static VaccinationService provideVaccinationService(Retrofit retrofit) {
        return retrofit.create(VaccinationService.class);
    }

    @Singleton
    @Provides
    static CertificationService provideCertificationService(Retrofit retrofit) {
        return retrofit.create(CertificationService.class);
    }
}

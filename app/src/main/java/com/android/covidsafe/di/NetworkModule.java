package com.android.covidsafe.di;

import android.app.Application;

import com.android.covidsafe.BuildConfig;
import com.android.covidsafe.api.AuthService;
import com.android.covidsafe.api.CertificationService;
import com.android.covidsafe.api.HealthDeclarationService;
import com.android.covidsafe.api.ProfileService;
import com.android.covidsafe.api.ReportService;
import com.android.covidsafe.api.UserService;
import com.android.covidsafe.api.VaccinationService;
import com.android.covidsafe.api.VaccineRegistrationService;
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
    static AccessTokenInterceptor provideAccessTokenInterceptor() {
        return new AccessTokenInterceptor();
    }

    @Singleton
    @Provides
    static AccessTokenAuthenticator provideAccessTokenAuthenticator(Application application, Lazy<AuthService> webLazyWrapper, AccessTokenInterceptor accessTokenInterceptor) {
        return new AccessTokenAuthenticator(application, webLazyWrapper, accessTokenInterceptor);
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
    static AuthService provideAuthService(Retrofit retrofit) {
        return retrofit.create(AuthService.class);
    }

    @Singleton
    @Provides
    static CertificationService provideCertificationService(Retrofit retrofit) {
        return retrofit.create(CertificationService.class);
    }

    @Singleton
    @Provides
    static HealthDeclarationService provideHealthDeclarationService(Retrofit retrofit) {
        return retrofit.create(HealthDeclarationService.class);
    }

    @Singleton
    @Provides
    static ProfileService provideProfileService(Retrofit retrofit) {
        return retrofit.create(ProfileService.class);
    }

    @Singleton
    @Provides
    static ReportService provideReportService(Retrofit retrofit) {
        return retrofit.create(ReportService.class);
    }

    @Singleton
    @Provides
    static UserService provideUserService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

    @Singleton
    @Provides
    static VaccinationService provideVaccinationService(Retrofit retrofit) {
        return retrofit.create(VaccinationService.class);
    }

    @Singleton
    @Provides
    static VaccineRegistrationService provideVaccineRegistrationService(Retrofit retrofit) {
        return retrofit.create(VaccineRegistrationService.class);
    }
}

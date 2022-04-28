package com.android.covidsafe.di;

import com.android.covidsafe.repository.ISharedPreferences;
import com.android.covidsafe.utilities.Constants;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class AuthenticationInterceptor implements Interceptor {

    private ISharedPreferences sharedPreference;
    private String authToken;

    @Inject
    public AuthenticationInterceptor(ISharedPreferences sharedPreference) {
        this.sharedPreference = sharedPreference;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        authToken = sharedPreference.getString(Constants.ACCESS_TOKEN);
        Request request = chain.request();
        String noAuth = request.header("no-auth");

        Request.Builder builder = request.newBuilder();

        if (noAuth == null && authToken != null && !authToken.isEmpty()) {
            builder.header("Authorization",  "Bearer " + authToken);
        }

        request = builder.build();

        return chain.proceed(request);
    }
}
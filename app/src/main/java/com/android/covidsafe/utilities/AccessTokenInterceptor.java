package com.android.covidsafe.utilities;

import com.android.covidsafe.repository.ISharedPreferences;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class AccessTokenInterceptor implements Interceptor {

    private final ISharedPreferences sharedPreferences;

    @Inject
    public AccessTokenInterceptor(ISharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN);

        Request request = chain.request();
        String noAuth = request.header("no-auth");

        Request.Builder builder = request.newBuilder();

        if (noAuth == null && accessToken != null && !accessToken.isEmpty()) {
            builder.header("Authorization",  "Bearer " + accessToken);
        }

        request = builder.build();

        return chain.proceed(request);
    }
}
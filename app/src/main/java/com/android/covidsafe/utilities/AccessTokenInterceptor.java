package com.android.covidsafe.utilities;

import com.android.covidsafe.repository.SecureSharedPref;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

@Singleton
public class AccessTokenInterceptor implements Interceptor {

    private String accessToken;

    @Inject
    public AccessTokenInterceptor() {
    }

    public void setAccessToken(String token) {
        accessToken = token;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        String noAuth = request.header("no-auth");

        Request.Builder builder = request.newBuilder();

        if (noAuth == null && accessToken != null && !accessToken.isEmpty()) {
            builder.header("Authorization", "Bearer " + accessToken);
        }

        request = builder.build();

        return chain.proceed(request);
    }
}
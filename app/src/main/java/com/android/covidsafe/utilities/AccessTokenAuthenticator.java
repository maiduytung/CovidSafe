package com.android.covidsafe.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.covidsafe.api.AuthService;
import com.android.covidsafe.vo.Token;
import com.android.covidsafe.vo.request.RefreshRequest;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

@Singleton
public class AccessTokenAuthenticator implements Authenticator {

    private final Context context;
    private final Lazy<AuthService> webLazyWrapper;
    private final AccessTokenInterceptor accessTokenInterceptor;
    private String accessToken;
    private String newAccessToken;
    private String refreshToken;

    @Inject
    public AccessTokenAuthenticator(Context context, Lazy<AuthService> webLazyWrapper, AccessTokenInterceptor accessTokenInterceptor) {
        this.context = context;
        this.webLazyWrapper = webLazyWrapper;
        this.accessTokenInterceptor = accessTokenInterceptor;
    }

    public void setAccessToken(String token) {
        accessToken = token;
        accessTokenInterceptor.setAccessToken(token);
    }

    public void setRefreshToken(String token) {
        refreshToken = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
    private void setNewAccessToken(String token) {
        newAccessToken = token;
        accessTokenInterceptor.setAccessToken(token);
    }

    @Nullable
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (!isRequestWithAccessToken(response) || accessToken == null) {
            if (!accessToken.equals("")) return null;
        }
        synchronized (this) {
            // Access token is refreshed in another thread.
            if (accessToken.equals(newAccessToken)) {
                return newRequestWithAccessToken(response.request(), newAccessToken);
            }

            // Need to refresh an access token
            @SuppressLint("HardwareIds")
            String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            RefreshRequest refreshRequest = new RefreshRequest(refreshToken, deviceId);

            Token token = webLazyWrapper.get().refreshAccessToken(refreshRequest).execute().body();
            final String updatedAccessToken = token.accessToken;
            setNewAccessToken(updatedAccessToken);

            return newRequestWithAccessToken(response.request(), updatedAccessToken);
        }
    }

    private boolean isRequestWithAccessToken(@NonNull Response response) {
        String header = response.request().header("Authorization");
        return header != null && header.startsWith("Bearer");
    }

    @NonNull
    private Request newRequestWithAccessToken(@NonNull Request request, @NonNull String accessToken) {
        return request.newBuilder()
                .header("Authorization", "Bearer " + accessToken)
                .build();
    }
}
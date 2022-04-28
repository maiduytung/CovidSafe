package com.android.covidsafe.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.covidsafe.api.APIService;
import com.android.covidsafe.repository.ISharedPreferences;
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
    private final ISharedPreferences sharedPreferences;
    private final Lazy<APIService> webLazyWrapper;

    @Inject
    public AccessTokenAuthenticator(Context context, ISharedPreferences sharedPreferences, Lazy<APIService> webLazyWrapper) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
        this.webLazyWrapper = webLazyWrapper;
    }

    @Nullable
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        final String accessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN);
        if (!isRequestWithAccessToken(response) || accessToken == null) {
            return null;
        }
        synchronized (this) {
            final String newAccessToken = sharedPreferences.getString(Constants.ACCESS_TOKEN);
            // Access token is refreshed in another thread.
            if (!accessToken.equals(newAccessToken)) {
                return newRequestWithAccessToken(response.request(), newAccessToken);
            }

            // Need to refresh an access token
            @SuppressLint("HardwareIds")
            String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

            RefreshRequest refreshRequest = new RefreshRequest(sharedPreferences.getString(Constants.REFRESH_TOKEN), deviceId);

            Token token = webLazyWrapper.get().refreshAccessToken(refreshRequest).execute().body();
            final String updatedAccessToken = token.accessToken;
            sharedPreferences.putString(Constants.ACCESS_TOKEN, updatedAccessToken);

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
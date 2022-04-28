package com.android.covidsafe.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.api.APIService;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.Token;
import com.android.covidsafe.vo.request.LoginRequest;
import com.android.covidsafe.vo.request.RegisterRequest;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles Token objects.
 */
@Singleton
public class AuthRepository {
    private final APIService apiService;
    private final AppExecutors appExecutors;
    private final ISharedPreferences sharedPreference;

    @Inject
    public AuthRepository(AppExecutors appExecutors, APIService apiService, ISharedPreferences sharedPreferences) {
        this.appExecutors = appExecutors;
        this.apiService = apiService;
        this.sharedPreference = sharedPreferences;
    }

    public LiveData<Resource<Token>> login(LoginRequest loginRequest) {
        return new NetworkResource<Token>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Token item) {
                sharedPreference.putString(Constants.TOKEN_TYPE, item.tokenType);
                sharedPreference.putString(Constants.ACCESS_TOKEN, item.accessToken);
                sharedPreference.putInt(Constants.EXPIRES_IN, item.expiresIn);
                sharedPreference.putString(Constants.REFRESH_TOKEN, item.refreshToken);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Token>> createCall() {
                return apiService.login(loginRequest);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Token>> register(RegisterRequest registerRequest) {
        return new NetworkResource<Token>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Token item) {
                sharedPreference.putString(Constants.TOKEN_TYPE, item.tokenType);
                sharedPreference.putString(Constants.ACCESS_TOKEN, item.accessToken);
                sharedPreference.putInt(Constants.EXPIRES_IN, item.expiresIn);
                sharedPreference.putString(Constants.REFRESH_TOKEN, item.refreshToken);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Token>> createCall() {
                return apiService.register(registerRequest);
            }
        }.asLiveData();
    }

    public boolean isLogin() {
        return sharedPreference.getString(Constants.ACCESS_TOKEN) != null;
    }
}

package com.android.covidsafe.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.api.AuthService;
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
    private final AuthService authService;
    private final AppExecutors appExecutors;

    @Inject
    public AuthRepository(AppExecutors appExecutors, AuthService authService) {
        this.appExecutors = appExecutors;
        this.authService = authService;
    }

    public LiveData<Resource<Token>> login(LoginRequest loginRequest) {
        return new NetworkResource<Token>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Token item) {
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Token>> createCall() {
                return authService.login(loginRequest);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Token>> register(RegisterRequest registerRequest) {
        return new NetworkResource<Token>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Token item) {
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Token>> createCall() {
                return authService.register(registerRequest);
            }
        }.asLiveData();
    }
}

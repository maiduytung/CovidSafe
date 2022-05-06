package com.android.covidsafe.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.api.AuthService;
import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.Verification;
import com.android.covidsafe.vo.request.OTPVerificationRequest;
import com.android.covidsafe.vo.request.SendVerificationRequest;
import com.android.covidsafe.vo.response.BaseResponse;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles Verification Code String.
 */
@Singleton
public class VerificationRepository {
    private final AuthService authService;
    private final AppExecutors appExecutors;

    @Inject
    VerificationRepository(AppExecutors appExecutors, AuthService authService) {
        this.authService = authService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<BaseResponse>> sendOtp(SendVerificationRequest sendVerificationRequest) {
        return new NetworkResource<BaseResponse>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull BaseResponse item) {
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<BaseResponse>> createCall() {
                return authService.sendVerification(sendVerificationRequest);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Verification>> verify(OTPVerificationRequest otpVerificationRequest) {
        return new NetworkResource<Verification>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Verification item) {
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Verification>> createCall() {
                return authService.verify(otpVerificationRequest);
            }
        }.asLiveData();
    }
}

package com.android.covidsafe.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.api.APIService;
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
    private final APIService apiService;
    private final AppExecutors appExecutors;

    @Inject
    VerificationRepository(AppExecutors appExecutors, APIService apiService) {
        this.apiService = apiService;
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
                return apiService.sendVerification(sendVerificationRequest);
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
                return apiService.verify(otpVerificationRequest);
            }
        }.asLiveData();
    }
}

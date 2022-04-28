package com.android.covidsafe.ui.dialog;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.covidsafe.repository.VerificationRepository;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.Verification;
import com.android.covidsafe.vo.request.OTPVerificationRequest;
import com.android.covidsafe.vo.request.SendVerificationRequest;
import com.android.covidsafe.vo.response.BaseResponse;

import javax.inject.Inject;

public class VerificationViewModel extends ViewModel {

    private final MutableLiveData<SendVerificationRequest> otpRequest = new MutableLiveData<>();
    private final MutableLiveData<OTPVerificationRequest> otpVerificationRequest = new MutableLiveData<>();

    private final LiveData<Resource<BaseResponse>> getOTPResults;
    private final LiveData<Resource<Verification>> verificationResults;

    public MutableLiveData<String> otpInput = new MutableLiveData<>();

    @Inject
    public VerificationViewModel(VerificationRepository verificationRepository) {
        getOTPResults = Transformations.switchMap(otpRequest, sendVerificationRequest -> {
            if (sendVerificationRequest == null) {
                return AbsentLiveData.create();
            } else {
                return verificationRepository.sendOtp(sendVerificationRequest);
            }
        });

        verificationResults = Transformations.switchMap(otpVerificationRequest, otpVerificationRequest -> {
            if (otpVerificationRequest == null) {
                return AbsentLiveData.create();
            } else {
                return verificationRepository.verify(otpVerificationRequest);
            }
        });
    }

    @VisibleForTesting
    public LiveData<Resource<BaseResponse>> getOTPResults() {
        return getOTPResults;
    }

    @VisibleForTesting
    public LiveData<Resource<Verification>> getVerificationResults() {
        return verificationResults;
    }

    public void setOTPRequest(SendVerificationRequest request) {
        otpRequest.setValue(request);
    }

    public void setOTPVerificationRequest() {
        OTPVerificationRequest request = new OTPVerificationRequest(otpInput.getValue(), otpRequest.getValue().getDeviceId());
        otpVerificationRequest.setValue(request);
    }
}

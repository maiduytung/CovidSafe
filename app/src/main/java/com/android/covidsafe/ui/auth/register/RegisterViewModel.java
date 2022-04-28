package com.android.covidsafe.ui.auth.register;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.covidsafe.repository.AuthRepository;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.Token;
import com.android.covidsafe.vo.request.RegisterRequest;

import javax.inject.Inject;

public class RegisterViewModel extends ViewModel {

    private final MutableLiveData<RegisterRequest> registerUser = new MutableLiveData<>();
    private final LiveData<Resource<Token>> tokenResource;

    public MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    @Inject
    RegisterViewModel(AuthRepository authRepository) {
        tokenResource = Transformations.switchMap(registerUser, registerRequest -> {
            if (registerRequest == null) {
                return AbsentLiveData.create();
            } else {
                return authRepository.register(registerRequest);
            }
        });
    }

    @VisibleForTesting
    public LiveData<Resource<Token>> getTokenResource() {
        return tokenResource;
    }

    public void setRegisterUser(String deviceId, String verificationKey) {
        RegisterRequest registerRequest = new RegisterRequest(phoneNumber.getValue(), password.getValue(), deviceId, verificationKey);
        this.registerUser.setValue(registerRequest);
    }

}

package com.android.covidsafe.ui.auth.login;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.covidsafe.repository.AuthRepository;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.Token;
import com.android.covidsafe.vo.request.LoginRequest;

import javax.inject.Inject;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginRequest> loginUser = new MutableLiveData<>();

    private final LiveData<Resource<Token>> tokenResource;

    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    @Inject
    LoginViewModel(AuthRepository authRepository) {
        tokenResource = Transformations.switchMap(loginUser, loginRequest -> {
            if (loginRequest == null) {
                return AbsentLiveData.create();
            } else {
                return authRepository.login(loginRequest);
            }
        });
    }

    @VisibleForTesting
    public LiveData<Resource<Token>> getTokenResource() {
        return tokenResource;
    }

    public void setLoginUser(@NonNull String deviceId) {
        LoginRequest loginRequest = new LoginRequest(username.getValue(), password.getValue(), deviceId);
        this.loginUser.setValue(loginRequest);
    }

}

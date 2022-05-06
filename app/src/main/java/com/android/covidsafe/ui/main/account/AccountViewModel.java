package com.android.covidsafe.ui.main.account;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.covidsafe.repository.ProfileRepository;
import com.android.covidsafe.repository.UserRepository;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.vo.Profile;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.User;

import javax.inject.Inject;

public class AccountViewModel extends ViewModel {

    private final LiveData<Resource<Profile>> profileResource;

    @Inject
    public AccountViewModel(ProfileRepository profileRepository) {
        profileResource = profileRepository.loadProfile();
    }

    @VisibleForTesting
    public LiveData<Resource<Profile>> getProfileResource() {
        return profileResource;
    }
}
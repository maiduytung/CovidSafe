package com.android.covidsafe.ui.main.home;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.covidsafe.repository.CertificationRepository;
import com.android.covidsafe.repository.ProfileRepository;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.vo.Certification;
import com.android.covidsafe.vo.Profile;
import com.android.covidsafe.vo.Resource;

import javax.inject.Inject;

public class HomeViewModel extends ViewModel {

    private final LiveData<Resource<Profile>> profileResource;
    private final LiveData<Resource<Certification>> certificationResource;

    @Inject
    public HomeViewModel(ProfileRepository profileRepository, CertificationRepository certificationRepository) {
        profileResource = profileRepository.loadProfile();
        certificationResource = certificationRepository.loadCertification();
    }

    @VisibleForTesting
    public LiveData<Resource<Profile>> getProfileResource() {
        return profileResource;
    }

    @VisibleForTesting
    public LiveData<Resource<Certification>> getCertificationResource() {
        return certificationResource;
    }
}
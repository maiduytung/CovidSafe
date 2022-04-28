package com.android.covidsafe.ui.main.account.profile;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.covidsafe.repository.EthnicRepository;
import com.android.covidsafe.repository.NationalityRepository;
import com.android.covidsafe.repository.ProfileRepository;
import com.android.covidsafe.repository.SubnationalRepository;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.vo.Ethnic;
import com.android.covidsafe.vo.Nationality;
import com.android.covidsafe.vo.Profile;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.Subnational;
import com.android.covidsafe.vo.request.ProfileRequest;

import java.util.List;

import javax.inject.Inject;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<String> profileId = new MutableLiveData<>();
    private final MutableLiveData<String> provinceId = new MutableLiveData<>();
    private final MutableLiveData<String> districtId = new MutableLiveData<>();
    private final MutableLiveData<String> wardId = new MutableLiveData<>();
    private final MutableLiveData<String> nationalityId = new MutableLiveData<>();
    private final MutableLiveData<String> ethnicId = new MutableLiveData<>();

    public LiveData<String> province;
    public LiveData<String> district;
    public LiveData<String> ward;
    public LiveData<String> nationality;
    public LiveData<String> ethnic;

    private final LiveData<Resource<Profile>> profileResource;
    private final LiveData<Resource<Profile>> profileUpdateResource;
    private final LiveData<List<Subnational>> provinceList;
    private final LiveData<List<Subnational>> districtList;
    private final LiveData<List<Subnational>> wardList;
    private final LiveData<List<Nationality>> nationalityList;
    private final LiveData<List<Ethnic>> ethnicList;

    public MutableLiveData<ProfileRequest> profileRequest = new MutableLiveData<>();

    @Inject
    ProfileViewModel(ProfileRepository profileRepository, SubnationalRepository subnationalRepository, NationalityRepository nationalityRepository, EthnicRepository ethnicRepository) {
        profileResource = profileRepository.loadProfile();

        profileUpdateResource = Transformations.switchMap(profileId, id -> {
            if (id == null) {
                return AbsentLiveData.create();
            } else {
                return profileRepository.updateProfile(profileRequest.getValue());
            }
        });

        province = Transformations.switchMap(provinceId, provinceId -> {
            if (provinceId == null || provinceId.equals("0")) {
                return AbsentLiveData.create();
            } else {
                return subnationalRepository.getName(provinceId);
            }
        });

        provinceList = Transformations.switchMap(provinceId, parentId -> {
            if (parentId == null) {
                return AbsentLiveData.create();
            } else {
                return subnationalRepository.getProvinceList();
            }
        });

        district = Transformations.switchMap(districtId, districtId -> {
            if (districtId == null) {
                return AbsentLiveData.create();
            } else {
                return subnationalRepository.getName(districtId);
            }
        });

        districtList = Transformations.switchMap(provinceId, parentId -> {
            if (parentId == null) {
                return AbsentLiveData.create();
            } else {
                return subnationalRepository.getDistrictList(parentId);
            }
        });

        ward = Transformations.switchMap(wardId, wardId -> {
            if (wardId == null) {
                return AbsentLiveData.create();
            } else {
                return subnationalRepository.getName(wardId);
            }
        });

        wardList = Transformations.switchMap(districtId, parentId -> {
            if (parentId == null) {
                return AbsentLiveData.create();
            } else {
                return subnationalRepository.getWardList(parentId);
            }
        });

        nationality = Transformations.switchMap(nationalityId, nationalityId -> {
            if (nationalityId == null || nationalityId.equals("0")) {
                return AbsentLiveData.create();
            } else {
                return nationalityRepository.getName(nationalityId);
            }
        });

        nationalityList = Transformations.switchMap(nationalityId, nationalityId -> {
            if (nationalityId == null) {
                return AbsentLiveData.create();
            } else {
                return nationalityRepository.getNationalityList();
            }
        });

        ethnic = Transformations.switchMap(ethnicId, ethnicId -> {
            if (ethnicId == null || ethnicId.equals("0")) {
                return AbsentLiveData.create();
            } else {
                return ethnicRepository.getName(ethnicId);
            }
        });

        ethnicList = Transformations.switchMap(ethnicId, ethnicId -> {
            if (ethnicId == null) {
                return AbsentLiveData.create();
            } else {
                return ethnicRepository.getEthnicList();
            }
        });
    }

    public void setProfile(Profile profile) {
        ProfileRequest data = new ProfileRequest(profile);
        profileRequest.setValue(data);
    }

    @VisibleForTesting
    public LiveData<Resource<Profile>> getProfileResource() {
        return profileResource;
    }

    @VisibleForTesting
    public LiveData<Resource<Profile>> getProfileUpdateResource() {
        return profileUpdateResource;
    }

    @VisibleForTesting
    public LiveData<List<Subnational>> getProvinceList() {
        return provinceList;
    }

    @VisibleForTesting
    public LiveData<List<Subnational>> getDistrictList() {
        return districtList;
    }

    @VisibleForTesting
    public LiveData<List<Subnational>> getWardList() {
        return wardList;
    }

    @VisibleForTesting
    public LiveData<List<Nationality>> getNationalityList() {
        return nationalityList;
    }

    @VisibleForTesting
    public LiveData<List<Ethnic>> getEthnicList() {
        return ethnicList;
    }

    @VisibleForTesting
    public MutableLiveData<ProfileRequest> getProfileRequest() {
        return profileRequest;
    }

    public void updateProfile() {
        profileId.setValue("currentProfile");
    }

    public void setProvince(String id) {
        if (!id.equals("0")) {
            ProfileRequest data = profileRequest.getValue();
            data.setProvince(id);
            profileRequest.setValue(data);
        }

        provinceId.setValue(id);
    }

    public void setDistrict(String id) {
        if (!id.equals("0")) {
            ProfileRequest data = profileRequest.getValue();
            data.setDistrict(id);
            profileRequest.setValue(data);
        }

        districtId.setValue(id);
    }

    public void setWard(String id) {
        if (!id.equals("0")) {
            ProfileRequest data = profileRequest.getValue();
            data.setWard(id);
            profileRequest.setValue(data);
        }

        wardId.setValue(id);
    }

    public void setNationality(String id) {
        if (!id.equals("0")) {
            ProfileRequest data = profileRequest.getValue();
            data.setNationality(id);
            profileRequest.setValue(data);
        }

        nationalityId.setValue(id);
    }

    public void setEthnic(String id) {
        if (!id.equals("0")) {
            ProfileRequest data = profileRequest.getValue();
            data.setEthnic(id);
            profileRequest.setValue(data);
        }

        ethnicId.setValue(id);
    }

    public void setDateOfBirth(String dateString) {
        ProfileRequest data = profileRequest.getValue();
        data.setDateOfBirth(dateString);
        profileRequest.setValue(data);
    }

    public void setAvatar(String avatar) {
        ProfileRequest data = profileRequest.getValue();
        data.setAvatar(avatar);
        profileRequest.setValue(data);
    }
}

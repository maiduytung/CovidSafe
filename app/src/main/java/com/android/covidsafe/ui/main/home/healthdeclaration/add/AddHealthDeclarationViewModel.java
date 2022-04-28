package com.android.covidsafe.ui.main.home.healthdeclaration.add;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.covidsafe.repository.HealthDeclarationRepository;
import com.android.covidsafe.repository.NationalityRepository;
import com.android.covidsafe.repository.ProfileRepository;
import com.android.covidsafe.repository.SubnationalRepository;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.vo.HealthDeclaration;
import com.android.covidsafe.vo.Nationality;
import com.android.covidsafe.vo.Profile;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.Subnational;
import com.android.covidsafe.vo.request.HealthDeclarationRequest;

import java.util.List;

import javax.inject.Inject;

public class AddHealthDeclarationViewModel extends ViewModel {

    private final MutableLiveData<String> query = new MutableLiveData<>();
    private final MutableLiveData<String> provinceId = new MutableLiveData<>();
    private final MutableLiveData<String> districtId = new MutableLiveData<>();
    private final MutableLiveData<String> wardId = new MutableLiveData<>();
    private final MutableLiveData<String> nationalityId = new MutableLiveData<>();

    public LiveData<String> province;
    public LiveData<String> district;
    public LiveData<String> ward;
    public LiveData<String> nationality;

    private final LiveData<Resource<Profile>> profileResource;
    private final LiveData<Resource<HealthDeclaration>> healthDeclarationResource;
    private final LiveData<List<Subnational>> provinceList;
    private final LiveData<List<Subnational>> districtList;
    private final LiveData<List<Subnational>> wardList;
    private final LiveData<List<Nationality>> nationalityList;

    public MutableLiveData<HealthDeclarationRequest> healthDeclarationRequest = new MutableLiveData<>();

    @Inject
    AddHealthDeclarationViewModel(ProfileRepository profileRepository, SubnationalRepository subnationalRepository, NationalityRepository nationalityRepository,
                                  HealthDeclarationRepository healthDeclarationRepository) {

        profileResource = profileRepository.loadProfile();

        healthDeclarationResource = Transformations.switchMap(query, query -> {
            if (query == null) {
                return AbsentLiveData.create();
            } else {
                return healthDeclarationRepository.add(healthDeclarationRequest.getValue());
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

        province = Transformations.switchMap(provinceId, provinceId -> {
            if (provinceId == null || provinceId.equals("0")) {
                return AbsentLiveData.create();
            } else {
                return subnationalRepository.getName(provinceId);
            }
        });

        provinceList = Transformations.switchMap(provinceId, provinceId -> {
            if (provinceId == null) {
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

    }

    public void setHealthDeclaration(Profile profile) {
        HealthDeclarationRequest data = new HealthDeclarationRequest(profile);
        healthDeclarationRequest.setValue(data);
    }

    @VisibleForTesting
    public LiveData<Resource<Profile>> getProfileResource() {
        return profileResource;
    }

    @VisibleForTesting
    public LiveData<Resource<HealthDeclaration>> getHealthDeclarationResource() {
        return healthDeclarationResource;
    }

    @VisibleForTesting
    public LiveData<List<Nationality>> getNationalityList() {
        return nationalityList;
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

    public void setNationality(String id) {
        if (!id.equals("0")) {
            HealthDeclarationRequest data = healthDeclarationRequest.getValue();
            data.setNationality(id);
            healthDeclarationRequest.setValue(data);
        }

        nationalityId.setValue(id);
    }

    public void setProvince(String id) {
        if (!id.equals("0")) {
            HealthDeclarationRequest data = healthDeclarationRequest.getValue();
            data.setProvince(id);
            healthDeclarationRequest.setValue(data);
        }

        provinceId.setValue(id);
    }

    public void setDistrict(String id) {
        if (!id.equals("0")) {
            HealthDeclarationRequest data = healthDeclarationRequest.getValue();
            data.setDistrict(id);
            healthDeclarationRequest.setValue(data);
        }

        districtId.setValue(id);
    }

    public void setWard(String id) {
        if (!id.equals("0")) {
            HealthDeclarationRequest data = healthDeclarationRequest.getValue();
            data.setWard(id);
            healthDeclarationRequest.setValue(data);
        }

        wardId.setValue(id);
    }

    public void doSend() {
        query.setValue("query");
    }

}

package com.android.covidsafe.ui.main.home.healthdeclaration.detail;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.covidsafe.repository.HealthDeclarationRepository;
import com.android.covidsafe.repository.NationalityRepository;
import com.android.covidsafe.repository.SubnationalRepository;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.vo.HealthDeclaration;
import com.android.covidsafe.vo.Resource;

import javax.inject.Inject;

public class HealthDeclarationDetailViewModel extends ViewModel {

    private final MutableLiveData<String> healthDeclarationId = new MutableLiveData<>();
    private final MutableLiveData<String> provinceId = new MutableLiveData<>();
    private final MutableLiveData<String> districtId = new MutableLiveData<>();
    private final MutableLiveData<String> wardId = new MutableLiveData<>();
    private final MutableLiveData<String> nationalityId = new MutableLiveData<>();

    public LiveData<String> province;
    public LiveData<String> district;
    public LiveData<String> ward;
    public LiveData<String> nationality;

    private final LiveData<Resource<HealthDeclaration>> healthDeclarationResource;

    @Inject
    HealthDeclarationDetailViewModel(SubnationalRepository subnationalRepository, NationalityRepository nationalityRepository,
                                     HealthDeclarationRepository healthDeclarationRepository) {

        healthDeclarationResource = Transformations.switchMap(healthDeclarationId, healthDeclarationId -> {
            if (healthDeclarationId == null || healthDeclarationId.equals("0")) {
                return AbsentLiveData.create();
            } else {
                return healthDeclarationRepository.getOne(healthDeclarationId);
            }
        });

        nationality = Transformations.switchMap(nationalityId, nationalityId -> {
            if (nationalityId == null || nationalityId.equals("0")) {
                return AbsentLiveData.create();
            } else {
                return nationalityRepository.getName(nationalityId);
            }
        });

        province = Transformations.switchMap(provinceId, provinceId -> {
            if (provinceId == null || provinceId.equals("0")) {
                return AbsentLiveData.create();
            } else {
                return subnationalRepository.getName(provinceId);
            }
        });

        district = Transformations.switchMap(districtId, districtId -> {
            if (districtId == null) {
                return AbsentLiveData.create();
            } else {
                return subnationalRepository.getName(districtId);
            }
        });

        ward = Transformations.switchMap(wardId, wardId -> {
            if (wardId == null) {
                return AbsentLiveData.create();
            } else {
                return subnationalRepository.getName(wardId);
            }
        });

    }

    @VisibleForTesting
    public LiveData<Resource<HealthDeclaration>> getHealthDeclarationResource() {
        return healthDeclarationResource;
    }

    public void setHealthDeclarationId(String id) {
        healthDeclarationId.setValue(id);
    }

    public void setNationality(String id) {
        nationalityId.setValue(id);
    }

    public void setProvince(String id) {
        provinceId.setValue(id);
    }

    public void setDistrict(String id) {
        districtId.setValue(id);
    }

    public void setWard(String id) {
        wardId.setValue(id);
    }
}

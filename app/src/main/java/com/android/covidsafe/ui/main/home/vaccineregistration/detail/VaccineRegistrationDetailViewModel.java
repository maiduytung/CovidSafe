package com.android.covidsafe.ui.main.home.vaccineregistration.detail;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.covidsafe.repository.EthnicRepository;
import com.android.covidsafe.repository.NationalityRepository;
import com.android.covidsafe.repository.PriorityRepository;
import com.android.covidsafe.repository.SubnationalRepository;
import com.android.covidsafe.repository.VaccineRegistrationRepository;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.VaccineRegistration;

import javax.inject.Inject;

public class VaccineRegistrationDetailViewModel extends ViewModel {

    private final MutableLiveData<String> vaccineRegistrationId = new MutableLiveData<>();
    private final MutableLiveData<String> priorityId = new MutableLiveData<>();
    private final MutableLiveData<String> provinceId = new MutableLiveData<>();
    private final MutableLiveData<String> districtId = new MutableLiveData<>();
    private final MutableLiveData<String> wardId = new MutableLiveData<>();
    private final MutableLiveData<String> nationalityId = new MutableLiveData<>();
    private final MutableLiveData<String> ethnicId = new MutableLiveData<>();

    public LiveData<String> priority;
    public LiveData<String> province;
    public LiveData<String> district;
    public LiveData<String> ward;
    public LiveData<String> nationality;
    public LiveData<String> ethnic;

    private final LiveData<Resource<VaccineRegistration>> vaccineRegistrationResource;

    @Inject
    VaccineRegistrationDetailViewModel(PriorityRepository priorityRepository, SubnationalRepository subnationalRepository, NationalityRepository nationalityRepository, EthnicRepository ethnicRepository, VaccineRegistrationRepository vaccineRegistrationRepository) {

        vaccineRegistrationResource = Transformations.switchMap(vaccineRegistrationId, vaccineRegistrationId -> {
            if (vaccineRegistrationId == null || vaccineRegistrationId.equals("0")) {
                return AbsentLiveData.create();
            } else {
                return vaccineRegistrationRepository.getOne(vaccineRegistrationId);
            }
        });

        priority = Transformations.switchMap(priorityId, priorityId -> {
            if (priorityId == null || priorityId.equals("0")) {
                return AbsentLiveData.create();
            } else {
                return priorityRepository.getName(priorityId);
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

        nationality = Transformations.switchMap(nationalityId, nationalityId -> {
            if (nationalityId == null || nationalityId.equals("0")) {
                return AbsentLiveData.create();
            } else {
                return nationalityRepository.getName(nationalityId);
            }
        });

        ethnic = Transformations.switchMap(ethnicId, ethnicId -> {
            if (ethnicId == null || ethnicId.equals("0")) {
                return AbsentLiveData.create();
            } else {
                return ethnicRepository.getName(ethnicId);
            }
        });
    }

    @VisibleForTesting
    public LiveData<Resource<VaccineRegistration>> getVaccineRegistrationResource() {
        return vaccineRegistrationResource;
    }

    public void setVaccineRegistrationId(String id) {
        vaccineRegistrationId.setValue(id);
    }

    public void setPriority(String id) {
        priorityId.setValue(id);
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

    public void setNationality(String id) {
        nationalityId.setValue(id);
    }

    public void setEthnic(String id) {
        ethnicId.setValue(id);
    }
}

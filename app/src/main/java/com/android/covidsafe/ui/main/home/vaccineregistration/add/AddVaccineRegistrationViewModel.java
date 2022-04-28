package com.android.covidsafe.ui.main.home.vaccineregistration.add;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.covidsafe.repository.EthnicRepository;
import com.android.covidsafe.repository.NationalityRepository;
import com.android.covidsafe.repository.PriorityRepository;
import com.android.covidsafe.repository.ProfileRepository;
import com.android.covidsafe.repository.SubnationalRepository;
import com.android.covidsafe.repository.VaccineRegistrationRepository;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.vo.Ethnic;
import com.android.covidsafe.vo.Nationality;
import com.android.covidsafe.vo.Priority;
import com.android.covidsafe.vo.Profile;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.Subnational;
import com.android.covidsafe.vo.VaccineRegistration;
import com.android.covidsafe.vo.request.VaccineRegistrationRequest;

import java.util.List;

import javax.inject.Inject;

public class AddVaccineRegistrationViewModel extends ViewModel {

    private final MutableLiveData<String> query = new MutableLiveData<>();
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

    private final LiveData<Resource<Profile>> profileResource;
    private final LiveData<Resource<VaccineRegistration>> vaccineRegistrationResource;
    private final LiveData<List<Priority>> priorityList;
    private final LiveData<List<Subnational>> provinceList;
    private final LiveData<List<Subnational>> districtList;
    private final LiveData<List<Subnational>> wardList;
    private final LiveData<List<Nationality>> nationalityList;
    private final LiveData<List<Ethnic>> ethnicList;

    public MutableLiveData<VaccineRegistrationRequest> vaccineRegistrationRequest = new MutableLiveData<>();

    @Inject
    AddVaccineRegistrationViewModel(ProfileRepository profileRepository, PriorityRepository priorityRepository, SubnationalRepository subnationalRepository, NationalityRepository nationalityRepository, EthnicRepository ethnicRepository, VaccineRegistrationRepository vaccineRegistrationRepository) {

        profileResource = profileRepository.loadProfile();

        vaccineRegistrationResource = Transformations.switchMap(query, query -> {
            if (query == null) {
                return AbsentLiveData.create();
            } else {
                return vaccineRegistrationRepository.add(vaccineRegistrationRequest.getValue());
            }
        });

        priority = Transformations.switchMap(priorityId, priorityId -> {
            if (priorityId == null || priorityId.equals("0")) {
                return AbsentLiveData.create();
            } else {
                return priorityRepository.getName(priorityId);
            }
        });

        priorityList = Transformations.switchMap(priorityId, priorityId -> {
            if (priorityId == null) {
                return AbsentLiveData.create();
            } else {
                return priorityRepository.getPriorityList();
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

    public void setVaccineRegistration(Profile profile) {
        VaccineRegistrationRequest data = new VaccineRegistrationRequest(profile);
        vaccineRegistrationRequest.setValue(data);
    }

    @VisibleForTesting
    public LiveData<Resource<Profile>> getProfileResource() {
        return profileResource;
    }

    @VisibleForTesting
    public LiveData<Resource<VaccineRegistration>> getVaccineRegistrationResource() {
        return vaccineRegistrationResource;
    }

    @VisibleForTesting
    public LiveData<List<Priority>> getPriorityList() {
        return priorityList;
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

    public void setPriority(String id) {
        if (!id.equals("0")) {
            VaccineRegistrationRequest data = vaccineRegistrationRequest.getValue();
            data.setPriority(id);
            vaccineRegistrationRequest.setValue(data);
        }

        priorityId.setValue(id);
    }

    public void setProvince(String id) {
        if (!id.equals("0")) {
            VaccineRegistrationRequest data = vaccineRegistrationRequest.getValue();
            data.setProvince(id);
            vaccineRegistrationRequest.setValue(data);
        }

        provinceId.setValue(id);
    }

    public void setDistrict(String id) {
        if (!id.equals("0")) {
            VaccineRegistrationRequest data = vaccineRegistrationRequest.getValue();
            data.setDistrict(id);
            vaccineRegistrationRequest.setValue(data);
        }

        districtId.setValue(id);
    }

    public void setWard(String id) {
        if (!id.equals("0")) {
            VaccineRegistrationRequest data = vaccineRegistrationRequest.getValue();
            data.setWard(id);
            vaccineRegistrationRequest.setValue(data);
        }

        wardId.setValue(id);
    }

    public void setNationality(String id) {
        if (!id.equals("0")) {
            VaccineRegistrationRequest data = vaccineRegistrationRequest.getValue();
            data.setNationality(id);
            vaccineRegistrationRequest.setValue(data);
        }
        nationalityId.setValue(id);
    }

    public void setEthnic(String id) {
        if (!id.equals("0")) {
            VaccineRegistrationRequest data = vaccineRegistrationRequest.getValue();
            data.setEthnic(id);
            vaccineRegistrationRequest.setValue(data);
        }

        ethnicId.setValue(id);
    }

    public void setDateOfBirth(String dateString) {
        VaccineRegistrationRequest data = vaccineRegistrationRequest.getValue();
        data.setDateOfBirth(dateString);
        vaccineRegistrationRequest.setValue(data);
    }

    public void setPreferredVaccinationDate(String dateString) {
        VaccineRegistrationRequest data = vaccineRegistrationRequest.getValue();
        data.setPreferredVaccinationDate(dateString);
        vaccineRegistrationRequest.setValue(data);
    }

    public void doSend() {
        query.setValue("query");
    }
}

package com.android.covidsafe.ui.main.home.vaccination.detail;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.covidsafe.repository.VaccinationRepository;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.Vaccination;

import javax.inject.Inject;

public class VaccinationDetailViewModel extends ViewModel {

    private final MutableLiveData<String> vaccinationId = new MutableLiveData<>();
    private final LiveData<Resource<Vaccination>> vaccinationResource;

    @Inject
    VaccinationDetailViewModel(VaccinationRepository vaccinationRepository) {

        vaccinationResource = Transformations.switchMap(vaccinationId, vaccinationId -> {
            if (vaccinationId == null || vaccinationId.equals("0")) {
                return AbsentLiveData.create();
            } else {
                return vaccinationRepository.getOne(vaccinationId);
            }
        });
    }

    @VisibleForTesting
    public LiveData<Resource<Vaccination>> getVaccinationResource() {
        return vaccinationResource;
    }

    public void setVaccinationId(String id) {
        vaccinationId.setValue(id);
    }
}

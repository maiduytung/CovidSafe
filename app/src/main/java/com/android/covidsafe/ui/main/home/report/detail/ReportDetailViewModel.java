package com.android.covidsafe.ui.main.home.report.detail;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.covidsafe.repository.EthnicRepository;
import com.android.covidsafe.repository.NationalityRepository;
import com.android.covidsafe.repository.PriorityRepository;
import com.android.covidsafe.repository.ProfileRepository;
import com.android.covidsafe.repository.ReportRepository;
import com.android.covidsafe.repository.SubnationalRepository;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.vo.Report;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.Subnational;
import com.android.covidsafe.vo.request.ReportRequest;

import java.util.List;

import javax.inject.Inject;

public class ReportDetailViewModel extends ViewModel {

    private final MutableLiveData<String> reportId = new MutableLiveData<>();
    private final MutableLiveData<String> provinceId = new MutableLiveData<>();
    private final MutableLiveData<String> districtId = new MutableLiveData<>();
    private final MutableLiveData<String> wardId = new MutableLiveData<>();

    public LiveData<String> province;
    public LiveData<String> district;
    public LiveData<String> ward;

    private final LiveData<Resource<Report>> reportResource;

    @Inject
    ReportDetailViewModel(SubnationalRepository subnationalRepository, ReportRepository reportRepository) {

        reportResource = Transformations.switchMap(reportId, reportId -> {
            if (reportId == null || reportId.equals("0")) {
                return AbsentLiveData.create();
            } else {
                return reportRepository.getOne(reportId);
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
    public LiveData<Resource<Report>> getReportResource() {
        return reportResource;
    }

    public void setReportId(String id) {
        reportId.setValue(id);
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

package com.android.covidsafe.ui.main.home.report.add;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.android.covidsafe.repository.ReportRepository;
import com.android.covidsafe.repository.SubnationalRepository;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.vo.Report;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.Subnational;
import com.android.covidsafe.vo.request.ReportRequest;

import java.util.List;

import javax.inject.Inject;

public class AddReportViewModel extends ViewModel {

    private final MutableLiveData<String> query = new MutableLiveData<>();
    private final MutableLiveData<String> provinceId = new MutableLiveData<>();
    private final MutableLiveData<String> districtId = new MutableLiveData<>();
    private final MutableLiveData<String> wardId = new MutableLiveData<>();

    public LiveData<String> province;
    public LiveData<String> district;
    public LiveData<String> ward;

    private final LiveData<Resource<Report>> reportResource;
    private final LiveData<List<Subnational>> provinceList;
    private final LiveData<List<Subnational>> districtList;
    private final LiveData<List<Subnational>> wardList;

    public MutableLiveData<ReportRequest> reportRequest = new MutableLiveData<>();

    @Inject
    AddReportViewModel(SubnationalRepository subnationalRepository, ReportRepository reportRepository) {
        ReportRequest data = new ReportRequest();
        reportRequest.setValue(data);

        reportResource = Transformations.switchMap(query, query -> {
            if (query == null) {
                return AbsentLiveData.create();
            } else {
                return reportRepository.add(reportRequest.getValue());
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

    @VisibleForTesting
    public LiveData<Resource<Report>> getReportResource() {
        return reportResource;
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

    public void setProvince(String id) {
        if (!id.equals("0")) {
            ReportRequest data = reportRequest.getValue();
            data.setProvince(id);
            reportRequest.setValue(data);
        }

        provinceId.setValue(id);
    }

    public void setDistrict(String id) {
        if (!id.equals("0")) {
            ReportRequest data = reportRequest.getValue();
            data.setDistrict(id);
            reportRequest.setValue(data);
        }

        districtId.setValue(id);
    }

    public void setWard(String id) {
        if (!id.equals("0")) {
            ReportRequest data = reportRequest.getValue();
            data.setWard(id);
            reportRequest.setValue(data);
        }

        wardId.setValue(id);
    }

    public void doSend() {
        query.setValue("query");
    }
}

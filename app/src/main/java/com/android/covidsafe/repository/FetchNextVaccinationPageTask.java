package com.android.covidsafe.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.api.VaccinationResponse;
import com.android.covidsafe.api.VaccinationService;
import com.android.covidsafe.db.SecureDatabase;
import com.android.covidsafe.vo.VaccinationResult;
import com.android.covidsafe.vo.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * A task that reads the search result in the database and fetches the next page, if it has one.
 */
public class FetchNextVaccinationPageTask implements Runnable {
    private final MutableLiveData<Resource<Boolean>> liveData = new MutableLiveData<>();
    private final String query;
    private final VaccinationService vaccinationService;
    private final SecureDatabase db;

    FetchNextVaccinationPageTask(String query, VaccinationService vaccinationService, SecureDatabase db) {
        this.query = query;
        this.vaccinationService = vaccinationService;
        this.db = db;
    }

    @Override
    public void run() {
        VaccinationResult current = db.vaccinationDao().findVaccinationResult(query);
        if (current == null) {
            liveData.postValue(null);
            return;
        }
        final Integer nextPage = current.next;
        if (nextPage == null) {
            liveData.postValue(Resource.success(false));
            return;
        }
        try {
            Response<VaccinationResponse> response = vaccinationService
                    .getAll(nextPage).execute();
            ApiResponse<VaccinationResponse> apiResponse = new ApiResponse<>(response);
            if (apiResponse.isSuccessful()) {
                // we merge all repo ids into 1 list so that it is easier to fetch the result list.
                List<String> ids = new ArrayList<>();
                ids.addAll(current.vaccinationIds);
                //noinspection ConstantConditions
                ids.addAll(apiResponse.body.getVaccinationIds());
                VaccinationResult merged = new VaccinationResult(query, ids,
                        apiResponse.body.getTotal(), apiResponse.getNextPage());
                try {
                    db.beginTransaction();
                    db.vaccinationDao().insert(merged);
                    db.vaccinationDao().insertVaccinations(apiResponse.body.getItems());
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                liveData.postValue(Resource.success(apiResponse.getNextPage() != null));
            } else {
                liveData.postValue(Resource.error(apiResponse.errorMessage, true));
            }
        } catch (IOException e) {
            liveData.postValue(Resource.error(e.getMessage(), true));
        }
    }

    LiveData<Resource<Boolean>> getLiveData() {
        return liveData;
    }
}

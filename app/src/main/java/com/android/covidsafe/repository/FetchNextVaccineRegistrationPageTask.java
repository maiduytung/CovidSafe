package com.android.covidsafe.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.api.VaccineRegistrationResponse;
import com.android.covidsafe.api.VaccineRegistrationService;
import com.android.covidsafe.db.SecureDatabase;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.VaccineRegistrationResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * A task that reads the search result in the database and fetches the next page, if it has one.
 */
public class FetchNextVaccineRegistrationPageTask implements Runnable {
    private final MutableLiveData<Resource<Boolean>> liveData = new MutableLiveData<>();
    private final String query;
    private final VaccineRegistrationService vaccineRegistrationService;
    private final SecureDatabase db;

    FetchNextVaccineRegistrationPageTask(String query, VaccineRegistrationService vaccineRegistrationService, SecureDatabase db) {
        this.query = query;
        this.vaccineRegistrationService = vaccineRegistrationService;
        this.db = db;
    }

    @Override
    public void run() {
        VaccineRegistrationResult current = db.vaccineRegistrationDao().findVaccineRegistrationResult(query);
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
            Response<VaccineRegistrationResponse> response = vaccineRegistrationService
                    .getAll(nextPage).execute();
            ApiResponse<VaccineRegistrationResponse> apiResponse = new ApiResponse<>(response);
            if (apiResponse.isSuccessful()) {
                // we merge all repo ids into 1 list so that it is easier to fetch the result list.
                List<String> ids = new ArrayList<>();
                ids.addAll(current.vaccineRegistrationIds);
                //noinspection ConstantConditions
                ids.addAll(apiResponse.body.getVaccineRegistrationIds());
                VaccineRegistrationResult merged = new VaccineRegistrationResult(query, ids,
                        apiResponse.body.getTotal(), apiResponse.getNextPage());
                try {
                    db.beginTransaction();
                    db.vaccineRegistrationDao().insert(merged);
                    db.vaccineRegistrationDao().insertVaccineRegistrations(apiResponse.body.getItems());
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

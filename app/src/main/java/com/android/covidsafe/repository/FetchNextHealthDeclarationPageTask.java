package com.android.covidsafe.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.api.HealthDeclarationResponse;
import com.android.covidsafe.api.HealthDeclarationService;
import com.android.covidsafe.db.AppDatabase;
import com.android.covidsafe.vo.HealthDeclarationResult;
import com.android.covidsafe.vo.Resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

/**
 * A task that reads the search result in the database and fetches the next page, if it has one.
 */
public class FetchNextHealthDeclarationPageTask implements Runnable {
    private final MutableLiveData<Resource<Boolean>> liveData = new MutableLiveData<>();
    private final String query;
    private final HealthDeclarationService healthDeclarationService;
    private final AppDatabase db;

    FetchNextHealthDeclarationPageTask(String query, HealthDeclarationService healthDeclarationService, AppDatabase db) {
        this.query = query;
        this.healthDeclarationService = healthDeclarationService;
        this.db = db;
    }

    @Override
    public void run() {
        HealthDeclarationResult current = db.healthDeclarationDao().findHealthDeclarationResult(query);
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
            Response<HealthDeclarationResponse> response = healthDeclarationService
                    .getAll(nextPage).execute();
            ApiResponse<HealthDeclarationResponse> apiResponse = new ApiResponse<>(response);
            if (apiResponse.isSuccessful()) {
                // we merge all repo ids into 1 list so that it is easier to fetch the result list.
                List<String> ids = new ArrayList<>();
                ids.addAll(current.healthDeclarationIds);
                //noinspection ConstantConditions
                ids.addAll(apiResponse.body.getHealthDeclarationIds());
                HealthDeclarationResult merged = new HealthDeclarationResult(query, ids,
                        apiResponse.body.getTotal(), apiResponse.getNextPage());
                try {
                    db.beginTransaction();
                    db.healthDeclarationDao().insert(merged);
                    db.healthDeclarationDao().insertHealthDeclarations(apiResponse.body.getItems());
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

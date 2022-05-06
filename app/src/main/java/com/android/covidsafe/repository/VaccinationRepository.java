package com.android.covidsafe.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.api.VaccinationResponse;
import com.android.covidsafe.api.VaccinationService;
import com.android.covidsafe.db.SecureDatabase;
import com.android.covidsafe.db.VaccinationDao;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.utilities.RateLimiter;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.Vaccination;
import com.android.covidsafe.vo.VaccinationResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles Vaccination instances.
 * <p>
 * unfortunate naming :/ .
 * Vaccination - value object name
 * Repository - type of this class.
 */
@Singleton
public class VaccinationRepository {

    private final SecureDatabase db;

    private final VaccinationDao vaccinationDao;

    private final VaccinationService vaccinationService;

    private final AppExecutors appExecutors;

    private RateLimiter<String> vaccinationListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    public VaccinationRepository(AppExecutors appExecutors, SecureDatabase db, VaccinationDao vaccinationDao,
                                 VaccinationService vaccinationService) {
        this.db = db;
        this.vaccinationDao = vaccinationDao;
        this.vaccinationService = vaccinationService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<Vaccination>> getOne(String id) {
        return new NetworkBoundResource<Vaccination, Vaccination>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Vaccination item) {
                vaccinationDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Vaccination data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<Vaccination> loadFromDb() {
                return vaccinationDao.load(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Vaccination>> createCall() {
                return vaccinationService.getOne(id);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPage(String query) {
        FetchNextVaccinationPageTask fetchNextVaccinationPageTask = new FetchNextVaccinationPageTask(
                query, vaccinationService, db);
        appExecutors.networkIO().execute(fetchNextVaccinationPageTask);
        return fetchNextVaccinationPageTask.getLiveData();
    }

    public LiveData<Resource<List<Vaccination>>> getAll(String query) {
        return new NetworkBoundResource<List<Vaccination>, VaccinationResponse>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull VaccinationResponse item) {
                List<String> vaccinationIds = item.getVaccinationIds();
                VaccinationResult vaccinationResult = new VaccinationResult(
                        query, vaccinationIds, item.getTotal(), item.getNextPage());
                db.beginTransaction();
                try {
                    vaccinationDao.insertVaccinations(item.getItems());
                    vaccinationDao.insert(vaccinationResult);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Vaccination> data) {
//                return data == null;
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Vaccination>> loadFromDb() {
                return Transformations.switchMap(vaccinationDao.loadResult(query), resultData -> {
                    if (resultData == null) {
                        return AbsentLiveData.create();
                    } else {
                        return vaccinationDao.loadOrdered(resultData.vaccinationIds);
                    }
                });
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<VaccinationResponse>> createCall() {
                return vaccinationService.getAll();
            }

            @Override
            protected VaccinationResponse processResponse(ApiResponse<VaccinationResponse> response) {
                VaccinationResponse body = response.body;
                if (body != null) {
                    body.setNextPage(response.getNextPage());
                }
                return body;
            }
        }.asLiveData();
    }

}

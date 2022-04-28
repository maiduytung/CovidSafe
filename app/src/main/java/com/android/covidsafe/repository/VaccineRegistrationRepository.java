package com.android.covidsafe.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.api.VaccineRegistrationResponse;
import com.android.covidsafe.api.VaccineRegistrationService;
import com.android.covidsafe.db.AppDatabase;
import com.android.covidsafe.db.VaccineRegistrationDao;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.utilities.RateLimiter;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.VaccineRegistration;
import com.android.covidsafe.vo.VaccineRegistrationResult;
import com.android.covidsafe.vo.request.VaccineRegistrationRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository that handles VaccineRegistration instances.
 * <p>
 * unfortunate naming :/ .
 * VaccineRegistration - value object name
 * Repository - type of this class.
 */
@Singleton
public class VaccineRegistrationRepository {

    private final AppDatabase db;

    private final VaccineRegistrationDao vaccineRegistrationDao;

    private final VaccineRegistrationService vaccineRegistrationService;

    private final AppExecutors appExecutors;

    private RateLimiter<String> vaccineRegistrationListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    public VaccineRegistrationRepository(AppExecutors appExecutors, AppDatabase db, VaccineRegistrationDao vaccineRegistrationDao,
                                         VaccineRegistrationService vaccineRegistrationService) {
        this.db = db;
        this.vaccineRegistrationDao = vaccineRegistrationDao;
        this.vaccineRegistrationService = vaccineRegistrationService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<VaccineRegistration>> add(VaccineRegistrationRequest vaccineRegistrationRequest) {
        return new NetworkResource<VaccineRegistration>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull VaccineRegistration item) {
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<VaccineRegistration>> createCall() {
                // Temporary solution
                MutableLiveData<ApiResponse<VaccineRegistration>> vaccineRegistration = new MutableLiveData<>();
                vaccineRegistrationService.add(vaccineRegistrationRequest).enqueue(new Callback<VaccineRegistration>() {
                    @Override
                    public void onResponse(Call<VaccineRegistration> call, Response<VaccineRegistration> response) {
                        ApiResponse<VaccineRegistration> apiResponse = new ApiResponse<>(response);
                        if (apiResponse.isSuccessful()) {
                            vaccineRegistration.setValue(apiResponse);
                        }
                    }

                    @Override
                    public void onFailure(Call<VaccineRegistration> call, Throwable t) {

                    }
                });
                return vaccineRegistration;
            }
        }.asLiveData();
    }

    public LiveData<Resource<VaccineRegistration>> update(VaccineRegistrationRequest vaccineRegistrationRequest, String id) {
        return new NetworkResource<VaccineRegistration>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull VaccineRegistration item) {
                vaccineRegistrationDao.insert(item);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<VaccineRegistration>> createCall() {
                MutableLiveData<ApiResponse<VaccineRegistration>> vaccineRegistration = new MutableLiveData<>();
                vaccineRegistrationService.update(id, vaccineRegistrationRequest).enqueue(new Callback<VaccineRegistration>() {
                    @Override
                    public void onResponse(Call<VaccineRegistration> call, Response<VaccineRegistration> response) {
                        ApiResponse<VaccineRegistration> apiResponse = new ApiResponse<>(response);
                        if (apiResponse.isSuccessful()) {
                            vaccineRegistration.setValue(apiResponse);
                        }
                    }

                    @Override
                    public void onFailure(Call<VaccineRegistration> call, Throwable t) {

                    }
                });
                return vaccineRegistration;
            }
        }.asLiveData();
    }

    public LiveData<Resource<VaccineRegistration>> getOne(String id) {
        return new NetworkBoundResource<VaccineRegistration, VaccineRegistration>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull VaccineRegistration item) {
                vaccineRegistrationDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable VaccineRegistration data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<VaccineRegistration> loadFromDb() {
                return vaccineRegistrationDao.load(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<VaccineRegistration>> createCall() {
                return vaccineRegistrationService.getOne(id);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPage(String query) {
        FetchNextVaccineRegistrationPageTask fetchNextVaccineRegistrationPageTask = new FetchNextVaccineRegistrationPageTask(
                query, vaccineRegistrationService, db);
        appExecutors.networkIO().execute(fetchNextVaccineRegistrationPageTask);
        return fetchNextVaccineRegistrationPageTask.getLiveData();
    }

    public LiveData<Resource<List<VaccineRegistration>>> getAll(String query) {
        return new NetworkBoundResource<List<VaccineRegistration>, VaccineRegistrationResponse>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull VaccineRegistrationResponse item) {
                List<String> vaccineRegistrationIds = item.getVaccineRegistrationIds();
                VaccineRegistrationResult vaccineRegistrationResult = new VaccineRegistrationResult(
                        query, vaccineRegistrationIds, item.getTotal(), item.getNextPage());
                db.beginTransaction();
                try {
                    vaccineRegistrationDao.insertVaccineRegistrations(item.getItems());
                    vaccineRegistrationDao.insert(vaccineRegistrationResult);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<VaccineRegistration> data) {
//                return data == null;
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<VaccineRegistration>> loadFromDb() {
                return Transformations.switchMap(vaccineRegistrationDao.loadResult(query), resultData -> {
                    if (resultData == null) {
                        return AbsentLiveData.create();
                    } else {
                        return vaccineRegistrationDao.loadOrdered(resultData.vaccineRegistrationIds);
                    }
                });
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<VaccineRegistrationResponse>> createCall() {
                return vaccineRegistrationService.getAll();
            }

            @Override
            protected VaccineRegistrationResponse processResponse(ApiResponse<VaccineRegistrationResponse> response) {
                VaccineRegistrationResponse body = response.body;
                if (body != null) {
                    body.setNextPage(response.getNextPage());
                }
                return body;
            }
        }.asLiveData();
    }

}

package com.android.covidsafe.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.api.HealthDeclarationResponse;
import com.android.covidsafe.api.HealthDeclarationService;
import com.android.covidsafe.db.SecureDatabase;
import com.android.covidsafe.db.HealthDeclarationDao;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.utilities.RateLimiter;
import com.android.covidsafe.vo.HealthDeclaration;
import com.android.covidsafe.vo.HealthDeclarationResult;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.request.HealthDeclarationRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository that handles HealthDeclaration instances.
 * <p>
 * unfortunate naming :/ .
 * HealthDeclaration - value object name
 * Repository - type of this class.
 */
@Singleton
public class HealthDeclarationRepository {

    private final SecureDatabase db;

    private final HealthDeclarationDao healthDeclarationDao;

    private final HealthDeclarationService healthDeclarationService;

    private final AppExecutors appExecutors;

    private RateLimiter<String> healthDeclarationListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    public HealthDeclarationRepository(AppExecutors appExecutors, SecureDatabase db, HealthDeclarationDao healthDeclarationDao,
                                       HealthDeclarationService healthDeclarationService) {
        this.db = db;
        this.healthDeclarationDao = healthDeclarationDao;
        this.healthDeclarationService = healthDeclarationService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<HealthDeclaration>> add(HealthDeclarationRequest healthDeclarationRequest) {
        return new NetworkResource<HealthDeclaration>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull HealthDeclaration item) {
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<HealthDeclaration>> createCall() {
                // Temporary solution
                MutableLiveData<ApiResponse<HealthDeclaration>> healthDeclaration = new MutableLiveData<>();
                healthDeclarationService.add(healthDeclarationRequest).enqueue(new Callback<HealthDeclaration>() {
                    @Override
                    public void onResponse(Call<HealthDeclaration> call, Response<HealthDeclaration> response) {
                        ApiResponse<HealthDeclaration> apiResponse = new ApiResponse<>(response);
                        if (apiResponse.isSuccessful()) {
                            healthDeclaration.setValue(apiResponse);
                        }
                    }

                    @Override
                    public void onFailure(Call<HealthDeclaration> call, Throwable t) {

                    }
                });
                return healthDeclaration;
            }
        }.asLiveData();
    }

    public LiveData<Resource<HealthDeclaration>> update(HealthDeclarationRequest healthDeclarationRequest, String id) {
        return new NetworkResource<HealthDeclaration>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull HealthDeclaration item) {
                healthDeclarationDao.insert(item);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<HealthDeclaration>> createCall() {
                MutableLiveData<ApiResponse<HealthDeclaration>> healthDeclaration = new MutableLiveData<>();
                healthDeclarationService.update(id, healthDeclarationRequest).enqueue(new Callback<HealthDeclaration>() {
                    @Override
                    public void onResponse(Call<HealthDeclaration> call, Response<HealthDeclaration> response) {
                        ApiResponse<HealthDeclaration> apiResponse = new ApiResponse<>(response);
                        if (apiResponse.isSuccessful()) {
                            healthDeclaration.setValue(apiResponse);
                        }
                    }

                    @Override
                    public void onFailure(Call<HealthDeclaration> call, Throwable t) {

                    }
                });
                return healthDeclaration;
            }
        }.asLiveData();
    }

    public LiveData<Resource<HealthDeclaration>> getOne(String id) {
        return new NetworkBoundResource<HealthDeclaration, HealthDeclaration>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull HealthDeclaration item) {
                healthDeclarationDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable HealthDeclaration data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<HealthDeclaration> loadFromDb() {
                return healthDeclarationDao.load(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<HealthDeclaration>> createCall() {
                return healthDeclarationService.getOne(id);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPage(String query) {
        FetchNextHealthDeclarationPageTask fetchNextHealthDeclarationPageTask = new FetchNextHealthDeclarationPageTask(
                query, healthDeclarationService, db);
        appExecutors.networkIO().execute(fetchNextHealthDeclarationPageTask);
        return fetchNextHealthDeclarationPageTask.getLiveData();
    }

    public LiveData<Resource<List<HealthDeclaration>>> getAll(String query) {
        return new NetworkBoundResource<List<HealthDeclaration>, HealthDeclarationResponse>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull HealthDeclarationResponse item) {
                List<String> healthDeclarationIds = item.getHealthDeclarationIds();
                HealthDeclarationResult healthDeclarationResult = new HealthDeclarationResult(
                        query, healthDeclarationIds, item.getTotal(), item.getNextPage());
                db.beginTransaction();
                try {
                    healthDeclarationDao.insertHealthDeclarations(item.getItems());
                    healthDeclarationDao.insert(healthDeclarationResult);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<HealthDeclaration> data) {
//                return data == null;
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<HealthDeclaration>> loadFromDb() {
                return Transformations.switchMap(healthDeclarationDao.loadResult(query), resultData -> {
                    if (resultData == null) {
                        return AbsentLiveData.create();
                    } else {
                        return healthDeclarationDao.loadOrdered(resultData.healthDeclarationIds);
                    }
                });
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<HealthDeclarationResponse>> createCall() {
                return healthDeclarationService.getAll();
            }

            @Override
            protected HealthDeclarationResponse processResponse(ApiResponse<HealthDeclarationResponse> response) {
                HealthDeclarationResponse body = response.body;
                if (body != null) {
                    body.setNextPage(response.getNextPage());
                }
                return body;
            }
        }.asLiveData();
    }

}

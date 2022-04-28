package com.android.covidsafe.repository;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.vo.Resource;

/**
 * A class that can provide a resource by the network.
 * <p>
 *
 * @param <RequestType>
 */
public abstract class NetworkResource<RequestType> {
    private final AppExecutors appExecutors;

    private final MediatorLiveData<Resource<RequestType>> result = new MediatorLiveData<>();

    @MainThread
    NetworkResource(AppExecutors appExecutors) {
        this.appExecutors = appExecutors;
        result.setValue(Resource.loading(null));

        fetchFromNetwork();
    }

    private void fetchFromNetwork() {
        LiveData<ApiResponse<RequestType>> apiResponse = createCall();
        result.addSource(apiResponse, response -> {
            result.removeSource(apiResponse);
            //noinspection ConstantConditions
            if (response.isSuccessful()) {
                appExecutors.diskIO().execute(() -> {
                    saveCallResult(processResponse(response));
                    appExecutors.mainThread().execute(() ->
                            result.setValue(Resource.success(response.body))
                    );
                });
            } else {
                onFetchFailed();
                result.setValue(Resource.error(response.errorMessage, null));
            }
        });
    }

    protected void onFetchFailed() {
    }

    public LiveData<Resource<RequestType>> asLiveData() {
        return result;
    }

    @WorkerThread
    protected RequestType processResponse(ApiResponse<RequestType> response) {
        return response.body;
    }

    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    @NonNull
    @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();
}

package com.android.covidsafe.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.api.CertificationService;
import com.android.covidsafe.db.SecureDatabase;
import com.android.covidsafe.db.CertificationDao;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.utilities.RateLimiter;
import com.android.covidsafe.vo.Certification;
import com.android.covidsafe.vo.Resource;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles Certification objects.
 */
@Singleton
public class CertificationRepository {
    private final SecureDatabase db;
    private final CertificationDao certificationDao;
    private final CertificationService certificationService;
    private final AppExecutors appExecutors;
    private RateLimiter<String> certificationRateLimit = new RateLimiter<>(5, TimeUnit.MINUTES);

    @Inject
    CertificationRepository(AppExecutors appExecutors, SecureDatabase db, CertificationDao certificationDao, CertificationService certificationService) {
        this.appExecutors = appExecutors;
        this.db = db;
        this.certificationDao = certificationDao;
        this.certificationService = certificationService;
    }

    public LiveData<Resource<Certification>> loadCertification() {
        return new NetworkBoundResource<Certification, Certification>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Certification item) {
                certificationDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Certification data) {
                return data == null || certificationRateLimit.shouldFetch(Constants.CERTIFICATION_KEY);
            }

            @NonNull
            @Override
            protected LiveData<Certification> loadFromDb() {
                return certificationDao.loadCertification();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Certification>> createCall() {
                return certificationService.getCurrentUser();
            }

            @Override
            protected void onFetchFailed() {
                certificationRateLimit.reset(Constants.CERTIFICATION_KEY);
            }
        }.asLiveData();
    }
}

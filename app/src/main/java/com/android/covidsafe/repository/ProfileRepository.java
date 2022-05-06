package com.android.covidsafe.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.api.ProfileService;
import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.db.SecureDatabase;
import com.android.covidsafe.db.ProfileDao;
import com.android.covidsafe.utilities.RateLimiter;
import com.android.covidsafe.vo.Profile;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.request.ProfileRequest;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles Profile objects.
 */
@Singleton
public class ProfileRepository {
    private final SecureDatabase db;
    private final ProfileDao profileDao;
    private final ProfileService profileService;
    private final AppExecutors appExecutors;
    private RateLimiter<String> profileRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    ProfileRepository(AppExecutors appExecutors, SecureDatabase db, ProfileDao profileDao, ProfileService profileService) {
        this.appExecutors = appExecutors;
        this.db = db;
        this.profileDao = profileDao;
        this.profileService = profileService;
    }

    public LiveData<Resource<Profile>> loadProfile() {
        return new NetworkBoundResource<Profile, Profile>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Profile item) {
                profileDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Profile data) {
                return data == null || profileRateLimit.shouldFetch("profile");
            }

            @NonNull
            @Override
            protected LiveData<Profile> loadFromDb() {
                return profileDao.loadProfile();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Profile>> createCall() {
                return profileService.getProfile();
            }

            @Override
            protected void onFetchFailed() {
                profileRateLimit.reset("profile");
            }
        }.asLiveData();
    }

    public LiveData<Resource<Profile>> updateProfile(ProfileRequest profileRequest) {
        return new NetworkResource<Profile>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Profile item) {
                profileDao.insert(item);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Profile>> createCall() {
                return profileService.updateProfile(profileRequest);
            }
        }.asLiveData();
    }
}

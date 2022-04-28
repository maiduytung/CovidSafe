package com.android.covidsafe.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.api.APIService;
import com.android.covidsafe.db.AppDatabase;
import com.android.covidsafe.db.UserDao;
import com.android.covidsafe.utilities.Constants;
import com.android.covidsafe.utilities.RateLimiter;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.User;
import com.android.covidsafe.vo.request.ProfileRequest;
import com.android.covidsafe.vo.response.BaseResponse;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles User objects.
 */
@Singleton
public class UserRepository {
    private final AppDatabase db;
    private final UserDao userDao;
    private final APIService apiService;
    private final AppExecutors appExecutors;
    private final ISharedPreferences sharedPreference;
    private RateLimiter<String> userRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    UserRepository(AppExecutors appExecutors, AppDatabase db, UserDao userDao, APIService apiService, ISharedPreferences sharedPreference) {
        this.appExecutors = appExecutors;
        this.db = db;
        this.userDao = userDao;
        this.apiService = apiService;
        this.sharedPreference = sharedPreference;
    }

    public LiveData<Resource<User>> loadUser() {
        return new NetworkBoundResource<User, User>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull User item) {
                userDao.insert(item);
                sharedPreference.putString(Constants.CURRENT_USER_ID, item.id);
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return data == null || userRateLimit.shouldFetch("user");
            }

            @NonNull
            @Override
            protected LiveData<User> loadFromDb() {
                return userDao.loadUser();
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return apiService.getUser();
            }

            @Override
            protected void onFetchFailed() {
                userRateLimit.reset("user");
            }
        }.asLiveData();
    }
}

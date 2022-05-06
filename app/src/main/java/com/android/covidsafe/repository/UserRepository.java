package com.android.covidsafe.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.api.UserService;
import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.db.SecureDatabase;
import com.android.covidsafe.db.UserDao;
import com.android.covidsafe.utilities.RateLimiter;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.User;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles User objects.
 */
@Singleton
public class UserRepository {
    private final SecureDatabase db;
    private final UserDao userDao;
    private final UserService userService;
    private final AppExecutors appExecutors;
    private RateLimiter<String> userRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    UserRepository(AppExecutors appExecutors, SecureDatabase db, UserDao userDao, UserService userService) {
        this.appExecutors = appExecutors;
        this.db = db;
        this.userDao = userDao;
        this.userService = userService;
    }

    public LiveData<Resource<User>> loadUser() {
        return new NetworkBoundResource<User, User>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull User item) {
                userDao.insert(item);
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
                return userService.getUser();
            }

            @Override
            protected void onFetchFailed() {
                userRateLimit.reset("user");
            }
        }.asLiveData();
    }
}

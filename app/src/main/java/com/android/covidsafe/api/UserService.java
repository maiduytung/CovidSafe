package com.android.covidsafe.api;

import androidx.lifecycle.LiveData;

import com.android.covidsafe.vo.Profile;
import com.android.covidsafe.vo.User;
import com.android.covidsafe.vo.request.ProfileRequest;

import javax.inject.Singleton;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;

/**
 * REST API access points
 */
@Singleton
public interface UserService {

    @GET("/api/user")
    LiveData<ApiResponse<User>> getUser();

}

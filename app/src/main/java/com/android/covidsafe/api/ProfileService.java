package com.android.covidsafe.api;

import androidx.lifecycle.LiveData;

import com.android.covidsafe.vo.Profile;
import com.android.covidsafe.vo.Token;
import com.android.covidsafe.vo.User;
import com.android.covidsafe.vo.Verification;
import com.android.covidsafe.vo.request.LoginRequest;
import com.android.covidsafe.vo.request.OTPVerificationRequest;
import com.android.covidsafe.vo.request.ProfileRequest;
import com.android.covidsafe.vo.request.RefreshRequest;
import com.android.covidsafe.vo.request.RegisterRequest;
import com.android.covidsafe.vo.request.SendVerificationRequest;
import com.android.covidsafe.vo.response.BaseResponse;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

/**
 * REST API access points
 */
@Singleton
public interface ProfileService {

    @GET("/api/user/profile")
    LiveData<ApiResponse<Profile>> getProfile();

    @PUT("/api/user/profile")
    LiveData<ApiResponse<Profile>> updateProfile(@Body ProfileRequest profileRequest);

}

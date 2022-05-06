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
public interface AuthService {

    @POST("/api/auth/register")
    LiveData<ApiResponse<Token>> register(@Body RegisterRequest registerRequest);

    @POST("/api/auth/login")
    LiveData<ApiResponse<Token>> login(@Body LoginRequest loginRequest);

    @POST("/api/auth/otp")
    LiveData<ApiResponse<BaseResponse>> sendVerification(@Body SendVerificationRequest sendVerificationRequest);

    @POST("/api/auth/otp/verification")
    LiveData<ApiResponse<Verification>> verify(@Body OTPVerificationRequest otpVerificationRequest);

    @POST("/api/auth/refresh")
    Call<Token> refreshAccessToken(@Body RefreshRequest refreshRequest);

}

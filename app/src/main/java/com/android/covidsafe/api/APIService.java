package com.android.covidsafe.api;

import androidx.lifecycle.LiveData;

import com.android.covidsafe.vo.Contributor;
import com.android.covidsafe.vo.HealthDeclaration;
import com.android.covidsafe.vo.Profile;
import com.android.covidsafe.vo.Repo;
import com.android.covidsafe.vo.Token;
import com.android.covidsafe.vo.User;
import com.android.covidsafe.vo.Verification;
import com.android.covidsafe.vo.request.HealthDeclarationRequest;
import com.android.covidsafe.vo.request.LoginRequest;
import com.android.covidsafe.vo.request.OTPVerificationRequest;
import com.android.covidsafe.vo.request.ProfileRequest;
import com.android.covidsafe.vo.request.RefreshRequest;
import com.android.covidsafe.vo.request.RegisterRequest;
import com.android.covidsafe.vo.request.SendVerificationRequest;
import com.android.covidsafe.vo.response.BaseResponse;

import java.util.List;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * REST API access points
 */
@Singleton
public interface APIService {

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

    @GET("/api/user")
    LiveData<ApiResponse<User>> getUser();

    @GET("/api/user/profile")
    LiveData<ApiResponse<Profile>> getProfile();

    @PUT("/api/user/profile")
    LiveData<ApiResponse<Profile>> updateProfile(@Body ProfileRequest profileRequest);

    @GET("users/{login}")
    LiveData<ApiResponse<User>> getUser(@Path("login") String login);

    @GET("users/{login}/repos")
    LiveData<ApiResponse<List<Repo>>> getRepos(@Path("login") String login);

    @GET("repos/{owner}/{name}")
    LiveData<ApiResponse<Repo>> getRepo(@Path("owner") String owner, @Path("name") String name);

    @GET("repos/{owner}/{name}/contributors")
    LiveData<ApiResponse<List<Contributor>>> getContributors(@Path("owner") String owner, @Path("name") String name);

    @GET("search/repositories")
    LiveData<ApiResponse<RepoSearchResponse>> searchRepos(@Query("q") String query);

    @GET("search/repositories")
    Call<RepoSearchResponse> searchRepos(@Query("q") String query, @Query("page") int page);
}

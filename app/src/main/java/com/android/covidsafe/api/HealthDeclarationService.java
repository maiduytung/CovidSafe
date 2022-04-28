package com.android.covidsafe.api;

import androidx.lifecycle.LiveData;

import com.android.covidsafe.vo.HealthDeclaration;
import com.android.covidsafe.vo.request.HealthDeclarationRequest;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * REST API access points
 */
@Singleton
public interface HealthDeclarationService {

    @POST("/api/user/declaration")
    Call<HealthDeclaration> add(@Body HealthDeclarationRequest healthDeclarationRequest);

    @PUT("/api/user/declaration/{id}")
    Call<HealthDeclaration> update(@Path("id") String id, @Body HealthDeclarationRequest healthDeclarationRequest);

    @GET("/api/user/declaration/{id}")
    LiveData<ApiResponse<HealthDeclaration>> getOne(@Path("id") String id);

    @GET("/api/user/declaration")
    LiveData<ApiResponse<HealthDeclarationResponse>> getAll();

    @GET("/api/user/declaration")
    Call<HealthDeclarationResponse> getAll(@Query("page") int page);
}

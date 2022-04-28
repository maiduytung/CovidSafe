package com.android.covidsafe.api;

import androidx.lifecycle.LiveData;

import com.android.covidsafe.vo.VaccineRegistration;
import com.android.covidsafe.vo.request.VaccineRegistrationRequest;

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
public interface VaccineRegistrationService {

    @POST("/api/user/registration")
    Call<VaccineRegistration> add(@Body VaccineRegistrationRequest vaccineRegistrationRequest);

    @PUT("/api/user/registration/{id}")
    Call<VaccineRegistration> update(@Path("id") String id, @Body VaccineRegistrationRequest vaccineRegistrationRequest);

    @GET("/api/user/registration/{id}")
    LiveData<ApiResponse<VaccineRegistration>> getOne(@Path("id") String id);

    @GET("/api/user/registration")
    LiveData<ApiResponse<VaccineRegistrationResponse>> getAll();

    @GET("/api/user/registration")
    Call<VaccineRegistrationResponse> getAll(@Query("page") int page);
}

package com.android.covidsafe.api;

import androidx.lifecycle.LiveData;

import com.android.covidsafe.vo.Vaccination;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * REST API access points
 */
@Singleton
public interface VaccinationService {

    @GET("/api/users/vaccination/{id}")
    LiveData<ApiResponse<Vaccination>> getOne(@Path("id") String id);

    @GET("/api/user/vaccination")
    LiveData<ApiResponse<VaccinationResponse>> getAll();

    @GET("/api/user/vaccination")
    Call<VaccinationResponse> getAll(@Query("page") int page);
}

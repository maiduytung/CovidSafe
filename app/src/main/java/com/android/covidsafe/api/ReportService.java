package com.android.covidsafe.api;

import androidx.lifecycle.LiveData;

import com.android.covidsafe.vo.Report;
import com.android.covidsafe.vo.VaccineRegistration;
import com.android.covidsafe.vo.request.ReportRequest;
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
public interface ReportService {

    @POST("/api/user/report")
    Call<Report> add(@Body ReportRequest ReportRequest);

    @PUT("/api/user/report/{id}")
    Call<Report> update(@Path("id") String id, @Body ReportRequest ReportRequest);

    @GET("/api/user/report/{id}")
    LiveData<ApiResponse<Report>> getOne(@Path("id") String id);

    @GET("/api/user/report")
    LiveData<ApiResponse<ReportResponse>> getAll();

    @GET("/api/user/report")
    Call<ReportResponse> getAll(@Query("page") int page);
}

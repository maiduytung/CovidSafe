package com.android.covidsafe.api;

import androidx.lifecycle.LiveData;

import com.android.covidsafe.vo.Certification;

import javax.inject.Singleton;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * REST API access points
 */
@Singleton
public interface CertificationService {

    @GET("/api/users/certification/{id}")
    LiveData<ApiResponse<Certification>> getOne(@Path("id") String id);
}

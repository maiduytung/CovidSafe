package com.android.covidsafe.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.api.ApiResponse;
import com.android.covidsafe.api.ReportResponse;
import com.android.covidsafe.api.ReportService;
import com.android.covidsafe.db.AppDatabase;
import com.android.covidsafe.db.ReportDao;
import com.android.covidsafe.utilities.AbsentLiveData;
import com.android.covidsafe.utilities.RateLimiter;
import com.android.covidsafe.vo.Report;
import com.android.covidsafe.vo.ReportResult;
import com.android.covidsafe.vo.Resource;
import com.android.covidsafe.vo.request.ReportRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository that handles Report instances.
 * <p>
 * unfortunate naming :/ .
 * Report - value object name
 * Repository - type of this class.
 */
@Singleton
public class ReportRepository {

    private final AppDatabase db;

    private final ReportDao reportDao;

    private final ReportService reportService;

    private final AppExecutors appExecutors;

    private RateLimiter<String> reportListRateLimit = new RateLimiter<>(10, TimeUnit.MINUTES);

    @Inject
    public ReportRepository(AppExecutors appExecutors, AppDatabase db, ReportDao reportDao,
                            ReportService reportService) {
        this.db = db;
        this.reportDao = reportDao;
        this.reportService = reportService;
        this.appExecutors = appExecutors;
    }

    public LiveData<Resource<Report>> add(ReportRequest reportRequest) {
        return new NetworkResource<Report>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Report item) {
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Report>> createCall() {
                // Temporary solution
                MutableLiveData<ApiResponse<Report>> report = new MutableLiveData<>();
                reportService.add(reportRequest).enqueue(new Callback<Report>() {
                    @Override
                    public void onResponse(Call<Report> call, Response<Report> response) {
                        ApiResponse<Report> apiResponse = new ApiResponse<>(response);
                        if (apiResponse.isSuccessful()) {
                            report.setValue(apiResponse);
                        }
                    }

                    @Override
                    public void onFailure(Call<Report> call, Throwable t) {

                    }
                });
                return report;
            }
        }.asLiveData();
    }

    public LiveData<Resource<Report>> update(ReportRequest reportRequest, String id) {
        return new NetworkResource<Report>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Report item) {
                reportDao.insert(item);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Report>> createCall() {
                MutableLiveData<ApiResponse<Report>> report = new MutableLiveData<>();
                reportService.update(id, reportRequest).enqueue(new Callback<Report>() {
                    @Override
                    public void onResponse(Call<Report> call, Response<Report> response) {
                        ApiResponse<Report> apiResponse = new ApiResponse<>(response);
                        if (apiResponse.isSuccessful()) {
                            report.setValue(apiResponse);
                        }
                    }

                    @Override
                    public void onFailure(Call<Report> call, Throwable t) {

                    }
                });
                return report;
            }
        }.asLiveData();
    }

    public LiveData<Resource<Report>> getOne(String id) {
        return new NetworkBoundResource<Report, Report>(appExecutors) {
            @Override
            protected void saveCallResult(@NonNull Report item) {
                reportDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Report data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<Report> loadFromDb() {
                return reportDao.load(id);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Report>> createCall() {
                return reportService.getOne(id);
            }
        }.asLiveData();
    }

    public LiveData<Resource<Boolean>> getNextPage(String query) {
        FetchNextReportPageTask fetchNextReportPageTask = new FetchNextReportPageTask(
                query, reportService, db);
        appExecutors.networkIO().execute(fetchNextReportPageTask);
        return fetchNextReportPageTask.getLiveData();
    }

    public LiveData<Resource<List<Report>>> getAll(String query) {
        return new NetworkBoundResource<List<Report>, ReportResponse>(appExecutors) {

            @Override
            protected void saveCallResult(@NonNull ReportResponse item) {
                List<String> reportIds = item.getReportIds();
                ReportResult reportResult = new ReportResult(
                        query, reportIds, item.getTotal(), item.getNextPage());
                db.beginTransaction();
                try {
                    reportDao.insertReports(item.getItems());
                    reportDao.insert(reportResult);
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Report> data) {
//                return data == null;
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Report>> loadFromDb() {
                return Transformations.switchMap(reportDao.loadResult(query), resultData -> {
                    if (resultData == null) {
                        return AbsentLiveData.create();
                    } else {
                        return reportDao.loadOrdered(resultData.reportIds);
                    }
                });
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ReportResponse>> createCall() {
                return reportService.getAll();
            }

            @Override
            protected ReportResponse processResponse(ApiResponse<ReportResponse> response) {
                ReportResponse body = response.body;
                if (body != null) {
                    body.setNextPage(response.getNextPage());
                }
                return body;
            }
        }.asLiveData();
    }

}

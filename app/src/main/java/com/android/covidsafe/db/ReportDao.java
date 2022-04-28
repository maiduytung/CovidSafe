package com.android.covidsafe.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.covidsafe.vo.Report;
import com.android.covidsafe.vo.ReportResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Interface for database access on Report related operations.
 */
@Dao
public abstract class ReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Report... reports);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertReports(List<Report> reports);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long createReportIfNotExists(Report report);

    @Query("SELECT * FROM Report WHERE id = :id")
    public abstract LiveData<Report> load(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(ReportResult reportResult);

    @Query("SELECT * FROM ReportResult WHERE query = :query")
    public abstract LiveData<ReportResult> loadResult(String query);

    public LiveData<List<Report>> loadOrdered(List<String> reportIds) {
        HashMap<String, Integer> order = new HashMap<>();
//        SparseIntArray order = new SparseIntArray();
        int index = 0;
        for (String reportId : reportIds) {
            order.put(reportId, index++);
        }
        return Transformations.map(loadById(reportIds), reportList -> {
            Collections.sort(reportList, (r1, r2) -> {
                int pos1 = order.get(r1.id);
                int pos2 = order.get(r2.id);
                return pos1 - pos2;
            });
            return reportList;
        });
    }

    @Query("SELECT * FROM Report WHERE id in (:reportIds)")
    protected abstract LiveData<List<Report>> loadById(List<String> reportIds);

    @Query("SELECT * FROM ReportResult WHERE query = :query")
    public abstract ReportResult findReportResult(String query);
}

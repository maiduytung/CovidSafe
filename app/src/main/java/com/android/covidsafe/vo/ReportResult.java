package com.android.covidsafe.vo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.android.covidsafe.db.GithubTypeConverters;

import java.util.List;

@Entity(primaryKeys = {"query"})
@TypeConverters(GithubTypeConverters.class)
public class ReportResult {
    @NonNull
    public final String query;
    public final List<String> reportIds;
    public final int totalCount;
    @Nullable
    public final Integer next;

    public ReportResult(@NonNull String query, List<String> reportIds, int totalCount,
                        @Nullable Integer next) {
        this.query = query;
        this.reportIds = reportIds;
        this.totalCount = totalCount;
        this.next = next;
    }
}

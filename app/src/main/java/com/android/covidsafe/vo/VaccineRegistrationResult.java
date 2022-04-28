package com.android.covidsafe.vo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.android.covidsafe.db.GithubTypeConverters;

import java.util.List;

@Entity(primaryKeys = {"query"})
@TypeConverters(GithubTypeConverters.class)
public class VaccineRegistrationResult {
    @NonNull
    public final String query;
    public final List<String> vaccineRegistrationIds;
    public final int totalCount;
    @Nullable
    public final Integer next;

    public VaccineRegistrationResult(@NonNull String query, List<String> vaccineRegistrationIds, int totalCount,
                                     @Nullable Integer next) {
        this.query = query;
        this.vaccineRegistrationIds = vaccineRegistrationIds;
        this.totalCount = totalCount;
        this.next = next;
    }
}

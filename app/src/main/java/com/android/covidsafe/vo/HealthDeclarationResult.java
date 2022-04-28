package com.android.covidsafe.vo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.android.covidsafe.db.GithubTypeConverters;

import java.util.List;

@Entity(primaryKeys = {"query"})
@TypeConverters(GithubTypeConverters.class)
public class HealthDeclarationResult {
    @NonNull
    public final String query;
    public final List<String> healthDeclarationIds;
    public final int totalCount;
    @Nullable
    public final Integer next;

    public HealthDeclarationResult(@NonNull String query, List<String> healthDeclarationIds, int totalCount,
                                   @Nullable Integer next) {
        this.query = query;
        this.healthDeclarationIds = healthDeclarationIds;
        this.totalCount = totalCount;
        this.next = next;
    }
}

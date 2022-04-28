package com.android.covidsafe.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "id")
public class Priority {
    @NonNull
    @SerializedName("_id")
    public final String id;
    @SerializedName("name")
    public final String name;

    public Priority(@NonNull String id, String name) {
        this.id = id;
        this.name = name;
    }
}

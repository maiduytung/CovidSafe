package com.android.covidsafe.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "id")
public class Ethnic {
    @NonNull
    @SerializedName("_id")
    public final String id;
    @SerializedName("name")
    public final String name;

    public Ethnic(@NonNull String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

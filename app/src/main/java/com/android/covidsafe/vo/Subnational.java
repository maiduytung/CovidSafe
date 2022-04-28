package com.android.covidsafe.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(primaryKeys = "id")
public class Subnational {
    @SerializedName("parent_id")
    public final String parentId;
    @NonNull
    @SerializedName("_id")
    public final String id;
    @SerializedName("name")
    public final String name;
    @SerializedName("type")
    public final String type;

    public Subnational(String parentId, @NonNull String id, String name, String type) {
        this.parentId = parentId;
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }
}

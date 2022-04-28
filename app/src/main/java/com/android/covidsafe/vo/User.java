package com.android.covidsafe.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

@Entity(primaryKeys = "id")
public class User {

    @NonNull
    @SerializedName("id")
    public final String id;

    @SerializedName("username")
    public final String username;

    @SerializedName("roles")
    public final ArrayList<String> roles;

    @SerializedName("active")
    public final boolean active;

    @SerializedName("last_modified_date")
    public final Date lastModifiedDate;

    public User(@NonNull String id, String username, ArrayList<String> roles, boolean active, Date lastModifiedDate) {
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.active = active;
        this.lastModifiedDate = lastModifiedDate;
    }
}

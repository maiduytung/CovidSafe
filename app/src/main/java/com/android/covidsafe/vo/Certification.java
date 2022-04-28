package com.android.covidsafe.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(primaryKeys = "identification")
public class Certification {
    @NonNull
    @SerializedName("identification")
    public final String identification;

    @SerializedName("count_the_dose")
    public final int countTheDose;

    @SerializedName("last_modified_date")
    public final Date lastModifiedDate;

    public Certification(@NonNull String identification, int countTheDose, Date lastModifiedDate) {
        this.identification = identification;
        this.countTheDose = countTheDose;
        this.lastModifiedDate = lastModifiedDate;
    }
}

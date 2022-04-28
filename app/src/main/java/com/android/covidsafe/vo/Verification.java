package com.android.covidsafe.vo;

import com.google.gson.annotations.SerializedName;

public class Verification {
    @SerializedName("verification_key")
    public final String verificationKey;

    @SerializedName("expires_in")
    public final int expiresIn;

    public Verification(String verificationKey, int expiresIn) {
        this.verificationKey = verificationKey;
        this.expiresIn = expiresIn;
    }
}

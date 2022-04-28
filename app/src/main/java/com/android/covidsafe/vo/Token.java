package com.android.covidsafe.vo;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("token_type")
    public final String tokenType;

    @SerializedName("access_token")
    public final String accessToken;

    @SerializedName("expires_in")
    public final int expiresIn;

    @SerializedName("refresh_token")
    public final String refreshToken;

    public Token(String tokenType, String accessToken, int expiresIn, String refreshToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
    }
}

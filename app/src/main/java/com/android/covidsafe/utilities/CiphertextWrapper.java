package com.android.covidsafe.utilities;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class CiphertextWrapper {
    @NotNull
    private final byte[] ciphertext;
    @NotNull
    private final byte[] initializationVector;

    @NotNull
    public final byte[] getCiphertext() {
        return this.ciphertext;
    }

    @NotNull
    public final byte[] getInitializationVector() {
        return this.initializationVector;
    }

    public CiphertextWrapper(@NotNull byte[] ciphertext, @NotNull byte[] initializationVector) {
        super();
        this.ciphertext = ciphertext;
        this.initializationVector = initializationVector;
    }


    @NotNull
    public final CiphertextWrapper copy(@NotNull byte[] ciphertext, @NotNull byte[] initializationVector) {
        return new CiphertextWrapper(ciphertext, initializationVector);
    }

    @NotNull
    public String toString() {
        return "CiphertextWrapper(ciphertext=" + Arrays.toString(this.ciphertext) + ", initializationVector=" + Arrays.toString(this.initializationVector) + ")";
    }

    public int hashCode() {
        return (ciphertext != null ? Arrays.hashCode(ciphertext) : 0) * 31 + (initializationVector != null ? Arrays.hashCode(initializationVector) : 0);
    }


    public boolean equals(@Nullable Object obj) {
        return (this == obj);
    }
}

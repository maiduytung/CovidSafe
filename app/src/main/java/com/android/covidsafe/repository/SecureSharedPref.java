package com.android.covidsafe.repository;

public interface SecureSharedPref {
    String getString(String key);

    int getInt(String key);

    Boolean getBoolean(String key);

    void putString(String key, String value);

    void putInt(String key, int value);

    void putBoolean(String key, Boolean value);

    void delete();
}


package com.android.covidsafe.repository;

public interface ISharedPreferences {
    String getString(String key);

    int getInt(String key);

    void putString(String key, String value);

    void putInt(String key, int value);
}

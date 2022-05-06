package com.android.covidsafe.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.covidsafe.vo.Profile;

/**
 * Interface for database access for Profile related operations.
 */
@Dao
public interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Profile profile);

    @Query("SELECT * FROM Profile")
    LiveData<Profile> loadProfile();
}

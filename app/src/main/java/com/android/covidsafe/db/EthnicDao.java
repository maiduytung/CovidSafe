package com.android.covidsafe.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.covidsafe.vo.Ethnic;

import java.util.List;

/**
 * Interface for database access for Ethnic related operations.
 */
@Dao
public interface EthnicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ethnic ethnic);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Ethnic> ethnicList);

    @Query("SELECT * FROM Ethnic")
    LiveData<List<Ethnic>> getAll();

    @Query("SELECT name FROM Ethnic WHERE id = :id")
    LiveData<String> getNameById(String id);
}

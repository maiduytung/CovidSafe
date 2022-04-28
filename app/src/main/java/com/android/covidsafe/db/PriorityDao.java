package com.android.covidsafe.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.covidsafe.vo.Priority;

import java.util.List;

/**
 * Interface for database access for Priority related operations.
 */
@Dao
public interface PriorityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Priority priority);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Priority> priorityList);

    @Query("SELECT * FROM priority")
    LiveData<List<Priority>> getAll();

    @Query("SELECT name FROM priority WHERE id = :id")
    LiveData<String> getNameById(String id);
}

package com.android.covidsafe.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.covidsafe.vo.Nationality;

import java.util.List;

/**
 * Interface for database access for Nationality related operations.
 */
@Dao
public interface NationalityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Nationality Nationality);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Nationality> nationalityList);

    @Query("SELECT * FROM nationality")
    LiveData<List<Nationality>> getAll();

    @Query("SELECT name FROM nationality WHERE id = :id")
    LiveData<String> getNameById(String id);

}

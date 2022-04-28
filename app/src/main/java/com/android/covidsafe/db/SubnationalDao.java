package com.android.covidsafe.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.covidsafe.vo.Subnational;

import java.util.List;

/**
 * Interface for database access for Subnational related operations.
 */
@Dao
public interface SubnationalDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Subnational subnational);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Subnational> subnationalList);

    @Query("SELECT * FROM subnational WHERE type = :type AND parentId = :parentId")
    LiveData<List<Subnational>> getByTypeAndParentId(String type, String parentId);

    @Query("SELECT * FROM subnational WHERE type = :type")
    LiveData<List<Subnational>> getByType(String type);

    @Query("SELECT name FROM subnational WHERE id = :id")
    LiveData<String> getNameById(String id);

    @Query("SELECT * FROM subnational WHERE id = :type")
    LiveData<List<Subnational>> getParentByChildName(String type);

}

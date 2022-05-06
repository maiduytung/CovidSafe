package com.android.covidsafe.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.covidsafe.vo.Certification;

/**
 * Interface for database access for Certification related operations.
 */
@Dao
public interface CertificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Certification certification);

    @Query("SELECT * FROM Certification")
    LiveData<Certification> loadCertification();
}

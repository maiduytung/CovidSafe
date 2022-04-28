package com.android.covidsafe.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.covidsafe.vo.HealthDeclaration;
import com.android.covidsafe.vo.HealthDeclarationResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Interface for database access on HealthDeclaration related operations.
 */
@Dao
public abstract class HealthDeclarationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(HealthDeclaration... healthDeclarations);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertHealthDeclarations(List<HealthDeclaration> healthDeclarations);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long createHealthDeclarationIfNotExists(HealthDeclaration healthDeclaration);

    @Query("SELECT * FROM HealthDeclaration WHERE id = :id")
    public abstract LiveData<HealthDeclaration> load(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(HealthDeclarationResult healthDeclarationResult);

    @Query("SELECT * FROM HealthDeclarationResult WHERE query = :query")
    public abstract LiveData<HealthDeclarationResult> loadResult(String query);

    public LiveData<List<HealthDeclaration>> loadOrdered(List<String> healthDeclarationIds) {
        HashMap<String, Integer> order = new HashMap<>();
//        SparseIntArray order = new SparseIntArray();
        int index = 0;
        for (String healthDeclarationId : healthDeclarationIds) {
            order.put(healthDeclarationId, index++);
        }
        return Transformations.map(loadById(healthDeclarationIds), healthDeclarationList -> {
            Collections.sort(healthDeclarationList, (r1, r2) -> {
                int pos1 = order.get(r1.id);
                int pos2 = order.get(r2.id);
                return pos1 - pos2;
            });
            return healthDeclarationList;
        });
    }

    @Query("SELECT * FROM HealthDeclaration WHERE id in (:healthDeclarationIds)")
    protected abstract LiveData<List<HealthDeclaration>> loadById(List<String> healthDeclarationIds);

    @Query("SELECT * FROM HealthDeclarationResult WHERE query = :query")
    public abstract HealthDeclarationResult findHealthDeclarationResult(String query);
}

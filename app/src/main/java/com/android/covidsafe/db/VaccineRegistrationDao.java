package com.android.covidsafe.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.covidsafe.vo.VaccineRegistration;
import com.android.covidsafe.vo.VaccineRegistrationResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Interface for database access on VaccineRegistration related operations.
 */
@Dao
public abstract class VaccineRegistrationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(VaccineRegistration... vaccineRegistrations);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertVaccineRegistrations(List<VaccineRegistration> vaccineRegistrations);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long createVaccineRegistrationIfNotExists(VaccineRegistration vaccineRegistration);

    @Query("SELECT * FROM VaccineRegistration WHERE id = :id")
    public abstract LiveData<VaccineRegistration> load(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(VaccineRegistrationResult vaccineRegistrationResult);

    @Query("SELECT * FROM VaccineRegistrationResult WHERE query = :query")
    public abstract LiveData<VaccineRegistrationResult> loadResult(String query);

    public LiveData<List<VaccineRegistration>> loadOrdered(List<String> vaccineRegistrationIds) {
        HashMap<String, Integer> order = new HashMap<>();
//        SparseIntArray order = new SparseIntArray();
        int index = 0;
        for (String vaccineRegistrationId : vaccineRegistrationIds) {
            order.put(vaccineRegistrationId, index++);
        }
        return Transformations.map(loadById(vaccineRegistrationIds), vaccineRegistrationList -> {
            Collections.sort(vaccineRegistrationList, (r1, r2) -> {
                int pos1 = order.get(r1.id);
                int pos2 = order.get(r2.id);
                return pos1 - pos2;
            });
            return vaccineRegistrationList;
        });
    }

    @Query("SELECT * FROM VaccineRegistration WHERE id in (:vaccineRegistrationIds)")
    protected abstract LiveData<List<VaccineRegistration>> loadById(List<String> vaccineRegistrationIds);

    @Query("SELECT * FROM VaccineRegistrationResult WHERE query = :query")
    public abstract VaccineRegistrationResult findVaccineRegistrationResult(String query);
}

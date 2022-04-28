package com.android.covidsafe.db;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.android.covidsafe.vo.Vaccination;
import com.android.covidsafe.vo.VaccinationResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Interface for database access on Vaccination related operations.
 */
@Dao
public abstract class VaccinationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(Vaccination... vaccinations);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertVaccinations(List<Vaccination> vaccinations);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public abstract long createVaccinationIfNotExists(Vaccination vaccination);

    @Query("SELECT * FROM Vaccination WHERE id = :id")
    public abstract LiveData<Vaccination> load(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(VaccinationResult vaccinationResult);

    @Query("SELECT * FROM VaccinationResult WHERE query = :query")
    public abstract LiveData<VaccinationResult> loadResult(String query);

    public LiveData<List<Vaccination>> loadOrdered(List<String> vaccinationIds) {
        HashMap<String, Integer> order = new HashMap<>();
//        SparseIntArray order = new SparseIntArray();
        int index = 0;
        for (String vaccinationId : vaccinationIds) {
            order.put(vaccinationId, index++);
        }
        return Transformations.map(loadById(vaccinationIds), vaccinationList -> {
            Collections.sort(vaccinationList, (r1, r2) -> {
                int pos1 = order.get(r1.id);
                int pos2 = order.get(r2.id);
                return pos1 - pos2;
            });
            return vaccinationList;
        });
    }

    @Query("SELECT * FROM Vaccination WHERE id in (:vaccinationIds)")
    protected abstract LiveData<List<Vaccination>> loadById(List<String> vaccinationIds);

    @Query("SELECT * FROM VaccinationResult WHERE query = :query")
    public abstract VaccinationResult findVaccinationResult(String query);
}

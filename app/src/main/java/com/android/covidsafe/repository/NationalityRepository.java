package com.android.covidsafe.repository;

import androidx.lifecycle.LiveData;

import com.android.covidsafe.db.NationalityDao;
import com.android.covidsafe.vo.Nationality;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles Nationality objects.
 */
@Singleton
public class NationalityRepository {
    private final NationalityDao nationalityDao;

    @Inject
    NationalityRepository(NationalityDao nationalityDao) {
        this.nationalityDao = nationalityDao;
    }

    public LiveData<List<Nationality>> getNationalityList() {
        return nationalityDao.getAll();
    }

    public LiveData<String> getName(String id) {
        return nationalityDao.getNameById(id);
    }
}

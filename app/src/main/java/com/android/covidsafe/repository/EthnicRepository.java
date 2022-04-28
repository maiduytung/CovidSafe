package com.android.covidsafe.repository;

import androidx.lifecycle.LiveData;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.db.AppDatabase;
import com.android.covidsafe.db.EthnicDao;
import com.android.covidsafe.vo.Ethnic;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles Ethnic objects.
 */
@Singleton
public class EthnicRepository {
    private final AppDatabase db;
    private final EthnicDao ethnicDao;
    private final AppExecutors appExecutors;

    @Inject
    EthnicRepository(AppExecutors appExecutors, AppDatabase db, EthnicDao ethnicDao) {
        this.appExecutors = appExecutors;
        this.db = db;
        this.ethnicDao = ethnicDao;
    }

    public LiveData<List<Ethnic>> getEthnicList() {
        return ethnicDao.getAll();
    }

    public LiveData<String> getName(String id) {
        return ethnicDao.getNameById(id);
    }

}

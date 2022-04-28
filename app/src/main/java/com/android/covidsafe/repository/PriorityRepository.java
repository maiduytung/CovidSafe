package com.android.covidsafe.repository;

import androidx.lifecycle.LiveData;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.db.AppDatabase;
import com.android.covidsafe.db.PriorityDao;
import com.android.covidsafe.vo.Priority;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles Priority objects.
 */
@Singleton
public class PriorityRepository {
    private final AppDatabase db;
    private final PriorityDao priorityDao;
    private final AppExecutors appExecutors;

    @Inject
    PriorityRepository(AppExecutors appExecutors, AppDatabase db, PriorityDao priorityDao) {
        this.appExecutors = appExecutors;
        this.db = db;
        this.priorityDao = priorityDao;
    }

    public LiveData<List<Priority>> getPriorityList() {
        return priorityDao.getAll();
    }

    public LiveData<String> getName(String id) {
        return priorityDao.getNameById(id);
    }

}

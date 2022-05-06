package com.android.covidsafe.repository;

import androidx.lifecycle.LiveData;

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
    private final PriorityDao priorityDao;

    @Inject
    PriorityRepository(PriorityDao priorityDao) {
        this.priorityDao = priorityDao;
    }

    public LiveData<List<Priority>> getPriorityList() {
        return priorityDao.getAll();
    }

    public LiveData<String> getName(String id) {
        return priorityDao.getNameById(id);
    }

}

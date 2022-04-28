package com.android.covidsafe.repository;

import static com.android.covidsafe.utilities.Constants.SUBNATIONAL_TYPE_DISTRICT;
import static com.android.covidsafe.utilities.Constants.SUBNATIONAL_TYPE_PROVINCE;
import static com.android.covidsafe.utilities.Constants.SUBNATIONAL_TYPE_WARD;

import androidx.lifecycle.LiveData;

import com.android.covidsafe.AppExecutors;
import com.android.covidsafe.db.AppDatabase;
import com.android.covidsafe.db.SubnationalDao;
import com.android.covidsafe.vo.Subnational;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles Subnational objects.
 */
@Singleton
public class SubnationalRepository {
    private final AppDatabase db;
    private final SubnationalDao subnationalDao;
    private final AppExecutors appExecutors;

    @Inject
    SubnationalRepository(AppExecutors appExecutors, AppDatabase db, SubnationalDao subnationalDao) {
        this.appExecutors = appExecutors;
        this.db = db;
        this.subnationalDao = subnationalDao;
    }

    public LiveData<List<Subnational>> getProvinceList() {
        return subnationalDao.getByType(SUBNATIONAL_TYPE_PROVINCE);
    }

    public LiveData<String> getName(String id) {
        return subnationalDao.getNameById(id);
    }

    public LiveData<List<Subnational>> getDistrictList(String parentId) {
        return subnationalDao.getByTypeAndParentId(SUBNATIONAL_TYPE_DISTRICT, parentId);
    }

    public LiveData<List<Subnational>> getWardList(String parentId) {
        return subnationalDao.getByTypeAndParentId(SUBNATIONAL_TYPE_WARD, parentId);
    }

}

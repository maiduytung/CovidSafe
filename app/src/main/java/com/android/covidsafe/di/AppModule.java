package com.android.covidsafe.di;

import android.app.Application;

import com.android.covidsafe.db.AppDatabase;
import com.android.covidsafe.db.CertificationDao;
import com.android.covidsafe.db.EthnicDao;
import com.android.covidsafe.db.HealthDeclarationDao;
import com.android.covidsafe.db.NationalityDao;
import com.android.covidsafe.db.PriorityDao;
import com.android.covidsafe.db.ProfileDao;
import com.android.covidsafe.db.RepoDao;
import com.android.covidsafe.db.ReportDao;
import com.android.covidsafe.db.SubnationalDao;
import com.android.covidsafe.db.UserDao;
import com.android.covidsafe.db.VaccinationDao;
import com.android.covidsafe.db.VaccineRegistrationDao;
import com.android.covidsafe.repository.ISharedPreferences;
import com.android.covidsafe.repository.SecureSharedPref;
import com.android.covidsafe.utilities.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = {
        NetworkModule.class,
        DatabaseModule.class
})
class AppModule {

    @Singleton
    @Provides
    ISharedPreferences provideISharedPreferences(Application application) {
        return new SecureSharedPref(application, Constants.TOKEN);
    }

    @Singleton
    @Provides
    AppDatabase provideAppDatabase(Application application) {
        return AppDatabase.getInstance(application);
    }

    @Singleton
    @Provides
    UserDao provideUserDao(AppDatabase appDatabase) {
        return appDatabase.userDao();
    }

    @Singleton
    @Provides
    ProfileDao provideProfileDao(AppDatabase appDatabase) {
        return appDatabase.profileDao();
    }

    @Singleton
    @Provides
    RepoDao provideRepoDao(AppDatabase appDatabase) {
        return appDatabase.repoDao();
    }

    @Singleton
    @Provides
    HealthDeclarationDao provideHealthDeclarationDao(AppDatabase appDatabase) {
        return appDatabase.healthDeclarationDao();
    }

    @Singleton
    @Provides
    VaccineRegistrationDao provideVaccineRegistrationDao(AppDatabase appDatabase) {
        return appDatabase.vaccineRegistrationDao();
    }

    @Singleton
    @Provides
    ReportDao provideReportDao(AppDatabase appDatabase) {
        return appDatabase.reportDao();
    }

    @Singleton
    @Provides
    VaccinationDao provideVaccinationDao(AppDatabase appDatabase) {
        return appDatabase.vaccinationDao();
    }

    @Singleton
    @Provides
    CertificationDao provideCertificationDao(AppDatabase appDatabase) {
        return appDatabase.certificationDao();
    }

    @Singleton
    @Provides
    SubnationalDao provideSubnationalDao(AppDatabase appDatabase) {
        return appDatabase.subnationalDao();
    }

    @Singleton
    @Provides
    NationalityDao provideNationalDao(AppDatabase appDatabase) {
        return appDatabase.nationalityDao();
    }

    @Singleton
    @Provides
    EthnicDao provideEthnicDao(AppDatabase appDatabase) {
        return appDatabase.ethnicDao();
    }

    @Singleton
    @Provides
    PriorityDao providePriorityDao(AppDatabase appDatabase) {
        return appDatabase.priorityDao();
    }

}

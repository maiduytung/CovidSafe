package com.android.covidsafe.di;

import android.app.Application;

import com.android.covidsafe.db.AppDatabase;
import com.android.covidsafe.db.CertificationDao;
import com.android.covidsafe.db.EthnicDao;
import com.android.covidsafe.db.HealthDeclarationDao;
import com.android.covidsafe.db.NationalityDao;
import com.android.covidsafe.db.PriorityDao;
import com.android.covidsafe.db.ProfileDao;
import com.android.covidsafe.db.ReportDao;
import com.android.covidsafe.db.SecureDatabase;
import com.android.covidsafe.db.SubnationalDao;
import com.android.covidsafe.db.UserDao;
import com.android.covidsafe.db.VaccinationDao;
import com.android.covidsafe.db.VaccineRegistrationDao;
import com.android.covidsafe.repository.SecureSharedPref;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Singleton
    @Provides
    SecureDatabase provideSecureDatabase(Application application, SecureSharedPref secureSharedPref) {
        return SecureDatabase.getInstance(application, secureSharedPref);
    }

    @Singleton
    @Provides
    AppDatabase provideAppDatabase(Application application) {
        return AppDatabase.getInstance(application);
    }

    @Singleton
    @Provides
    UserDao provideUserDao(SecureDatabase secureDatabase) {
        return secureDatabase.userDao();
    }

    @Singleton
    @Provides
    ProfileDao provideProfileDao(SecureDatabase secureDatabase) {
        return secureDatabase.profileDao();
    }

    @Singleton
    @Provides
    HealthDeclarationDao provideHealthDeclarationDao(SecureDatabase secureDatabase) {
        return secureDatabase.healthDeclarationDao();
    }

    @Singleton
    @Provides
    VaccineRegistrationDao provideVaccineRegistrationDao(SecureDatabase secureDatabase) {
        return secureDatabase.vaccineRegistrationDao();
    }

    @Singleton
    @Provides
    ReportDao provideReportDao(SecureDatabase secureDatabase) {
        return secureDatabase.reportDao();
    }

    @Singleton
    @Provides
    VaccinationDao provideVaccinationDao(SecureDatabase secureDatabase) {
        return secureDatabase.vaccinationDao();
    }

    @Singleton
    @Provides
    CertificationDao provideCertificationDao(SecureDatabase secureDatabase) {
        return secureDatabase.certificationDao();
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

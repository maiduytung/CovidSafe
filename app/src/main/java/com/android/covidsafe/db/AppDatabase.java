package com.android.covidsafe.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.android.covidsafe.vo.Certification;
import com.android.covidsafe.vo.Contributor;
import com.android.covidsafe.vo.Ethnic;
import com.android.covidsafe.vo.HealthDeclaration;
import com.android.covidsafe.vo.HealthDeclarationResult;
import com.android.covidsafe.vo.Nationality;
import com.android.covidsafe.vo.Priority;
import com.android.covidsafe.vo.Profile;
import com.android.covidsafe.vo.Repo;
import com.android.covidsafe.vo.RepoSearchResult;
import com.android.covidsafe.vo.Report;
import com.android.covidsafe.vo.ReportResult;
import com.android.covidsafe.vo.Subnational;
import com.android.covidsafe.vo.User;
import com.android.covidsafe.vo.Vaccination;
import com.android.covidsafe.vo.VaccinationResult;
import com.android.covidsafe.vo.VaccineRegistration;
import com.android.covidsafe.vo.VaccineRegistrationResult;
import com.android.covidsafe.workers.DatabaseWorker;

/**
 * Main database description.
 */
@Database(entities = {User.class, Profile.class, Certification.class, Repo.class, Contributor.class, RepoSearchResult.class, HealthDeclarationResult.class, HealthDeclaration.class,
        VaccineRegistrationResult.class, VaccineRegistration.class, ReportResult.class, Report.class, VaccinationResult.class, Vaccination.class,
        Subnational.class, Nationality.class, Ethnic.class, Priority.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    @VisibleForTesting
    public static final String DATABASE_NAME = "encrypted-db";

    private static volatile AppDatabase instance = null;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase(context);
                }
            }
        }
        return instance;
    }

    private static AppDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(DatabaseWorker.class)
                                .build();
                        WorkManager.getInstance(context).enqueue(request);
                    }
                }).build();
    }

    abstract public UserDao userDao();

    abstract public ProfileDao profileDao();

    abstract public CertificationDao certificationDao();

    abstract public RepoDao repoDao();

    abstract public HealthDeclarationDao healthDeclarationDao();

    abstract public VaccineRegistrationDao vaccineRegistrationDao();

    abstract public ReportDao reportDao();

    abstract public VaccinationDao vaccinationDao();

    abstract public SubnationalDao subnationalDao();

    abstract public NationalityDao nationalityDao();

    abstract public EthnicDao ethnicDao();

    abstract public PriorityDao priorityDao();
}

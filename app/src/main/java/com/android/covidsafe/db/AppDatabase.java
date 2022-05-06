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

import com.android.covidsafe.vo.Ethnic;
import com.android.covidsafe.vo.Nationality;
import com.android.covidsafe.vo.Priority;
import com.android.covidsafe.vo.Subnational;
import com.android.covidsafe.workers.DatabaseWorker;

/**
 * Base database description.
 */
@Database(entities = {Subnational.class, Nationality.class, Ethnic.class, Priority.class}, version = 3)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    @VisibleForTesting
    public static final String DATABASE_NAME = "covidsafe";

    private static volatile AppDatabase covidsafeDB = null;

    public static AppDatabase getInstance(Context context) {
        if (covidsafeDB == null) {
            synchronized (AppDatabase.class) {
                if (covidsafeDB == null) {
                    covidsafeDB = buildDatabase(context);
                }
            }
        }
        return covidsafeDB;
    }

    private static AppDatabase buildDatabase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);

                        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(DatabaseWorker.class)
                                .build();
                        WorkManager.getInstance(context).enqueue(request);
                    }
                }).build();
    }

    abstract public SubnationalDao subnationalDao();

    abstract public NationalityDao nationalityDao();

    abstract public EthnicDao ethnicDao();

    abstract public PriorityDao priorityDao();
}

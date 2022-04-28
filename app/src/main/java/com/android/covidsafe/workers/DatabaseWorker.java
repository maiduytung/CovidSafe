package com.android.covidsafe.workers;

import static com.android.covidsafe.utilities.Constants.ETHNIC_DATA_FILENAME;
import static com.android.covidsafe.utilities.Constants.NATIONALITY_DATA_FILENAME;
import static com.android.covidsafe.utilities.Constants.PRIORITY_DATA_FILENAME;
import static com.android.covidsafe.utilities.Constants.SUBNATIONAL_DATA_FILENAME;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.covidsafe.db.AppDatabase;
import com.android.covidsafe.vo.Ethnic;
import com.android.covidsafe.vo.Nationality;
import com.android.covidsafe.vo.Priority;
import com.android.covidsafe.vo.Subnational;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public final class DatabaseWorker extends Worker {
    private static final String TAG = "DatabaseWork";

    public DatabaseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            //subnational
            InputStream inputStreamSubnational = getApplicationContext().getAssets().open(SUBNATIONAL_DATA_FILENAME);
            JsonReader jsonReaderSubnational = new JsonReader(new InputStreamReader(inputStreamSubnational, "UTF-8"));

            Type subType = new TypeToken<List<Subnational>>() {
            }.getType();

            List<Subnational> subnationalList = new Gson().fromJson(jsonReaderSubnational, subType);

            //nationality
            InputStream inputStreamNationality = getApplicationContext().getAssets().open(NATIONALITY_DATA_FILENAME);
            JsonReader jsonReaderNationality = new JsonReader(new InputStreamReader(inputStreamNationality, "UTF-8"));

            Type nationalityType = new TypeToken<List<Nationality>>() {
            }.getType();

            List<Nationality> nationalityList = new Gson().fromJson(jsonReaderNationality, nationalityType);

            //ethnic
            InputStream inputStreamEthnic = getApplicationContext().getAssets().open(ETHNIC_DATA_FILENAME);
            JsonReader jsonReaderEthnic = new JsonReader(new InputStreamReader(inputStreamEthnic, "UTF-8"));

            Type ethnicType = new TypeToken<List<Ethnic>>() {
            }.getType();

            List<Ethnic> ethnicList = new Gson().fromJson(jsonReaderEthnic, ethnicType);

            //priority
            InputStream inputStreamPriority = getApplicationContext().getAssets().open(PRIORITY_DATA_FILENAME);
            JsonReader jsonReaderPriority = new JsonReader(new InputStreamReader(inputStreamPriority, "UTF-8"));

            Type priorityType = new TypeToken<List<Priority>>() {
            }.getType();

            List<Priority> priorityList = new Gson().fromJson(jsonReaderPriority, priorityType);

            //insert database
            AppDatabase database = AppDatabase.getInstance(getApplicationContext());
            database.subnationalDao().insertAll(subnationalList);
            database.nationalityDao().insertAll(nationalityList);
            database.ethnicDao().insertAll(ethnicList);
            database.priorityDao().insertAll(priorityList);

            return Result.success();
        } catch (Exception ex) {
            Log.e(TAG, "Error seeding database", ex);
            return Result.failure();
        }
    }
}

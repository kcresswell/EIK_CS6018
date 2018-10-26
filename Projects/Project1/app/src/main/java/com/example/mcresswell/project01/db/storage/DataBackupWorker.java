package com.example.mcresswell.project01.db.storage;

import android.content.Context;
import android.support.annotation.NonNull;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class DataBackupWorker extends Worker {

    private static final String LOG_TAG = DataBackupWorker.class.getSimpleName();
    private static final long DATA_BACKUP_INTERVAL = 1_800_000L; //Backup every 30 minutes
    private static Timestamp lastBackup = null;

    public DataBackupWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters) {
        super(context, workerParameters);

        PeriodicWorkRequest.Builder dataBackupBuilder =
                new PeriodicWorkRequest.Builder(DataBackupWorker.class, DATA_BACKUP_INTERVAL,
                        TimeUnit.MILLISECONDS);

        PeriodicWorkRequest dbBackupRequest = dataBackupBuilder.build();

        WorkManager.getInstance().enqueue(dbBackupRequest);
    }

    @NonNull
    @Override
    public Result doWork() {
//
//        Context applicationContext = getApplicationContext();
//        return WorkerResult.SUCCESS;
        return null;
    }
}

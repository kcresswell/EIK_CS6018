package com.example.mcresswell.project01.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.mcresswell.project01.db.dao.FitnessProfileDao;
import com.example.mcresswell.project01.db.dao.UserDao;
import com.example.mcresswell.project01.db.dao.WeatherDao;
import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.db.entity.Weather;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.util.DataTypeConverters;

import java.util.List;

@Database(entities = {User.class, FitnessProfile.class, Weather.class}, version = 1)
@TypeConverters({DataTypeConverters.class})
public abstract class InStyleDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "instyle_database";

    //Abstract getters for each DAO
    public abstract UserDao userDao();
    public abstract FitnessProfileDao fitnessProfileDao();
    public abstract WeatherDao weatherDao();

    //DATABASE IS A SINGLETON
    private static volatile InStyleDatabase INSTANCE;

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();


    public static InStyleDatabase getDatabaseInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (InStyleDatabase.class) {
                if (INSTANCE == null) {
                    //Create single instance of database with FitnessProfile, User, and Weather tables
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            InStyleDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }

//    private static InStyleDatabase buildDatabase() {
//
//    }

    /**
     * If instance of database already exists, update mIsDatabaseCreated flag.
     * @param context
     */
    private void updateIsDatabaseCreatedFlag(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
            //Post task to main thread to set mIsDatabaseCreated flag
            mIsDatabaseCreated.postValue(true);
        }
    }

    /**
     * Populates database with test/dummy data.
     * @param database
     * @param userList List of test user accounts.
     * @param fitnessProfileList List of test fitness profile records.
     * @param weatherList List of test weather forecast data records.
     */
    private static void insertTestData(final InStyleDatabase database,
                                       final List<User> userList,
                                       final List<FitnessProfile> fitnessProfileList,
                                       final List<Weather> weatherList) {
        database.runInTransaction(() -> {
            database.userDao().insertAll(userList);
            database.weatherDao().insertAll(weatherList);
//            database.fitnessProfileDao().insertAll(fitnessProfileList);
        });
    }

    /**
     * Helper method to check whether an instance of the database already exists.
     * @return
     */
    private LiveData<Boolean> isDatabaseAlreadyCreated() {
        return mIsDatabaseCreated;
    }

}

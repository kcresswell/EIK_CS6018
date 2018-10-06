package com.example.mcresswell.project01.db.repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.mcresswell.project01.db.InStyleDatabase;
import com.example.mcresswell.project01.db.dao.WeatherDao;
import com.example.mcresswell.project01.db.entity.Weather;

import java.util.List;

public class WeatherRepository {
    private WeatherDao mWeatherDao;
    private LiveData<List<Weather>> mAllWeather; // All weather forecast records in Weather table

    public WeatherRepository(Application application) {
        InStyleDatabase db = InStyleDatabase.getDatabaseInstance(application);
        mWeatherDao = db.weatherDao();
        mAllWeather = getAllWeather();
    }

    public LiveData<List<Weather>> getAllWeather() {
        return mAllWeather;
    }

    public void insert (Weather weather) {
        new insertAsyncTask(mWeatherDao).execute(weather);
    }

    private static class insertAsyncTask extends AsyncTask<Weather, Void, Void> {

        private WeatherDao mAsyncTaskDao;

        insertAsyncTask(WeatherDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Weather... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}

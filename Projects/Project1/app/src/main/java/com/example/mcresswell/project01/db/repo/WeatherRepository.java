package com.example.mcresswell.project01.db.repo;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;

import com.example.mcresswell.project01.db.InStyleDatabase;
import com.example.mcresswell.project01.db.dao.WeatherDao;
import com.example.mcresswell.project01.db.entity.Weather;

import java.util.List;

/**
 * Repository/model class for the Weather entity that handles all business logic associated
 * handling Weather data.
 * This class interfaces with the in-memory database, and handles API calls to the
 * OpenWeatherMap API to retrieve live weather data. Data that is retrieved and/or modified is
 * passed to the WeatherViewModel class.
 */

//TODO: MOVE EXISTING BUSINESS LOGIC FROM WEATHERVIEWMODEL TO WEATHERREPOSITORY
public class WeatherRepository {
    private WeatherDao mWeatherDao;
    private MutableLiveData<Weather> mWeather = new MutableLiveData<>();
    private LiveData<List<Weather>> mAllWeather; // All weather forecast records in Weather table


    public WeatherRepository(Application application) {
        InStyleDatabase db = InStyleDatabase.getDatabaseInstance(application);
        mWeatherDao = db.weatherDao();
//        mAllWeather = getAllWeather();
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

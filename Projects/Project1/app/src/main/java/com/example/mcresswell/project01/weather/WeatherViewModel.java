package com.example.mcresswell.project01.weather;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mcresswell.project01.db.entity.Weather;
import com.example.mcresswell.project01.db.repo.WeatherRepository;
import com.example.mcresswell.project01.util.WeatherUtils;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class WeatherViewModel extends AndroidViewModel {

    private static final String LOG = WeatherViewModel.class.getSimpleName();

    //OpenWeatherMap API member variables
    private final MutableLiveData<WeatherForecast> mforecastData =
            new MutableLiveData<>(); //Observable LiveData for WeatherForecast from API calls
    private String mLocationCity;
    private String mLocationCountry;

//    //Database
//    private WeatherRepository mWeatherRepository;
//    private LiveData<List<Weather>> mAllWeather;
//
    public WeatherViewModel (Application application) {
        super(application);
//        mWeatherRepository = new WeatherRepository(application);
//        mAllWeather = getAllWeather();
    }
//
//    LiveData<List<Weather>> getAllWeather() {
//        return mAllWeather;
//    }

    @SuppressLint("StaticFieldLeak")
    private void loadData(){
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... strings) {
                URL weatherDataURL = null;
                String retrievedJsonData = null;
                if (strings != null) {
                    String city = strings[0];
                    String country = strings[1];
                    Log.d(LOG, city + ", " + country);
                    weatherDataURL = WeatherUtils.buildWeatherApiUrl(city, country);
                }
                try {
                    retrievedJsonData = WeatherUtils.getJsonDataFromUrl(weatherDataURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return retrievedJsonData;

            }

            @Override
            protected void onPostExecute(String s) {
                    mforecastData.setValue(WeatherUtils.getWeatherForecast(s));

            }
        }.execute(mLocationCity, mLocationCountry);
    }

    public void setLocation(String city, String country) {
        mLocationCity = city;
        mLocationCountry = country;
        loadData();
    }


    public MutableLiveData<WeatherForecast> getForecastData() {
        return mforecastData;
    }
}

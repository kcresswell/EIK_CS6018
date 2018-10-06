package com.example.mcresswell.project01.weather;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mcresswell.project01.db.entity.Weather;
import com.example.mcresswell.project01.db.repo.WeatherRepository;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public class WeatherViewModel extends AndroidViewModel {

    private static final String LOG = WeatherViewModel.class.getSimpleName();

    //OpenWeatherMap API member variables
    private final MutableLiveData<WeatherForecast> mforecastData =
            new MutableLiveData<>(); //Observable LiveData for WeatherForecast from API calls
    private Map<String, String> mRecentWeatherLocations;
    private String mLocationCity;
    private String mLocationCountry;

    //Database
    private WeatherRepository mWeatherRepository;
    private LiveData<List<Weather>> mAllWeather;
    //
    public WeatherViewModel (Application application) {
        super(application);
        mWeatherRepository = new WeatherRepository(application);
        mAllWeather = mWeatherRepository.getAllWeather();
    }
//
//    LiveData<List<Weather>> getAllWeather() {
//        return mAllWeather;
//    }

    @SuppressLint("StaticFieldLeak")
    private void loadData(){
        new AsyncTask<String,Void,WeatherForecast>(){
            @Override
            protected WeatherForecast doInBackground(String... strings) {
                WeatherForecast weatherForecast = null;
                if (strings != null) {
                    String city = strings[0];
                    String country = strings[1];
                    Log.d(LOG, city + ", " + country);
                    try {
                        weatherForecast = WeatherClient.fetchCurrentWeather(city, country);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    if (weatherForecast != null) {
                        weatherForecast.printWeatherForecast();
                    }

                }
                return weatherForecast;
            }

                @Override
                protected void onPostExecute(WeatherForecast weather) {
                    mforecastData.setValue(weather);

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

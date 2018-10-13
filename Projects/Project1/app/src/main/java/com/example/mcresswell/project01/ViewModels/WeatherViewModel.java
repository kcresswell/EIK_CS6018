package com.example.mcresswell.project01.ViewModels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mcresswell.project01.db.InStyleDatabase;
import com.example.mcresswell.project01.db.entity.Weather;
import com.example.mcresswell.project01.db.repo.WeatherRepository;
import com.example.mcresswell.project01.weather.WeatherClient;
import com.example.mcresswell.project01.weather.WeatherForecast;

import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class WeatherViewModel extends AndroidViewModel {

    private static final String LOG = WeatherViewModel.class.getSimpleName();

    private final MutableLiveData<WeatherForecast> mforecastData =
            new MutableLiveData<>(); //Observable LiveData for WeatherForecast from API calls

    private Map<String, String> mRecentWeatherLocations;
    private String mLocationCity;
    private String mLocationCountry;

    @Inject
    WeatherRepository mWeatherRepository;
    private LiveData<List<Weather>> mAllWeather;

    public WeatherViewModel (@NonNull Application application) {
        super(application);
        InStyleDatabase database = InStyleDatabase.getDatabaseInstance(application);
        mWeatherRepository = WeatherRepository.getInstance(database);

        mAllWeather = mWeatherRepository.getAllWeather();
    }

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

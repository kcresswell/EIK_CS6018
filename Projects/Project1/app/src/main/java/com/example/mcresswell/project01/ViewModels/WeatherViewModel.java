package com.example.mcresswell.project01.ViewModels;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
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

public class WeatherViewModel extends AndroidViewModel {

    private static final String LOG = WeatherViewModel.class.getSimpleName();

    private final MediatorLiveData<Weather> m_observableWeather;
    private final WeatherRepository m_weatherRepository;

    /////////////////////////////////////////////////////////////////////////////////////////////////////
    private final MutableLiveData<Weather> mforecastData =
            new MutableLiveData<>(); //Observable LiveData for WeatherForecast from API calls
    private Map<String, String> mRecentWeatherLocations;
    private String mLocationCity;
    private String mLocationCountry;
    private LiveData<List<Weather>> mAllWeather;

/////////////////////////////////////////////////////////////////////////////////////////////////////

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        InStyleDatabase database = InStyleDatabase.getDatabaseInstance(application);
        m_weatherRepository = WeatherRepository.getInstance(database);

        m_observableWeather = new MediatorLiveData<>();

        configureMediatorLiveData();
    }

    private void configureMediatorLiveData() {
        m_observableWeather.setValue(null);

        LiveData<Weather> weatherData = m_weatherRepository.getWeather();

        m_observableWeather.addSource(weatherData, data -> {
            Log.d(LOG, "m_weatherRepository.getWeather() listener onChanged");
            if (data == null) {
                Log.d(LOG, "BUT WHY IS THE WEATHER NULL ?????? :(");
                return;
            }

            m_observableWeather.setValue(data);
        });
    }

    public void deleteAllWeather() {
        m_weatherRepository.deleteAll();
    }

    public void createWeather(Weather weather) {
        m_weatherRepository.insert(weather);
    }

    public void updateExpiredWeather(Weather weather) {
        m_weatherRepository.update(weather);
    }

    public LiveData<Weather> findWeather(String city, String country) {
        LiveData<Weather> weatherResult = m_weatherRepository.find(city, country);
        if (weatherResult.getValue() != null) {
            Log.d(LOG, String.format("Weather record for %s, %s found in repo", city, country));
        }
        return weatherResult;
    }

    public LiveData<Weather> getWeather() {
        return m_observableWeather;
    }
}
/////////////////////////////////////////////////////////////////////////////////////////////////////////

//    @SuppressLint("StaticFieldLeak")
//    private void loadData(){
//        new AsyncTask<String,Void,WeatherForecast>(){
//            @Override
//            protected WeatherForecast doInBackground(String... strings) {
//                WeatherForecast weatherForecast = null;
//                if (strings != null) {
//                    String city = strings[0];
//                    String country = strings[1];
//                    Log.d(LOG, city + ", " + country);
//                    try {
//                        weatherForecast = WeatherClient.fetchCurrentWeather(city, country);
//                    } catch (MalformedURLException e) {
//                        e.printStackTrace();
//                    }
//                    if (weatherForecast != null) {
//                        weatherForecast.printWeatherForecast();
//                    }
//
//                }
//                return weatherForecast;
//            }
//
//                @Override
//                protected void onPostExecute(WeatherForecast weather) {
//                    mforecastData.setValue(weather);
//
//                }
//            }.execute(mLocationCity, mLocationCountry);
//        }
//
//        public void setLocation(String city, String country) {
//            mLocationCity = city;
//            mLocationCountry = country;
//            loadData();
//        }
//
//
//        public MutableLiveData<WeatherForecast> getForecastData() {
//            return mforecastData;
//        }
//    }
/////////////////////////////////////////////////////////////////////////////////////////////////////////
package com.example.mcresswell.project01.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mcresswell.project01.db.InStyleDatabase;
import com.example.mcresswell.project01.db.entity.Weather;
import com.example.mcresswell.project01.db.repo.WeatherRepository;

import java.util.ArrayList;
import java.util.Random;

public class WeatherViewModel extends AndroidViewModel {

    private static final String LOG = WeatherViewModel.class.getSimpleName();

    private final MediatorLiveData<Weather> m_observableWeather;

    private final WeatherRepository m_weatherRepository;

    public WeatherViewModel(@NonNull Application application) {
        super(application);
        InStyleDatabase database = InStyleDatabase.getDatabaseInstance(application);
        m_weatherRepository = WeatherRepository.getInstance(database);

        m_observableWeather = new MediatorLiveData<>();

        configureMediatorLiveData();
    }

    private void configureMediatorLiveData() {
        m_observableWeather.setValue(null);
        m_observableWeather.addSource(m_weatherRepository.getWeather(), data -> {
            Log.d(LOG, "m_weatherRepository.getWeather() listener onChanged");
            if (data == null) {
                Log.d(LOG, "BUT WHY IS THE WEATHER NULL ?????? :(");
                return;
            }

            m_observableWeather.setValue(data);
        });
    }

    public void loadWeather(String city, String country) {
        m_weatherRepository.loadWeatherData(city, country);
    }


    public void loadRandomWeather(ArrayList<Integer> weatherIdList) {
        Random random = new Random();
        m_weatherRepository.
                loadRandomWeatherData(weatherIdList.get(random.nextInt(weatherIdList.size())));

    }

    public LiveData<Weather> getWeather() {
        return m_observableWeather;
    }

}
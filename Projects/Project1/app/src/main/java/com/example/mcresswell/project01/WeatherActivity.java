package com.example.mcresswell.project01;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.weather.WeatherForecast;
import com.example.mcresswell.project01.weather.WeatherFragment;
import com.example.mcresswell.project01.weather.WeatherViewModel;

import java.util.concurrent.atomic.AtomicBoolean;

public class WeatherActivity extends AppCompatActivity
        implements WeatherFragment.OnWeatherDataLoadedListener {

    private static final String LOG = WeatherActivity.class.getSimpleName();
//    private AtomicBoolean fragmentExists = new AtomicBoolean(false);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

//        if (!fragmentExists.get() && savedInstanceState != null) {
//            fragmentExists.set(true);
//        }

        String city = getIntent().getStringExtra("city");
        String country = getIntent().getStringExtra("country");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WeatherFragment weatherFragment = WeatherFragment.newInstance(city, country);
//        WeatherFragment weatherFragment = (WeatherFragment) fragmentManager.findFragmentById(R.id.fragment_weather);
        fragmentTransaction.add(R.id.fl_activity_weather, weatherFragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onWeatherDataLoaded(WeatherForecast forecast) {
        if (!getResources().getBoolean(R.bool.isWideDisplay)) {
            Log.d(LOG, "onWeatherDataLoaded, mobile");
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            WeatherFragment fragment = (WeatherFragment) fragmentManager.findFragmentById(R.id.fl_activity_weather);
            fragmentTransaction.replace(R.id.fl_activity_weather, fragment);
            fragmentTransaction.commit();

        }
    }
}

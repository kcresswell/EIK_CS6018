package com.example.mcresswell.project01.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.db.entity.Weather;
import com.example.mcresswell.project01.fragments.WeatherFragment;

public class WeatherActivity extends AppCompatActivity
        implements WeatherFragment.OnWeatherDataLoadedListener {

    private FragmentManager m_fragmentManager = getSupportFragmentManager();

    private static final String LOG = WeatherActivity.class.getSimpleName();
//    private AtomicBoolean fragmentExists = new AtomicBoolean(false);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

//        String city = getIntent().getStringExtra("city");
//        String country = getIntent().getStringExtra("country");

        FragmentTransaction fragmentTransaction = m_fragmentManager.beginTransaction();
        WeatherFragment frag_weather = WeatherFragment.newInstance();
        fragmentTransaction.add(R.id.fl_activity_weather, frag_weather);
        fragmentTransaction.commit();
    }

    @Override
    public void onWeatherDataLoaded(Weather weather) {
        if (!getResources().getBoolean(R.bool.isWideDisplay)) {
            Log.d(LOG, "onWeatherDataLoaded, mobile");

            FragmentTransaction fragmentTransaction = m_fragmentManager.beginTransaction();
            WeatherFragment frag_weather = (WeatherFragment) m_fragmentManager.findFragmentById(R.id.fl_activity_weather);
            fragmentTransaction.replace(R.id.fl_activity_weather, frag_weather);
            fragmentTransaction.commit();

        }
    }
}

package com.example.mcresswell.project01;

import android.os.Bundle;
import android.app.Activity;

public class WeatherActivity extends Activity
    implements WeatherFragment.OnWeatherFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
    }

    @Override
    public void onWeatherFragmentInteraction() {

    }
}

package com.example.mcresswell.project01;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.example.mcresswell.project01.util.WeatherForecast;
import com.example.mcresswell.project01.util.WeatherUtils;

import java.io.IOException;
import java.net.URL;

public class WeatherForecastLoader
        extends AsyncTaskLoader<String> {

    private static final String LOG = WeatherForecastLoader.class.getSimpleName();

    private static final int LOADER_ID = 1;
    public static final String LOCATION_QUERY_KEY = "url";
    private static String COORDINATES;
    private String forecastData;

    public WeatherForecastLoader(Context context, String coords) {
        super(context);
        COORDINATES = coords;
    }

    @Override
    protected void onStartLoading() {
        Log.d(LOG, "onStartLoading");

        if (forecastData != null){
            deliverResult(forecastData);
        }
        else {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {
        Log.d(LOG, "loadInBackground");
        URL url = WeatherUtils.buildWeatherApiUrl(WeatherUtils.getCityFromCoordinates(COORDINATES), WeatherUtils.getCountryFromCoordinates(COORDINATES));
        try {
            return WeatherUtils.getJsonDataFromUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deliverResult(String data) {
        super.deliverResult(data);
    }
}

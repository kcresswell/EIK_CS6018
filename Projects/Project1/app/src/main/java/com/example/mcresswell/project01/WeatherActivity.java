package com.example.mcresswell.project01;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class WeatherActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String> {

    private static final String LOG = WeatherActivity.class.getSimpleName();
    private String weatherForecast;
    private FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

//        getLoaderManager().initLoader(0, null, this).forceLoad();

        fragmentManager = getSupportFragmentManager();
        WeatherFragment fragment = savedInstanceState == null ?
                WeatherFragment.newInstance(savedInstanceState.getString("location")) :
                (WeatherFragment) fragmentManager.findFragmentById(R.id.cl_weather);

    }
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        return new WeatherForecastLoader(this, bundle.getString("location"));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String forecast) {
        Log.d(LOG, "onLoadFinished");
        if (forecast != null) {
            weatherForecast = forecast;
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        WeatherFragment fragment = (WeatherFragment) fragmentManager.findFragmentById(R.id.cl_weather);
        if (fragment == null) {
            Log.d(LOG, "onLoadFinished, WeatherFragment is null");
            return;
        }
        if (fragment != null) {
            fragmentTransaction.add(R.id.cl_weather, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}

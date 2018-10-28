package com.example.mcresswell.project01;

import android.app.Application;
import android.content.Intent;

import com.example.mcresswell.project01.db.InStyleDatabase;
import com.example.mcresswell.project01.db.repo.FitnessProfileRepository;
import com.example.mcresswell.project01.db.repo.UserRepository;
import com.example.mcresswell.project01.db.repo.WeatherRepository;

/**
 * Main application class containing static method calls to ensure the application only generates
 * a single instance of the InStyleDatabase, as well as each of the repositories. This minimizes
 * the overhead of unnecessarily creating multiple instances of each of the application components.
 */
public class InStyleApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        getDatabase();

//        getUserRepository();
//        getWeatherRepository();
//        getFitnessProfileRepository();
    }

    public InStyleDatabase getDatabase() {
        return InStyleDatabase.getDatabaseInstance(this);
    }

    public UserRepository getUserRepository() {
        return UserRepository.getInstance(getDatabase());
    }

    public FitnessProfileRepository getFitnessProfileRepository() {
//        return FitnessProfileRepository.getInstance(getDatabase());
        return null;
    }

    public WeatherRepository getWeatherRepository() {
        return WeatherRepository.getInstance(getDatabase());
    }
}

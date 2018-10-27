package com.example.mcresswell.project01.db.repo;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mcresswell.project01.db.InStyleDatabase;
import com.example.mcresswell.project01.db.dao.WeatherDao;
import com.example.mcresswell.project01.db.entity.Weather;
import com.example.mcresswell.project01.weather.WeatherClient;

import java.sql.Date;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static com.example.mcresswell.project01.util.WeatherUtils.DEFAULT_CITY;
import static com.example.mcresswell.project01.util.WeatherUtils.DEFAULT_COUNTRY;
import static com.example.mcresswell.project01.util.WeatherUtils.formatCaseCity;
import static com.example.mcresswell.project01.util.WeatherUtils.formatCaseCountryCodeFromCountryName;

/**
 * Repository/model class for the Weather entity that handles all business logic associated
 * handling Weather data.
 * This class interfaces with the in-memory database, and handles API calls to the
 * OpenWeatherMap API to retrieve live weather data. Data that is retrieved and/or modified is
 * passed to the WeatherViewModel class.
 */

public class WeatherRepository {

    private static final String LOG_TAG = WeatherRepository.class.getSimpleName();

    public static final long DATA_REFRESH_INTERVAL = 300_000L; //If weather data is older than 5 minutes, refetch data
    private static final String DUMMY_CITY = "SALT LAKE CITY";
    private static final String DUMMY_COUNTRY = "United States";

    private WeatherDao mWeatherDao;
    private InStyleDatabase inStyleDatabase;
    private static WeatherRepository weatherRepository;
    private static WeatherClient weatherClient = new WeatherClient(); //For fetching real-time weather data from API

    private MediatorLiveData<List<Weather>> m_observableWeatherList;
    private MediatorLiveData<Weather> m_observableWeather;

    private WeatherRepository(final InStyleDatabase database) {
        inStyleDatabase = database;
        mWeatherDao = inStyleDatabase.weatherDao();

//        asyncResetWeatherDatabase();

        //Populate with test data record by direct insertion into database
        //        asyncInsertWeather(testWeatherDatabaseRecord());


        //Retrieve additional data to insert into the database by making API calls to fetch real-time weather
//
//        asyncFetchWeatherFromApi("Tokyo", "Japan", false);
//        asyncFetchWeatherFromApi("Seoul", "Korea", false);
//        asyncFetchWeatherFromApi("Hong Kong", "Hong Kong", false);
//        asyncFetchWeatherFromApi("NEW YORK", "United States", false);
//        asyncFetchWeatherFromApi("Salt Lake City", "United States", false);
//        asyncFetchWeatherFromApi("Rome", "Italy", false);

        addLiveDataListenerSources();

    }

    public void fetchPlaceholderWeatherData() {
        //If weather database is empty, fetch dummy data to display while other data is being retrieved
        asyncFetchWeatherFromApi(DUMMY_CITY, DUMMY_COUNTRY, false);
    }

    private void addLiveDataListenerSources() {
        m_observableWeatherList = new MediatorLiveData<>();
        m_observableWeatherList.setValue(null);

        m_observableWeatherList.addSource(mWeatherDao.loadAllWeather(),
                weatherList -> {
                    if (weatherList != null) {
                        Log.d(LOG_TAG, "LiveData<List<<Weather>> loadAllWeather() onChanged");
                        if (inStyleDatabase.isDatabaseCreated().getValue() != null) {
                            m_observableWeatherList.setValue(weatherList);

                        }
                    }
                });


        m_observableWeather = new MediatorLiveData<>();
        m_observableWeather.setValue(null);

//        m_observableWeather.addSource(mWeatherDao.findFirstWeatherRecord(), weather -> { //FIXME: ADDED THIS IN PURELY FOR DEBUGGING, REMOVE THIS LATER
//            if (inStyleDatabase.isDatabaseCreated().getValue() != null) {
//                Log.d(LOG_TAG, "update to mWeatherDao.findFirstWeatherRecord() detected by mediator " +
//                        "live data");
//                Log.d(LOG_TAG, "Broadcasting updated value of LiveData<Weather> to observers");
//
//                m_observableWeather.setValue(weather);
//            }
//        });
    }


    /**
     * Static method to ensure only one instance of the WeatherRepository is instantiated.
     * @param database
     * @return
     */
    public static WeatherRepository getInstance(final InStyleDatabase database) {
        if (weatherRepository == null) {
            synchronized (WeatherRepository.class) {
                if (weatherRepository == null) {
                    weatherRepository = new WeatherRepository(database);
                }
            }
        }
        return weatherRepository;
    }

    private boolean isWeatherDataExpired(Weather weather) {
        if (weather == null) {
            return false;
        }
        Instant lastUpdated = weather.getLastUpdated().toInstant();
        long differential = Instant.now().toEpochMilli() - lastUpdated.toEpochMilli();
        Log.d(LOG_TAG, "Weather data was last fetched " + differential/1000 + " seconds ago");

        return  differential > DATA_REFRESH_INTERVAL;
    }

    public void fetchWeatherDataFromDataSource(String city, String country) {
        String cityScrubbed = formatCaseCity(city);
        String countryScrubbed = formatCaseCountryCodeFromCountryName(country);

        //Check weatherListViewModel data records to see if weather for user's location was recently retrieved
        if (m_observableWeatherList.getValue() != null) {
            Log.d(LOG_TAG, String.format("Weather list is not null, searching for weather for %s, %s", cityScrubbed, countryScrubbed));
            for (Weather r : m_observableWeatherList.getValue()) {

                if ((r.getCity().equals(cityScrubbed) &&
                        r.getCountryCode().equals(countryScrubbed)) ||
                        (r.getCountryCode() == null && r.getCity().equals(cityScrubbed))) {
                    Log.d(LOG_TAG, String.format("Existing weather data was found in database for %s, %s. " +
                            "Now just need to check to see whether weather data is more than 5 minutes old. ", cityScrubbed, countryScrubbed));
                    if (isWeatherDataExpired(r)) {
                        Log.d(LOG_TAG, "WEATHER DATA IS EXPIRED");

                        asyncFetchWeatherFromApi(city, country, true);
                    } else { //Weather data is in database and is not expired yet

                        Log.d(LOG_TAG, "WEATHER DATA IS STILL VALID");

                        m_observableWeather.setValue(r);

                    }
                    return;
                }
            }
            //Didn't find a matching record in the database, fetch from API then insert into database
            Log.d(LOG_TAG, String.format("No existing weather data record for %s, %s exists in the database", cityScrubbed, countryScrubbed));

            Log.d(LOG_TAG, "Fetching data for the first time from OpenWeatherAPI . . .");

            asyncFetchWeatherFromApi(city, country, false);
        }
    }

//            if (r.getValue() != null) {
//                Log.d(LOG_TAG, String.format("Database query result is not null, searching for weather for %s, %s", cityScrubbed, countryScrubbed));
//                Log.d(LOG_TAG, "Weather record result: \t" + r.getValue().getId() +"\t" + r.getValue().getCity() + "\t" + r.getValue().getCountryCode() + "\t" + r.getValue().getLastUpdated());
//                if ((r.getValue().getCity().equals(cityScrubbed) &&
//                        r.getValue().getCountryCode().equals(countryScrubbed)) ||
//                        (r.getValue().getCountryCode() == null && r.getValue().getCity().equals(cityScrubbed))) {
//                    Log.d(LOG_TAG, String.format( "Existing weather data was found in database for %s, %s. " +
//                            "Now just need to check to see whether weather data is more than 5 minutes old. ", cityScrubbed, countryScrubbed));
//                    if (isWeatherDataExpired(r.getValue())) {
//                        Log.d(LOG_TAG, "WEATHER DATA IS EXPIRED");
//
//                        asyncFetchWeatherFromApi(city, country, true);
//                    } else { //Weather data is in database and is not expired yet
//                        Log.d(LOG_TAG, "WEATHER DATA IS STILL VALID");
//
//                        m_observableWeather.setValue(r.getValue());
//
//                    }
//                    return;
//                }
//            }
//            //Didn't find a matching record in the database, fetch from API then insert into database
//            Log.d(LOG_TAG, String.format("No existing weather data record for %s, %s exists in the database", cityScrubbed, countryScrubbed));
//
//            Log.d(LOG_TAG, "Fetching data for the first time from OpenWeatherAPI . . .");
//
//            asyncFetchWeatherFromApi(city, country, false);
//        }
    public void loadRandomWeatherData(int recordNum) {
        asyncLoadRandomWeatherData(recordNum);
    }

    ////////////////////////// GETTERS /////////////////////////////


    public LiveData<Weather> getWeather() { return m_observableWeather; }

    public LiveData<List<Weather>> getAllWeather() {
        return m_observableWeatherList;
    }

    ////////////////// CRUD Database Operations ////////////////////

    public void insert(Weather weather) {
        weather.setCity(formatCaseCity(weather.getCity()));
        weather.setCountryCode(formatCaseCountryCodeFromCountryName(weather.getCountryCode()));

        asyncInsertWeather(weather);
    }

    public LiveData<Weather> findInDatabase(String city, String country) {
        String cityScrubbed = formatCaseCity(city);
        String countryScrubbed = formatCaseCountryCodeFromCountryName(country);
        m_observableWeather.addSource(
                mWeatherDao.findWeatherByLocation(cityScrubbed, countryScrubbed), weather -> {
                    if (weather != null) {
                        Log.d(LOG_TAG, String.format("findWeatherByLocation() for %s,%s LiveData<User> onChanged",
                                cityScrubbed,
                                countryScrubbed));
                        if (inStyleDatabase.isDatabaseCreated().getValue() != null) {
                            Log.d(LOG_TAG, "Broadcasting findWeatherByLocation() result to its observers... ");
                            m_observableWeather.setValue(weather);
                        }
                    }
                });

        asyncLoadWeatherFromDatabase(cityScrubbed, countryScrubbed);

        return m_observableWeather;
    }

    ///////////// ASYNC TASKS FOR FETCHING REAL-TIME WEATHER DATA FROM WEATHER API ///////////////

    @SuppressLint("StaticFieldLeak")
    private void asyncFetchWeatherFromApi(String city, String country, boolean isDataRefresh) {
        new AsyncTask<String, Void, Weather>() {
            @Override
            protected Weather doInBackground(String... params) {
                String city = params[0];
                String country = params[1];

                Log.d(LOG_TAG, String.format(
                        "Fetching real-time weather data from OpenWeatherMap API for %s, %s", city, country));

                Weather weatherData = weatherClient.fetchCurrentWeather(city, country);

                if (isDataRefresh) { //Update existing data record for previously fetched location
                    Log.d(LOG_TAG, String.format(
                            "Updating weather data fetched from OpenWeatherMap API for %s, %s " +
                                    "into database to replace existing data record with new time last updated as %s",
                            weatherData.getCity(), weatherData.getCountryCode(), weatherData.getLastUpdated().toString()));
//                        mWeatherDao.updateWeather(weatherData);
                } else { //Weather data not previously fetched before, insert
                    Log.d(LOG_TAG, String.format(
                            "Inserting weather data fetched from OpenWeatherMap API for %s, %s " +
                                    "as new record into database with time last updated as %s",
                            weatherData.getCity(), weatherData.getCountryCode(), weatherData.getLastUpdated().toString()));
                }
                mWeatherDao.insertWeather(weatherData);

                return weatherData;
            }

            @Override
            protected void onPostExecute(Weather weather) {
                m_observableWeather.setValue(weather);
            }
        }.execute(city, country);
    }

    //////////// ASYNC TASKS FOR A SINGLE WEATHER RECORD /////////////

    @SuppressLint("StaticFieldLeak")
    private void asyncLoadWeatherFromDatabase(String city, String country) {
        new AsyncTask<String, Void, Weather>() {
            @Override
            protected Weather doInBackground(String... params) {
                String city = params[0];
                String country = params[1];
                Log.d(LOG_TAG, String.format("Retrieving weather record for %s, %s from database", city, country));

                LiveData<Weather> weather = mWeatherDao.findWeatherByLocation(city, country);
                return weather.getValue();
            }

            @Override
            protected void onPostExecute(Weather weather) {
                m_observableWeather.setValue(weather);
            }
        }.execute(city, country);
    }

    @SuppressLint("StaticFieldLeak")
    private void asyncInsertWeather(Weather weather) {
        new AsyncTask<Weather, Void, Void>() {
            @Override
            protected Void doInBackground(Weather... params) {
                Weather weatherToInsert = params[0];
                Log.d(LOG_TAG, String.format("Inserting weather record for %s, %s into database",
                        weatherToInsert.getCity(),
                        weatherToInsert.getCountryCode()));

                mWeatherDao.insertWeather(weatherToInsert);

                Log.d(LOG_TAG, "Inserting Weather data . . .");
                return null;
            }
        }.execute(weather);
    }

    ////////////////// ASYNC TASKS FOR A MULTIPLE WEATHER RECORDS //////////////////

    @SuppressLint("StaticFieldLeak")
    private void asyncLoadWeatherDataFromDatabase() {
        new AsyncTask<Void, Void, List<Weather>>() {
            @Override
            protected List<Weather> doInBackground(Void... params) {
                Log.d(LOG_TAG, "Loading users from database");
                LiveData<List<Weather>> listLiveData = mWeatherDao.loadAllWeather();

                if (listLiveData.getValue() != null) {
                    return listLiveData.getValue();
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<Weather> weatherList) {
                m_observableWeatherList.setValue(weatherList);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void asyncDeleteAllWeatherDataFromDatabase() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.d(LOG_TAG, "Deleting all users from database");
                mWeatherDao.deleteAllWeather();
                return null;
            }

            @Override
            protected void onPostExecute(Void voidResult) {
                m_observableWeatherList.setValue(Collections.emptyList());
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private void asyncLoadRandomWeatherData(int recordNumber) {
        new AsyncTask<Void, Void, Weather>() {
            @Override
            protected Weather doInBackground(Void... params) {
                Log.d(LOG_TAG, String.format("Loading weather record number %d", recordNumber));

                LiveData<Weather> weatherResult = mWeatherDao.findWeatherById(recordNumber);
                return weatherResult.getValue();
            }

            @Override
            protected void onPostExecute(Weather weather) {
                m_observableWeather.setValue(weather);
            }
        }.execute();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    private Weather testWeatherDatabaseRecord() {
        Weather weather = new Weather();
        weather.setCity(formatCaseCity(DEFAULT_CITY));
        weather.setCountryCode(formatCaseCountryCodeFromCountryName(DEFAULT_COUNTRY));
        weather.setLatitude(Float.parseFloat("40.77"));
        weather.setLongitude(Float.parseFloat("-111.89"));
        weather.setForecastMain("Clouds");
        weather.setForecastDescription("broken clouds");
        Weather.Temperature temperature =
                weather.createTemp(
                        Float.parseFloat("277.37"),
                        Float.parseFloat("276.15"),
                        Float.parseFloat("278.75"));

        weather.setTemperature(temperature);
        weather.setPressure(Integer.parseInt("1027"));
        weather.setHumidity(Integer.parseInt("13"));
        weather.setWindSpeed(Float.parseFloat("4.1"));
        weather.setLastUpdated(Date.from(Instant.now()));

        return weather;
    }
}

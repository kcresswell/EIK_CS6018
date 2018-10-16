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
import static com.example.mcresswell.project01.util.WeatherUtils.formatCaseCountryCode;

/**
 * Repository/model class for the Weather entity that handles all business logic associated
 * handling Weather data.
 * This class interfaces with the in-memory database, and handles API calls to the
 * OpenWeatherMap API to retrieve live weather data. Data that is retrieved and/or modified is
 * passed to the WeatherViewModel class.
 */

//TODO: MOVE EXISTING BUSINESS LOG_TAGIC FROM WEATHERVIEWMODEL TO WEATHERREPOSITORY
public class WeatherRepository {

    private static final String LOG_TAG = WeatherRepository.class.getSimpleName();

    public static final long DATA_REFRESH_INTERVAL = 300L; //If weather data is older than 5 minutes, refetch data

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

//        asyncInsertWeather(testWeatherDatabaseRecord());

        //Populate with data to test async retrieval of data using API

//        asyncFetchWeatherFromApi("Tokyo", "JP", false);
//
//        asyncFetchWeatherFromApi("Seoul", "KR", false);
//
//        asyncFetchWeatherFromApi("Hong Kong", null, false);
//
//        asyncFetchWeatherFromApi("NEW YORK", "US", false);




        addLiveDataListenerSources();

    }

    private void addLiveDataListenerSources() {
        m_observableWeatherList = new MediatorLiveData<>();
        m_observableWeatherList.setValue(null);

        m_observableWeatherList.addSource(mWeatherDao.loadAllWeather(),
                users -> {
                    Log.d(LOG_TAG, "LiveData<List<<Weather>> loadAllWeather() onChanged");
                    if (inStyleDatabase.isDatabaseCreated().getValue() != null) {
                        m_observableWeatherList.setValue(users);
                    }
                });


        m_observableWeather = new MediatorLiveData<>();
        m_observableWeather.setValue(null);

        m_observableWeather.addSource(mWeatherDao.findFirstWeatherRecord(), weather -> { //FIXME: ADDED THIS IN PURELY FOR DEBUGGING, REMOVE THIS LATER
            if (inStyleDatabase.isDatabaseCreated().getValue() != null) {
                Log.d(LOG_TAG, "update to mWeatherDao.findFirstWeatherRecord() detected by mediator " +
                        "live data");
                Log.d(LOG_TAG, "Broadcasting updated value of LiveData<Weather> to observers");

                m_observableWeather.setValue(weather);
            }
        });
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

        return  differential > DATA_REFRESH_INTERVAL;
    }

    public void loadWeatherData(String city, String country) {
        LiveData<Weather> result = findInDatabase(city, country);
        if (result.getValue() != null) {

            String resultCity = result.getValue().getCity();
            String resultCountry = result.getValue().getCountryCode();

            if ((city.equalsIgnoreCase(resultCity) &&
                    country.equalsIgnoreCase(resultCountry)) ||
                    (resultCountry == null &&  city.equalsIgnoreCase(resultCity))) {

                Log.d(LOG_TAG, "loadWeatherData() Weather data was found in database!!!! Now just need to check to see whether weather data is more than 5 minutes old!!!! ");

                if (isWeatherDataExpired(result.getValue())) {
                    asyncFetchWeatherFromApi(city, country, true);

                    return;
                }

                //Otherwise do nothing (weather data retrieved from database is less than 5 minutes old)

            } else { //Didn't find a matching record in the database, fetch from API then insert into database
                Log.d(LOG_TAG, String.format("No existing weather data record for %s, %s exists in the database", city, country));

                Log.d(LOG_TAG, "Fetching data for the first time from OpenWeatherAPI . . .");

                asyncFetchWeatherFromApi(city, country, false);
            }
        }
    }

    ////////////////////////// GETTERS /////////////////////////////


    public LiveData<Weather> getWeather() { return m_observableWeather; }

    public LiveData<List<Weather>> getAllWeather() {
        return m_observableWeatherList;
    }

    ////////////////// CRUD Database Operations ////////////////////

    public void insert(Weather weather) {
        weather.setCity(formatCaseCity(weather.getCity()));
        weather.setCountryCode(formatCaseCountryCode(weather.getCountryCode()));

        asyncInsertWeather(weather);
    }

    public LiveData<Weather> findInDatabase(String city, String country) {
        String cityScrubbed = formatCaseCity(city);
        String countryScrubbed = formatCaseCountryCode(country);
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


    public void update(Weather weather) {
        weather.setCity(formatCaseCity(weather.getCity()));
        weather.setCountryCode(formatCaseCountryCode(weather.getCountryCode()));

        asyncUpdateWeather(weather);
    }

    public void delete(Weather weather) {
        weather.setCity(formatCaseCity(weather.getCity()));
        weather.setCountryCode(formatCaseCountryCode(weather.getCountryCode()));

        asyncDeleteWeather(weather);
    }

    public void deleteAll() {
        asyncDeleteAllWeatherDataFromDatabase();
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

    @SuppressLint("StaticFieldLeak")
    private void asyncUpdateWeather(Weather weather) {
        new AsyncTask<Weather, Void, Void>() {
            @Override
            protected Void doInBackground(Weather... params) {
                Weather weatherToUpdate = params[0];
                Log.d(LOG_TAG, String.format("Updating weather record for %s, %s in database",
                        weatherToUpdate.getCity(),
                        weatherToUpdate.getCountryCode()));

                mWeatherDao.updateWeather(weatherToUpdate);

                Log.d(LOG_TAG, "Updating weather data . . .");
                return null;
            }
        }.execute(weather);
    }

    @SuppressLint("StaticFieldLeak")
    private void asyncDeleteWeather(Weather weather) {
        new AsyncTask<Weather, Void, Void>() {
            @Override
            protected Void doInBackground(Weather... params) {
                Weather weatherToDelete = params[0];
                Log.d(LOG_TAG, String.format("Deleting weather record for %s, %s from database",
                        weatherToDelete.getCity(),
                        weatherToDelete.getCountryCode()));

                mWeatherDao.deleteWeather(weatherToDelete);
                Log.d(LOG_TAG, "Deleting Weather data . . .");
                return null;
            }
        }.execute(weather);
    }


    ////////////////// ASYNC TASKS FOR A LIST OF WEATHER RECORDS //////////////////

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    public void asyncResetWeatherDatabase() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
//                LiveData<Integer> numRecords = mUserDao.getUserCount();
//                if (numRecords.getValue() != null && numRecords.getValue() >= MAX_USERS) {
//                    Log.d(LOG_TAG, "Number of records in User table exceeds max. Resetting database contents");
                mWeatherDao.deleteAllWeather();
//                }
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("unchecked")
    public void asyncPopulateDatabaseWeatherData(List<Weather> weatherList) {
        new AsyncTask<List<Weather>, Void, Void>() {
            @Override
            protected Void doInBackground(List<Weather>... params) {

                Log.d(LOG_TAG, "Inserting test data into database table to populate.");
                //Insert randomly generated weather data

                mWeatherDao.insertAllWeather(params[0]);

                return null;
            }
        }.execute(weatherList);
    }

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


                Log.d(LOG_TAG, String.format(
                        "Inserting weather data fetched from OpenWeatherMap API for %s, %s into database", weatherData.getCity(), weatherData.getCountryCode()));

                if (isDataRefresh) { //Update existing data record for previously fetched location
                    mWeatherDao.updateWeather(weatherData);
                } else { //Weather data not previously fetched before, insert
                    mWeatherDao.insertWeather(weatherData);
                }

                return weatherData;
            }

            @Override
            protected void onPostExecute(Weather weather) {
                m_observableWeather.setValue(weather);
            }
        }.execute(city, country);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////

    private Weather testWeatherDatabaseRecord() {
        Weather weather = new Weather();
        weather.setCity(formatCaseCity(DEFAULT_CITY));
        weather.setCountryCode(formatCaseCountryCode(DEFAULT_COUNTRY));
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

package com.example.mcresswell.project01.util;

import android.util.Log;

import com.example.mcresswell.project01.weather.WeatherForecast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

public class WeatherUtils {

    private static final String LOG = WeatherUtils.class.getSimpleName();

    private static final String API_URL_CITY_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    //private static final String API_URL_LATLON_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s";
    private static final String OWM_API_KEY = "718324f1015a96a2369287e867133dd9";
    private static final String API_KEY_QUERY = "&appid=" + OWM_API_KEY;

    private static final String DEFAULT_CITY = "SALT+LAKE+CITY";
    private static final String DEFAULT_COUNTRY = "US";

    public static final String INVALID_CITY_URL_JSON_RESPONSE = "{\"cod\":\"404\",\"message\":\"city not found\"}";

    public static WeatherForecast getWeatherForecast(String jsonResponse) {
        if (!ValidationUtils.isNotNullOrEmpty(jsonResponse) ||
                jsonResponse.equals(INVALID_CITY_URL_JSON_RESPONSE)) {
            return null;
        }
        WeatherForecast forecast = new WeatherForecast(jsonResponse);
        if (forecast != null) {
            forecast.printWeatherForecast();
        }
        return forecast;
    }

    public static String getJsonDataFromUrl(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        StringBuilder response = new StringBuilder();
        Scanner s = null;
        try {
            s = new Scanner(connection.getInputStream());

            if (connection.getResponseCode() != 200) {
                System.out.println("Error ResponseCode: " + connection.getResponseCode());
                return response.append(INVALID_CITY_URL_JSON_RESPONSE).toString();
            }

            while (s.hasNext()) {
                String next = s.nextLine();
                response.append(next);
            }
        } catch (FileNotFoundException e) {
            return response.append(INVALID_CITY_URL_JSON_RESPONSE).toString();
        }
        if (s != null) {
            s.close();
        }
        if (connection != null) {
            connection.disconnect();
        }
            return response.toString();
        }

        public static URL buildWeatherApiUrl(String city, String countryCode) {
            URI uri = null;
            if (!isValidCity(city, countryCode)) {
                return buildDefaultWeatherApiUrl();
            }
            else if (!isValidCountryCode(countryCode)) { //Country code is not required
                uri = URI.create(API_URL_CITY_BASE_URL + city + API_KEY_QUERY);
            } else { // City and country are valid
                uri = URI.create(API_URL_CITY_BASE_URL + city + "," + countryCode + API_KEY_QUERY);
            }
            Log.d(LOG, "Weather url: " + uri.toString());
            try {
                return uri.toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return null;
        }

        private static boolean isValidCity(String city, String countryCode) {
            return ValidationUtils.isNotNullOrEmpty(city);
        }

        private static boolean isValidCountryCode(String countryCode) {
            return ValidationUtils.isNotNullOrEmpty(countryCode) &&
                    countryCode.trim().length() == 2;
        }

        public static URL buildDefaultWeatherApiUrl() {
            try {
                return URI.create(API_URL_CITY_BASE_URL + DEFAULT_CITY + "," +
                        DEFAULT_COUNTRY + API_KEY_QUERY).toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static double kelvinToFarenheit(double tempInKelvin) {
            return tempInKelvin * (9/5.0) - 459.67;
        }

    }

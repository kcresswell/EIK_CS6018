package com.example.mcresswell.project01.weather;

import android.util.Log;

import com.example.mcresswell.project01.util.ValidationUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;

import javax.inject.Singleton;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.example.mcresswell.project01.util.ValidationUtils.isValidCity;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidCountryCode;

@Singleton
public class WeatherClient {

    private static final String LOG = WeatherClient.class.getSimpleName();

    public static final String API_BASE_URL = "http://api.openweathermap.org";
    private static final String API_ENDPOINT = "/data/2.5/weather?q=";

    //private static final String API_URL_LATLON_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s";
    private static final String OWM_API_KEY = "718324f1015a96a2369287e867133dd9";
    private static final String API_KEY_QUERY = "&appid=" + OWM_API_KEY;

    public static final String DEFAULT_CITY = "SALT+LAKE+CITY";
    public static final String DEFAULT_COUNTRY = "US";
    public static final String INVALID_CITY_URL_JSON_RESPONSE = "{\"cod\":\"404\",\"message\":\"city not found\"}";

    private static Retrofit retrofit;

    @Singleton
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .build();
        }
        return retrofit;
    }

    public interface WeatherService {
        @GET("/data/2.5/weather?q={city},{country}&appid={api_key}")
        Call<WeatherForecast> getnWeatherByCityCountry(@Path("city") String city,
                                                       @Path("country") String country,
                                                       @Path("api_key") String apiKey);

        @GET("/data/2.5/weather?q={city}&appid={api_key}")
        Call<WeatherForecast> getWeatherByCity(@Path("city") String city,
                                               @Path("api_key") String apiKey);

        @GET("/data/2.5/weather?q=" + DEFAULT_CITY + "," + DEFAULT_COUNTRY + "&appid={api_key}")
        Call<WeatherForecast> getDefaultWeather(@Path("api_key") String apiKey);


    }

    public static WeatherForecast fetchCurrentWeather(String city, String country) throws MalformedURLException {
        final URL FETCH_WEATHER_URL = buildWeatherApiUrl(city, country);
        try {
            final String RESPONSE_BODY = getJsonDataFromUrl(FETCH_WEATHER_URL);
            return getWeatherForecast(RESPONSE_BODY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

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

    private static String getAbsoluteUrl(String relativeUrl) {
        return API_BASE_URL + API_ENDPOINT + relativeUrl;
    }

    public static URL buildWeatherApiUrl(String city, String countryCode){
        URI uri = URI.create(getAbsoluteUrl( city + "," + countryCode + API_KEY_QUERY));

        Log.d(LOG, "Weather url: " + uri.toString());
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            Log.d(LOG, "Malformed URL: " + uri.toString());
            e.printStackTrace();
        }
        return null;
    }

    public static URL buildDefaultWeatherApiUrl() {
        try {
            return URI.create(getAbsoluteUrl(DEFAULT_CITY + "," +
                    DEFAULT_COUNTRY + API_KEY_QUERY)).toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }



}

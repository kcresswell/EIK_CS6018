package com.example.mcresswell.project01;

import com.example.mcresswell.project01.weather.WeatherClient;

import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WeatherClientUnitTest {

    @Test
    public void getJsonDataFromUrl_defaultLocation_returnsValidJson() {
        String s = null;
        try {
            s = WeatherClient.getJsonDataFromUrl(WeatherClient.buildDefaultWeatherApiUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(s);
        assertNotEquals(s, WeatherClient.INVALID_CITY_URL_JSON_RESPONSE);
    }

    @Test
    public void getJsonDataFromUrl_invalidLocation_returnsErrorJson() {
        String s = null;
        try {
            s = WeatherClient.getJsonDataFromUrl(WeatherClient.buildWeatherApiUrl("INVALIDCITY", null));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(s);
        assertEquals(s, WeatherClient.INVALID_CITY_URL_JSON_RESPONSE);
    }

    @Test
    public void buildWeatherApiUrl() throws MalformedURLException {
        URL url = WeatherClient.buildWeatherApiUrl("PROVO", "US");
        assertTrue(url.equals(new URL("http://api.openweathermap.org/data/2.5/weather?q=PROVO,US&appid=718324f1015a96a2369287e867133dd9")));

    }

    @Test
    public void buildWeatherApiUrl_invalidCountry_returnsValidUrl() throws MalformedURLException {
        URL url = WeatherClient.buildWeatherApiUrl("PROVO", null);
        assertTrue(url.equals(new URL("http://api.openweathermap.org/data/2.5/weather?q=PROVO&appid=718324f1015a96a2369287e867133dd9")));
    }

    @Test
    public void buildWeatherApiUrl_invalidCityInvalidCountry_returnsDefaultUrl() {
        URL url = WeatherClient.buildWeatherApiUrl(" ", null);
        assertTrue(url.equals(WeatherClient.buildDefaultWeatherApiUrl()));
    }

    @Test
    public void getJsonDataFromUrl_validCity_returnsWeatherForecast() {
        URL url = WeatherClient.buildWeatherApiUrl("PROVO", null);
        String result = null;
        try {
            result = WeatherClient.getJsonDataFromUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(WeatherClient.getWeatherForecast(result));
    }

    @Test
    public void getJsonDataFromUrl_invalidCity_returnsNullWeatherForecast() throws MalformedURLException {
        URL url = WeatherClient.buildWeatherApiUrl("INVALIDCITY", null);
        String result = null;
        try {
            result = WeatherClient.getJsonDataFromUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNull(WeatherClient.getWeatherForecast(result));
    }


}
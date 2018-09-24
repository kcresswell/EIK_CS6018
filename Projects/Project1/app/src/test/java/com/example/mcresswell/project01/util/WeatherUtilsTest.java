package com.example.mcresswell.project01.util;

import org.json.JSONException;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

public class WeatherUtilsTest {

    @Test
    public void getJsonDataFromUrl_defaultLocation_returnsValidJson() {
        String s = null;
        try {
            s = WeatherUtils.getJsonDataFromUrl(WeatherUtils.buildDefaultWeatherApiUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(s);
        assertNotEquals(s, WeatherUtils.INVALID_CITY_URL_JSON_RESPONSE);
    }

    @Test
    public void getJsonDataFromUrl_invalidLocation_returnsErrorJson() {
        String s = null;
        try {
            s = WeatherUtils.getJsonDataFromUrl(WeatherUtils.buildWeatherApiUrl("INVALIDCITY", null));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(s);
        assertEquals(s, WeatherUtils.INVALID_CITY_URL_JSON_RESPONSE);
    }

    @Test
    public void buildWeatherApiUrl() throws MalformedURLException {
        URL url = WeatherUtils.buildWeatherApiUrl("PROVO", "US");
        assertTrue(url.equals(new URL("http://api.openweathermap.org/data/2.5/weather?q=PROVO,US&appid=718324f1015a96a2369287e867133dd9")));

    }

    @Test
    public void buildWeatherApiUrl_invalidCountry_returnsValidUrl() throws MalformedURLException {
        URL url = WeatherUtils.buildWeatherApiUrl("PROVO", null);
        assertTrue(url.equals(new URL("http://api.openweathermap.org/data/2.5/weather?q=PROVO&appid=718324f1015a96a2369287e867133dd9")));
    }

    @Test
    public void buildWeatherApiUrl_invalidCityInvalidCountry_returnsDefaultUrl() {
        URL url = WeatherUtils.buildWeatherApiUrl(" ", null);
        assertTrue(url.equals(WeatherUtils.buildDefaultWeatherApiUrl()));
    }

    @Test
    public void getJsonDataFromUrl_validCity_returnsWeatherForecast() {
        URL url = WeatherUtils.buildWeatherApiUrl("PROVO", null);
        String result = null;
        try {
            result = WeatherUtils.getJsonDataFromUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNotNull(WeatherUtils.getWeatherForecast(result));
    }

    @Test
    public void getJsonDataFromUrl_invalidCity_returnsNullWeatherForecast() throws MalformedURLException {
        URL url = WeatherUtils.buildWeatherApiUrl("INVALIDCITY", null);
        String result = null;
        try {
            result = WeatherUtils.getJsonDataFromUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertNull(WeatherUtils.getWeatherForecast(result));
    }


}
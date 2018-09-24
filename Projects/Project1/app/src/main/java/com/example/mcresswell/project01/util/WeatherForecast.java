package com.example.mcresswell.project01.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Locale;

public class WeatherForecast {
    private String[] coords;
    private String forecastMain;
    private String forecastDescription;

    private String temp;
    private String pressure;
    private String humidity;
    private String temp_min;
    private String temp_max;

    private String windSpeed;

    private String countryCode;

    private String city;

    public WeatherForecast (String response) {
        JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();

        JsonObject coord = jsonObject.get("coord").getAsJsonObject();
        JsonObject weather = jsonObject.get("weather").getAsJsonArray().get(0).getAsJsonObject();
        JsonObject main = jsonObject.get("main").getAsJsonObject();
        JsonObject wind = jsonObject.get("wind").getAsJsonObject();

        coords = new String[] {String.valueOf(coord.get("lat")), String.valueOf(coord.get("lon"))};

        forecastMain = String.valueOf(weather.get("main")).replace("\"", "");
        forecastDescription = String.valueOf(weather.get("description")).replace("\"", "");

        temp = convertAndFormatTemp(main.get("temp").getAsDouble());
        pressure = String.valueOf(main.get("pressure"));
        humidity = String.valueOf(main.get("humidity"));
        temp_min = convertAndFormatTemp(main.get("temp_min").getAsDouble());
        temp_max = convertAndFormatTemp(main.get("temp_max").getAsDouble());

        windSpeed = String.format(Locale.US, "%.1f", wind.get("speed").getAsDouble());

        countryCode = String.valueOf(jsonObject.get("sys").getAsJsonObject().get("country"));
        city = String.valueOf(jsonObject.get("name"));
    }

    public void printWeatherForecast() {
        System.out.println("Weather Forecast for " + city + ", " + countryCode + ":\n");

        System.out.println("coords: lat: " + coords[0] + ", lon: " + coords[1]);
        System.out.println("forecast: " + forecastMain);
        System.out.println("forecast description: " + forecastDescription);
        System.out.println("temp: " + temp + " F");
        System.out.println("temp_min: " + temp_min + " F");
        System.out.println("temp_max: " + temp_max + " F");
        System.out.println("pressure: " + pressure);
        System.out.println("humidity: " + humidity + "%");
        System.out.println("windspeed: " + windSpeed);
    }

    private String convertAndFormatTemp(double temp) {
        return String.format(Locale.US,"%.1f", WeatherUtils.kelvinToFarenheit(temp));
    }


}

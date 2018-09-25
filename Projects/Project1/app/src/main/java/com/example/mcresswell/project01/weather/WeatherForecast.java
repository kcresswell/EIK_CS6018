package com.example.mcresswell.project01.weather;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.util.WeatherUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class WeatherForecast implements Parcelable{
    private final String FARENHEIT = " °F";

    private String coords;
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

        coords = String.valueOf(coord.get("lat")) + "," + String.valueOf(coord.get("lon"));

        forecastMain = String.valueOf(weather.get("main")).replace("\"", "");
        forecastDescription = String.valueOf(weather.get("description")).replace("\"", "");

        temp = convertAndFormatTemp(main.get("temp").getAsDouble());
        pressure = String.valueOf(main.get("pressure"));
        humidity = String.valueOf(main.get("humidity")) + "%";
        temp_min = convertAndFormatTemp(main.get("temp_min").getAsDouble());
        temp_max = convertAndFormatTemp(main.get("temp_max").getAsDouble());

        windSpeed = String.format(Locale.US, "%.1f", wind.get("speed").getAsDouble());

        countryCode = String.valueOf(jsonObject.get("sys").getAsJsonObject().get("country")).replace("\"", "");;
        city = String.valueOf(jsonObject.get("name")).replace("\"", "");
    }

    protected WeatherForecast(Parcel in) {
        coords = in.readString();
        forecastMain = in.readString();
        forecastDescription = in.readString();
        temp = in.readString();
        pressure = in.readString();
        humidity = in.readString();
        temp_min = in.readString();
        temp_max = in.readString();
        windSpeed = in.readString();
        countryCode = in.readString();
        city = in.readString();
    }

    public static final Creator<WeatherForecast> CREATOR = new Creator<WeatherForecast>() {
        @Override
        public WeatherForecast createFromParcel(Parcel in) {
            return new WeatherForecast(in);
        }

        @Override
        public WeatherForecast[] newArray(int size) {
            return new WeatherForecast[size];
        }
    };

    public void printWeatherForecast() {
        System.out.println("Weather Forecast for " + city + ", " + countryCode + ":\n");
        Log.d("Weather Forecast", "Weather Forecast for " + city + ", " + countryCode + ":\n");

        System.out.println("coords: " + coords);
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
        return String.format(Locale.US,"%.1f", WeatherUtils.kelvinToFarenheit(temp)) + FARENHEIT;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getForecastMain() {
        return forecastMain;
    }

    public void setForecastMain(String forecastMain) {
        this.forecastMain = forecastMain;
    }

    public String getForecastDescription() {
        return forecastDescription;
    }

    public void setForecastDescription(String forecastDescription) {
        this.forecastDescription = forecastDescription;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(FARENHEIT);
        dest.writeString(coords);
        dest.writeString(forecastMain);
        dest.writeString(forecastDescription);
        dest.writeString(temp);
        dest.writeString(pressure);
        dest.writeString(humidity);
        dest.writeString(temp_min);
        dest.writeString(temp_max);
        dest.writeString(windSpeed);
        dest.writeString(countryCode);
        dest.writeString(city);
    }
}

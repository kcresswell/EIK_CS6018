package com.example.mcresswell.project01.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity
public class Weather {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "country")
    private String countryCode;

    @ColumnInfo(name = "lat")
    private float latitude;

    @ColumnInfo(name = "lon")
    private float longitude;

    @ColumnInfo(name = "summary")
    private String forecastMain;

    @ColumnInfo(name = "detail")
    private String forecastDescription;

    @ColumnInfo(name = "current_temp")
    private float temp;

    @ColumnInfo(name = "temp_min")
    private float temp_min;

    @ColumnInfo(name = "temp_max")
    private float temp_max;

    @ColumnInfo(name = "wind")
    private float windSpeed;

    @ColumnInfo(name = "pressure")
    private int pressure;

    @ColumnInfo(name = "humidity")
    private int humidity;

    @ColumnInfo(name = "last_updated")
    private Date lastUpdated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
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

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }

    public float getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(float temp_min) {
        this.temp_min = temp_min;
    }

    public float getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(float temp_max) {
        this.temp_max = temp_max;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getPressure() {
        return pressure;
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}

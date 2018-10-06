package com.example.mcresswell.project01.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.mcresswell.project01.db.entity.Weather;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) for querying Weather table in database.
 * Scenarios where this DAO will be used:
 * 1. A weather forecast for a new location is stored in database.
 * 2. An updated weather forecast for an existing location is stored.
 * 3. The database is queried for a weather forecast for a given location.
 *
 * An Optional<Weather> is returned in the case where no weather forecast for a given location exists
 * in the Weather table yet. This prevents an exception from being thrown.
 */

@Dao
public interface WeatherDao {

    @Query("SELECT * FROM Weather WHERE city=:city AND (country IS NULL OR country LIKE :country)")
    Optional<Weather> findByLocation(String city, String country);

    @Insert
    void insert(Weather weather);

    @Update
    void update(Weather weather);


    //Below methods are for configuring/populating Weather database table
    @Insert
    void insertAll(List<Weather> weatherList);

    @Query("SELECT * FROM Weather ORDER BY city ASC")
    List<Weather> getAllWeatherData();

}

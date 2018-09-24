package com.example.mcresswell.project01.util;

import android.location.Location;

public class LocationUtils {

    //Location[gps 37.421998,-122.084000 hAcc=20 et=+1h16m59s755ms alt=0.0 vAcc=??? sAcc=??? bAcc=??? {Bundle[mParcelledData.dataSize=96]}]
    public static String getLocationCoordinates(Location location) {
        String[] locationData = location.toString().split(" ");
        return locationData[1].trim();
    }

    public static String getCityFromCoords(String coordinates) {
        return null;
    }

    public static String getCountryFromCoords(String coordinates) {
        return null;
    }
}

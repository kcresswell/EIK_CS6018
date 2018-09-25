package com.example.mcresswell.project01.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.example.mcresswell.project01.DashboardFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationUtils {

    private static final String LOG = DashboardFragment.class.getSimpleName();

    //Location[gps 37.421998,-122.084000 hAcc=20 et=+1h16m59s755ms alt=0.0 vAcc=??? sAcc=??? bAcc=??? {Bundle[mParcelledData.dataSize=96]}]
    public static String getLocationCoordinates(Location location) {
        String[] locationData = location.toString().split(" ");
        return locationData[1].trim();
    }

    public static ArrayList<String> getLocationFromCoordinates(Context context, String coordinates) {
        String lat = coordinates.split(",")[0];
        String lon = coordinates.split(",")[1];
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String cityName = addresses.get(0).getLocality();
        String stateName = addresses.get(0).getAdminArea();
        String countryName = addresses.get(0).getCountryName();
        Log.d(LOG, "cityName :" + cityName);
        Log.d(LOG, "state :" + stateName);
        Log.d(LOG, "country :" + countryName);
        return null;
    }

    public String getCityFromCoords(String coordinates) {
        return null;
    }

    public String getCountryFromCoords(String coordinates) {
        return null;
    }
}

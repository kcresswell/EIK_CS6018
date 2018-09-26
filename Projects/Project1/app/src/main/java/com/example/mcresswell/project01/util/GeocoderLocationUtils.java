package com.example.mcresswell.project01.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.example.mcresswell.project01.DashboardFragment;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import static com.example.mcresswell.project01.util.ValidationUtils.isValidCity;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidCountryCode;

public class GeocoderLocationUtils {

    private static final String LOG = DashboardFragment.class.getSimpleName();

    private static final String GEOCODER_API_BASE_URL =
            "https://maps.googleapis.com/maps/api/geocode/json?address=";
    private static final String GOOGLE_MAPS_API_KEY = "AIzaSyB_U6PQFP1cdeWwwhPgM9VA7xMfWaGzhJo";
    private static final String API_KEY_QUERY = "&key=" + GOOGLE_MAPS_API_KEY;

    private static final String DEFAULT_CITY = "SALT+LAKE+CITY";
    private static final String DEFAULT_COUNTRY = "US";
    private static final String GOOGLE_API_ERROR_RESPONSE =
            "{\n" + "    \"results\": [],\n" + "    \"status\": \"ZERO_RESULTS\"\n" + "}";

    public static String getCoordinatesFromCityCountry(String city, String countryCode) throws IOException {
        URL apiUrl = buildGeocoderApiUrl(city, countryCode);
        String response = retrieveGeocoderApiResponseFromUrl(apiUrl);
        return extractCoordinatesFromGeocoderResponse(response);
    }

    public static URL buildGeocoderApiUrl(String city, String countryCode) {
        URI uri = null;
        if (!isValidCity(city, countryCode)) {
            return buildDefaultGeocoderApiUrl();
        }
        else if (!isValidCountryCode(countryCode)) { //Country code is not required
            uri = URI.create(GEOCODER_API_BASE_URL + city + API_KEY_QUERY);
        } else { // City and country are valid
            uri = URI.create(GEOCODER_API_BASE_URL + city + "," + countryCode + API_KEY_QUERY);
        }
        Log.d(LOG, "Google maps Geocoder API url: " + uri.toString());
        try {
            return uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String extractCoordinatesFromGeocoderResponse(String jsonResponse) {
        JsonObject jsonObject = new JsonParser().parse(jsonResponse).getAsJsonObject();
        String status = jsonObject.get("status").getAsString();
        if (!status.equals("OK")) {
            Log.d(LOG, "Error performing Google Maps API Geocoder query");
            return null;
        }
        JsonObject results = jsonObject.get("results").getAsJsonArray().get(0).getAsJsonObject();
        JsonObject geometry = results.get("geometry").getAsJsonObject();
        JsonObject location = geometry.get("location").getAsJsonObject();

        return String.valueOf(location.get("lat")) + "," + String.valueOf(location.get("lng"));
    }


    public static String retrieveGeocoderApiResponseFromUrl(URL url) throws IOException {
        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();
        connection.connect();

        StringBuilder response = new StringBuilder();
        Scanner s = null;
            s = new Scanner(connection.getInputStream());

            if (connection.getResponseCode() != 200) {
                System.out.println("Error ResponseCode: " + connection.getResponseCode());
                return null;
            }

            while (s.hasNext()) {
                String next = s.nextLine();
                response.append(next);
            }
        if (s != null) {
            s.close();
        }
        if (connection != null) {
            connection.disconnect();
        }
        return response.toString();
    }
    //Location[gps 37.421998,-122.084000 hAcc=20 et=+1h16m59s755ms alt=0.0 vAcc=??? sAcc=??? bAcc=??? {Bundle[mParcelledData.dataSize=96]}]
    public static String parseCoordinatesFromLocation(Location location) {
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

    public static URL buildDefaultGeocoderApiUrl() {
        try {
            return URI.create(GEOCODER_API_BASE_URL + DEFAULT_CITY + "," +
                    DEFAULT_COUNTRY + API_KEY_QUERY).toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

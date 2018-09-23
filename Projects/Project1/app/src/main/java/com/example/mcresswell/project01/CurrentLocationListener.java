package com.example.mcresswell.project01;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class CurrentLocationListener implements LocationListener {

    private static final String LOG = DashboardActivity.class.getSimpleName();

    @Override
    public void onLocationChanged(Location loc) {
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}

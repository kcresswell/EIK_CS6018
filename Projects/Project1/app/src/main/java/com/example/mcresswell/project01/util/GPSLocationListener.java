package com.example.mcresswell.project01.util;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class GPSLocationListener implements LocationListener {

    private static final String LOG = GPSLocationListener.class.getSimpleName();

    @Override
    public void onLocationChanged(Location location) {
        Log.d(LOG, Constants.LOCATION_CHANGED);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

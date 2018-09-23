package com.example.mcresswell.project01;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mcresswell.project01.util.LocationUtils;

public class DashboardActivity extends AppCompatActivity
        implements ProfileEntryFragment.OnDataChannel, View.OnClickListener {

    private static final String LOG = DashboardActivity.class.getSimpleName();

    //member variables
    private FragmentTransaction m_fTrans;
    private LocationListener locationListener;
    private Location currentLocation;

    private Button hikingButton;
    private Button weatherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //If not saved instance state, build initial fragment
        if (savedInstanceState == null) {
            //create fragment
//            ProfileEntryFragment frag_profileEntry = new ProfileEntryFragment();
            DashboardFragment frag_dashboard = new DashboardFragment();

            //present fragment to display
            m_fTrans = getSupportFragmentManager().beginTransaction();
            m_fTrans.replace(R.id.fl_master_nd, frag_dashboard, "v_frag_dashboard");
//            m_fTrans.replace(R.id.fl_master_nd, frag_profileEntry, "v_frag_profile_entry");
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }

        hikingButton = findViewById(R.id.dashboard_button_hiking);
        weatherButton = findViewById(R.id.dashboard_button_weather);

        hikingButton.setOnClickListener(this);
        weatherButton.setOnClickListener(this);

    }

    private void configureLocationSettings() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new CurrentLocationListener();
        //Check to make sure the activity has permission to access location
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG, "Permission not granted, requesting permission");

            //Request permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 50, locationListener);
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        //TODO: make sure this is actually retrieving the real time location
    }

    @Override
    public void onDataPass(String fname, String lname, int age, Bitmap image) {

    }

    boolean isWideDisplay(){
        return getResources().getBoolean(R.bool.isWideDisplay);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.dashboard_button_hiking: {
                Log.d(LOG, "onClick Hiking");
                configureLocationSettings();
                Log.d(LOG, "Last Known Coordinates: " + LocationUtils.getLocationCoordinates(currentLocation));
                Uri searchUri = Uri.parse("geo:" + LocationUtils.getLocationCoordinates(currentLocation) + "?q=hikes");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, searchUri);
                if(mapIntent.resolveActivity(getPackageManager())!=null){
                    startActivity(mapIntent);
                }
                break;
            }
            case R.id.dashboard_button_weather: {
                configureLocationSettings();

                Log.d(LOG, "onClick Weather");
                Intent intent = new Intent(this, WeatherActivity.class);
                intent.putExtra("location", currentLocation.toString());
                startActivity(intent);
                break;
            }
        }

    }
}

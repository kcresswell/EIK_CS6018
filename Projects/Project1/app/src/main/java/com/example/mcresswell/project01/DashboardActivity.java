package com.example.mcresswell.project01;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.mcresswell.project01.userProfile.UserProfile;
import com.example.mcresswell.project01.userProfile.UserProfileViewModel;
import com.example.mcresswell.project01.util.GeocoderLocationUtils;
import com.example.mcresswell.project01.util.ValidationUtils;
import com.example.mcresswell.project01.weather.WeatherForecast;
import com.example.mcresswell.project01.weather.WeatherFragment;

import java.io.IOException;

public class DashboardActivity extends AppCompatActivity implements
        ProfileEntryFragment.OnProfileEntryDataChannel,
        RV_Adapter.OnAdapterDataChannel,
        WeatherFragment.OnWeatherDataLoadedListener {

    private final String LOG = getClass().getSimpleName();

    //member variables
    private final String DEFAULT_COORDINATES = "40.7608,-111.8910";
    private final String DEFAULT_CITY = "PROVO";
    private final String DEFAULT_COUNTRY_CODE = "US";
    private FragmentTransaction m_fTrans;
    private UserProfileViewModel userProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if(savedInstanceState == null){
            restoreDefaultDashboardView();
        }
    }

    @Override
    public void onProfileEntryDataPass(Bundle userDataBundle) {

    }

    @Override
    public void onAdapterDataPass(int position) {
        if (isWideDisplay()) {
            executeTabletDashboardButtonHandler(position);
            return;
        }
        executeMobileDashboardButtonHandler(position);
    }

    private void executeTabletDashboardButtonHandler(int buttonPosition) {
        m_fTrans = getSupportFragmentManager().beginTransaction();
        switch (buttonPosition) {
            case 0: //FitnessDetails
                m_fTrans.replace(R.id.fl_detail_wd, new FitnessDetailsFragment());
                m_fTrans.addToBackStack(null);
                m_fTrans.commit();
                break;
            case 1: //Hiking
                hikingButtonHandler();
                break;
            case 2: //User Profile
                m_fTrans.replace(R.id.fl_detail_wd, new ProfileSummaryFragment());
                m_fTrans.addToBackStack(null);
                m_fTrans.commit();
                break;
            case 3: //Weather
                weatherButtonHandler(DEFAULT_CITY, DEFAULT_COUNTRY_CODE);
                break;
            default:
        }
    }

    private void executeMobileDashboardButtonHandler(int buttonPosition) {
//        Toast.makeText(this, "Position: " + buttonPosition, Toast.LENGTH_SHORT).show();
        switch (buttonPosition) {
            case 0: //FitnessDetails
                fitnessButtonHandler();
                break;
            case 1: //Hiking
                hikingButtonHandler();
                break;
            case 2: //User Profile
                profileButtonHandler();
                break;
            case 3: //Weather
                weatherButtonHandler(DEFAULT_CITY, DEFAULT_COUNTRY_CODE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            restoreDefaultDashboardView();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Helper method to restore the default app view to
     * make sure user doesn't end up with a blank app screen.
     * from pressing back repeatedly in the app.
     */
    private void restoreDefaultDashboardView() {
        m_fTrans = getSupportFragmentManager().beginTransaction();
        DashboardFragment frag_dashboard = new DashboardFragment();

        if(isWideDisplay()){ //Tablet default: master fragment left, detail fragment right
            m_fTrans.replace(R.id.fl_master_wd, frag_dashboard, "v_frag_dashboard");
            m_fTrans.replace(R.id.fl_detail_wd, new FitnessDetailsFragment(), "v_frag_fitness");
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        } else { //Mobile default
            m_fTrans.replace(R.id.fl_master_nd, frag_dashboard, "v_frag_dashboard");
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    private void fitnessButtonHandler() {
        Intent intent = new Intent(this, FitnessDetailsActivity.class);
        startActivity(intent);
    }

    private void profileButtonHandler() {
        Intent intent = new Intent(this, ProfileDetailsActivity.class);
        startActivity(intent);
    }

    private void hikingButtonHandler() {
        String coords = null;
        if (userProfileViewModel == null) {
            coords = DEFAULT_COORDINATES;
        } else {
            UserProfile user = userProfileViewModel.getUserProfile().getValue();
            try {
                coords = GeocoderLocationUtils.getCoordinatesFromCityCountry(user.getM_city(), user.getM_country());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Uri searchUri = Uri.parse("geo:" + coords + "?q=hikes");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, searchUri);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void weatherButtonHandler(String city, String country) {
        if (!isWideDisplay()) { //Load WeatherActivity in mobile
            Log.d(LOG, "weatherButtonHanlder mobileView");
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("city",
                    !ValidationUtils.isNotNullOrEmpty(city) ? DEFAULT_CITY : city);
            intent.putExtra("country",
                    !ValidationUtils.isNotNullOrEmpty(country) ? DEFAULT_COUNTRY_CODE : country);
            startActivity(intent);
        } else { //Tablet
            Log.d(LOG, "weatherButtonHanlder tabletView");
            getIntent().putExtra("city", city);
            getIntent().putExtra("country", country);

            WeatherFragment weatherFragment = WeatherFragment.newInstance(city, country);

            m_fTrans = getSupportFragmentManager().beginTransaction();
            m_fTrans.replace(R.id.fl_detail_wd, weatherFragment).setTransition(5);
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    @Override
    public void onWeatherDataLoaded(WeatherForecast forecast) {
        Log.d(LOG, "onWeatherDataLoaded");
        if (isWideDisplay()) {
            FragmentManager manager = getSupportFragmentManager();
            m_fTrans = manager.beginTransaction();
            WeatherFragment fragment = (WeatherFragment) manager.findFragmentById(R.id.fl_detail_wd);
            m_fTrans.replace(R.id.fl_detail_wd, fragment).setTransition(10);
            m_fTrans.commit();
        }
    }

    private boolean isWideDisplay(){
        return getResources().getBoolean(R.bool.isWideDisplay);
    }
}
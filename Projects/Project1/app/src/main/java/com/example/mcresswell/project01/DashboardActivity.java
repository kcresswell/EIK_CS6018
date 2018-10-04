package com.example.mcresswell.project01;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.userProfile.UserProfile;
import com.example.mcresswell.project01.util.GeocoderLocationUtils;
import com.example.mcresswell.project01.util.ValidationUtils;
import com.example.mcresswell.project01.weather.WeatherForecast;
import com.example.mcresswell.project01.weather.WeatherFragment;

import java.io.IOException;

public class DashboardActivity extends AppCompatActivity implements
        ProfileSummaryFragment.OnProfileSummaryInteractionListener,
        ProfileEntryFragment.OnProfileEntryFragmentListener,
        RV_Adapter.OnAdapterDataChannel,
        WeatherFragment.OnWeatherDataLoadedListener {

    private final String LOG = getClass().getSimpleName();

    //member variables
    private final String DEFAULT_COORDINATES = "40.7608,-111.8910";
    private final String DEFAULT_CITY = "PROVO";
    private final String DEFAULT_COUNTRY_CODE = "US";
    private FragmentTransaction m_fTrans;
    private UserProfile m_userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if(savedInstanceState == null){
            restoreDefaultDashboardView();
        }
    }

    @Override
    public void onAdapterDataPass(int position) {
        executeDashboardButtonHandler(position);
    }

    private void executeDashboardButtonHandler(int buttonPosition) {
        if (isWideDisplay()){
            m_fTrans = getSupportFragmentManager().beginTransaction();
        }
        switch (buttonPosition) {
            case 0: //FitnessDetails
                fitnessDetailsButtonHandler();
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

    private void fitnessDetailsButtonHandler() {
        if(!isWideDisplay()) { //mobile
            Intent intent = new Intent(this, FitnessDetailsActivity.class);
            if (m_userProfile != null) {
                intent.putExtra("profile", m_userProfile);
            }
            startActivity(intent);
        } else { //Tablet
            FitnessDetailsFragment fitnessDetailsFragment =
                    m_userProfile == null ? new FitnessDetailsFragment() :
                            FitnessDetailsFragment.newInstance(m_userProfile);
            m_fTrans.replace(R.id.fl_detail_wd, fitnessDetailsFragment);
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    private void hikingButtonHandler() {
        String coords = null;
        if (m_userProfile == null) {
            coords = DEFAULT_COORDINATES;
        } else {
            try {
                coords =
                        GeocoderLocationUtils.getCoordinatesFromCityCountry(
                                m_userProfile.getM_city(), m_userProfile.getM_country());
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

    private void profileButtonHandler() {
        if(!isWideDisplay()) { //mobile
            Intent intent = new Intent(this, ProfileSummaryActivity.class);
            startActivity(intent);
        } else { //Tablet
            m_fTrans.replace(R.id.fl_detail_wd, new ProfileSummaryFragment());
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    //weather button behavior
    private void weatherButtonHandler(String city, String country) {
        if (!isWideDisplay()) { //Load WeatherActivity in mobile
            Log.d(LOG, "weatherButtonHanlder mobileView");
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("city",
                    !ValidationUtils.isValidCity(city) ? DEFAULT_CITY : city);
            intent.putExtra("country",
                    !ValidationUtils.isValidCountryCode(country) ? DEFAULT_COUNTRY_CODE : country);
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

    //helper functions //////////////////////////////////////////////////
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

    /**
     * Helper method to restore the default app view to
     * make sure user doesn't end up with a blank app screen.
     * from pressing back repeatedly in the app.
     */
    private void restoreDefaultDashboardView() {
        m_fTrans = getSupportFragmentManager().beginTransaction();
        DashboardFragment frag_dashboard = new DashboardFragment();

        if(!isWideDisplay()){ //Mobile default
            m_fTrans.replace(R.id.fl_master_nd, frag_dashboard, "v_frag_dashboard");
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        } else { //Tablet default: master fragment left, detail fragment right
            m_fTrans.replace(R.id.fl_master_wd, frag_dashboard, "v_frag_dashboard");

            FitnessDetailsFragment fitnessDetailsFragment =
                    m_userProfile != null ? FitnessDetailsFragment.newInstance(m_userProfile) :
                            new FitnessDetailsFragment();

            m_fTrans.replace(R.id.fl_detail_wd, fitnessDetailsFragment, "v_frag_fitness");
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    @Override
    public void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            restoreDefaultDashboardView();
//        } else {
//            super.onBackPressed();
//        }
    }

    private boolean isWideDisplay(){
        return getResources().getBoolean(R.bool.isWideDisplay);
    }

    @Override
    public void onProfileEntryDataEntered_DoneButtonOnClick(UserProfile profile) {
        m_userProfile = profile;
    }

    @Override
    public void onProfileEntryDataPass_DoneButtonClicked(boolean isClicked) {

        //FIXME: THIS EXTRA INTERFACE METHOD IS NOT NEEDED. SEE onProfileEntryDataEntered_DoneButtonOnClick()
        //nothing to implement for this class. This indicates we need to clean up the code structure
    }

    @Override
    public void onProfileSummaryEditButton(UserProfile profile) {
        //Do stuff with the user profile. This seems to be something that we will need to remove
        //once the view model and repository are fully working. As we should not have interfaces anymore.
    }
}
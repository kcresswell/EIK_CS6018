package com.example.mcresswell.project01.Activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.RV_Adapter;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.fragments.DashboardFragment;
import com.example.mcresswell.project01.fragments.FitnessDetailsFragment;
import com.example.mcresswell.project01.fragments.ProfileEntryFragment;
import com.example.mcresswell.project01.fragments.ProfileSummaryFragment;
import com.example.mcresswell.project01.util.SampleProfileData;
import com.example.mcresswell.project01.weather.WeatherClient;
import com.example.mcresswell.project01.weather.WeatherForecast;
import com.example.mcresswell.project01.fragments.WeatherFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.mcresswell.project01.util.GeocoderLocationUtils.DEFAULT_COORDINATES;
import static com.example.mcresswell.project01.util.GeocoderLocationUtils.getCoordinatesFromCityCountry;

public class DashboardActivity extends AppCompatActivity implements
        ProfileSummaryFragment.OnProfileSummaryInteractionListener,
        ProfileEntryFragment.OnProfileEntryFragmentListener,
        RV_Adapter.OnAdapterDataChannel,
        WeatherFragment.OnWeatherDataLoadedListener {

    private final String LOG = getClass().getSimpleName();
    private List<FitnessProfile> fitnessProfilesData = new ArrayList<>();

    //member variables
    private FragmentTransaction m_fTrans;
    private FitnessProfile m_fitnessProfile;

    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        if (savedInstanceState != null) {
            m_fitnessProfile = getIntent().getParcelableExtra("profile");
        } else {
            restoreDefaultDashboardView();
        }

        fitnessProfilesData.addAll(SampleProfileData.getUserProfiles());
    }


    //response to dashboard button click based on position of RecyclerView Button clicked.
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
                weatherButtonHandler();
                break;
        }
    }

    private void fitnessDetailsButtonHandler() {
        if(!isWideDisplay()) { //mobile
            Intent intent = new Intent(this, FitnessDetailsActivity.class);
//            if (m_fitnessProfile != null) {
//                intent.putExtra("profile", m_fitnessProfile);
//            }
            startActivityForResult(intent, Activity.RESULT_OK);
        } else { //Tablet
            m_fTrans.replace(R.id.fl_detail_wd, FitnessDetailsFragment.newInstance(m_fitnessProfile));
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    private void hikingButtonHandler() {
        String coords = null;
        if (m_fitnessProfile == null) {
            coords = DEFAULT_COORDINATES;
        } else {
            try {
                coords = getCoordinatesFromCityCountry(m_fitnessProfile.getM_city(), m_fitnessProfile.getM_country());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Uri searchUri = Uri.parse("geo:" + coords + "?q=hikes");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, searchUri);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(mapIntent, Activity.RESULT_OK);
        }
    }

    private void profileButtonHandler() {
        if(!isWideDisplay()) { //mobile
            Intent intent = new Intent(this, ProfileSummaryActivity.class);
//            if (m_fitnessProfile != null) {
//                intent.putExtra("profile", m_fitnessProfile);
//            }
            startActivityForResult(intent, Activity.RESULT_OK);
        } else { //Tablet
            m_fTrans.replace(R.id.fl_detail_wd, ProfileSummaryFragment.newInstance(m_fitnessProfile));
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    //weather button behavior
    private void weatherButtonHandler() {
        String city = WeatherClient.DEFAULT_CITY;
        String country = WeatherClient.DEFAULT_COUNTRY;
         if (m_fitnessProfile != null) {
            Log.d(LOG, "weatherButtonHandler: at least the user profile object has data ..?");
            city = m_fitnessProfile.getM_city();
            country = m_fitnessProfile.getM_country();
        }

        if (!isWideDisplay()) { //Load WeatherActivity in mobile
            Log.d(LOG, "weatherButtonHandler mobileView");
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("city", city);
            intent.putExtra("country", country);
            startActivityForResult(intent, Activity.RESULT_OK);
        } else { //Tablet
            Log.d(LOG, "weatherButtonHandler tabletView");
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

            m_fTrans.replace(R.id.fl_detail_wd,
                    FitnessDetailsFragment.newInstance(m_fitnessProfile), "v_frag_fitness");
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG, "onBackPressed");
        restoreDefaultDashboardView();
    }

    private boolean isWideDisplay(){
        return getResources().getBoolean(R.bool.isWideDisplay);
    }

    @Override
    public void onProfileEntryDataEntered_DoneButtonOnClick(FitnessProfile profile) {
        m_fitnessProfile = profile;
    }

    @Override
    public void onProfileSummaryEditButton(FitnessProfile profile) {
        //Do stuff with the user profile. This seems to be something that we will need to remove
        //once the view model and repository are fully working. As we should not have interfaces anymore.
    }
}
package com.example.mcresswell.project01.activities;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.ui.RV_Adapter;
import com.example.mcresswell.project01.viewmodel.FitnessProfileViewModel;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.db.entity.Weather;
import com.example.mcresswell.project01.fragments.DashboardFragment;
import com.example.mcresswell.project01.fragments.FitnessDetailsFragment;
import com.example.mcresswell.project01.fragments.ProfileSummaryFragment;
import com.example.mcresswell.project01.util.WeatherUtils;
import com.example.mcresswell.project01.fragments.WeatherFragment;

import java.io.IOException;

import static com.example.mcresswell.project01.util.GeocoderLocationUtils.DEFAULT_COORDINATES;
import static com.example.mcresswell.project01.util.GeocoderLocationUtils.getCoordinatesFromCityCountry;

public class DashboardActivity extends AppCompatActivity implements
//        ProfileSummaryFragment.OnProfileSummaryInteractionListener,
//        ProfileEntryFragment.OnProfileEntryFragmentListener,
        RV_Adapter.OnAdapterDataChannel,
        WeatherFragment.OnWeatherDataLoadedListener {

    private final String LOG = getClass().getSimpleName();

    private FragmentTransaction m_fTrans;
    private MutableLiveData<FitnessProfile> m_fitnessProfile;
    private FitnessProfileViewModel m_fitnessProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        restoreDefaultDashboardView();

        initViewModel();

        m_fitnessProfile = m_fitnessProfileViewModel.getFitnessProfile();

    }

    private void initViewModel() {
//        final Observer<FitnessProfile> fitnessProfileObserver = fitnessProfile -> m_fitnessProfile.setValue(fitnessProfile);
        m_fitnessProfileViewModel = ViewModelProviders.of(this).get(FitnessProfileViewModel.class);
//        m_fitnessProfileViewModel.getFitnessProfile().observe(this, fitnessProfileObserver);
    }

    @Override
    public void onAdapterDataPass(int position) {
        executeDashboardButtonHandler(position);
    }

    private void executeDashboardButtonHandler(int buttonPosition) {
        if (isWideDisplay()) {
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
        if (!isWideDisplay()) {
            Intent intent = new Intent(this, FitnessDetailsActivity.class);
            startActivityForResult(intent, Activity.RESULT_OK);
        } else {
            m_fTrans.replace(R.id.fl_detail_wd, new FitnessDetailsFragment());
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
                coords = getCoordinatesFromCityCountry(m_fitnessProfile.getValue().getM_city(), m_fitnessProfile.getValue().getM_country());
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
        if (!isWideDisplay()) { //mobile
            Intent intent = new Intent(this, ProfileSummaryActivity.class);
            startActivity(intent);
        } else { //Tablet
            m_fTrans.replace(R.id.fl_detail_wd, new ProfileSummaryFragment());
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    private void weatherButtonHandler() {
        String city = WeatherUtils.DEFAULT_CITY;
        String country = WeatherUtils.DEFAULT_COUNTRY;
        if (m_fitnessProfile.getValue() != null) {
            Log.d(LOG, "weatherButtonHandler: at least the user profile object has data ..?");
            city = m_fitnessProfile.getValue().getM_city();
            country = m_fitnessProfile.getValue().getM_country();
        }

        if (!isWideDisplay()) { //Load WeatherActivity in mobile
            Log.d(LOG, "weatherButtonHandler mobileView");
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("city", city);
            intent.putExtra("country", country);
            startActivityForResult(intent, Activity.RESULT_OK);
        } else { //Tablet
            Log.d(LOG, "weatherButtonHandler tabletView");

            WeatherFragment weatherFragment = WeatherFragment.newInstance();

            m_fTrans = getSupportFragmentManager().beginTransaction();
            m_fTrans.replace(R.id.fl_detail_wd, weatherFragment).setTransition(5);
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    @Override
    public void onWeatherDataLoaded(Weather forecast) {
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

        if (!isWideDisplay()) {
            m_fTrans.replace(R.id.fl_master_nd, frag_dashboard, "v_frag_dashboard");
//            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        } else { //Tablet default: master fragment left, detail fragment right
            m_fTrans.replace(R.id.fl_master_wd, frag_dashboard, "v_frag_dashboard");
//            m_fTrans.replace(R.id.fl_detail_wd, FitnessDetailsFragment.newInstance(m_fitnessProfile.getValue()), "v_frag_fitness");
            m_fTrans.replace(R.id.fl_detail_wd, new FitnessDetailsFragment(), "v_frag_fitness");
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG, "onBackPressed");
        restoreDefaultDashboardView();
    }

    private boolean isWideDisplay() {
        return getResources().getBoolean(R.bool.isWideDisplay);
    }

}
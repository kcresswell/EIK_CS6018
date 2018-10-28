package com.example.mcresswell.project01.activities;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.ui.RV_Adapter;
import com.example.mcresswell.project01.util.GeocoderLocationUtils;
import com.example.mcresswell.project01.viewmodel.FitnessProfileViewModel;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.fragments.DashboardFragment;
import com.example.mcresswell.project01.fragments.FitnessDetailsFragment;
import com.example.mcresswell.project01.fragments.ProfileSummaryFragment;
import com.example.mcresswell.project01.fragments.WeatherFragment;
import com.example.mcresswell.project01.viewmodel.UserViewModel;
import com.example.mcresswell.project01.viewmodel.WeatherViewModel;

import static com.example.mcresswell.project01.util.Constants.ON_CLICK;
import static com.example.mcresswell.project01.util.GeocoderLocationUtils.DEFAULT_COORDINATES;
import static com.example.mcresswell.project01.util.ValidationUtils.isNotNullOrEmpty;

public class DashboardActivity extends AppCompatActivity implements RV_Adapter.OnAdapterDataChannel
{

    private final static String LOG_TAG = DashboardActivity.class.getSimpleName();

    private FragmentTransaction m_fTrans;
    private FitnessProfile m_fitnessProfile;
    private FitnessProfileViewModel m_fitnessProfileViewModel;
    private WeatherViewModel weatherViewModel;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        restoreDefaultDashboardView();

        initializeViewModels();

    }

    private void initializeViewModels() {
        m_fitnessProfileViewModel =
                ViewModelProviders.of(this).get(FitnessProfileViewModel.class);
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
//        userViewModel.getUser().observe(this, user -> {
//            if (user != null) {
//                m_fitnessProfileViewModel.getFitnessProfile(user.getId()).observe(this, fp -> {
//                    if (fp != null) {
//
//                        Log.d(LOG_TAG, String.format("Loading weather for %s, %s", fp.getM_city(), fp.getM_country()));
//
//                        weatherViewModel.loadWeather(fp.getM_city(), fp.getM_country());
//                    }
//                });
//            }
//        });
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
                fitnessProfileButtonHandler();
                break;
            case 3: //Weather
                weatherButtonHandler();
                break;
        }
    }

    private void fitnessDetailsButtonHandler() {
        Log.d(LOG_TAG, "fitnessDetails " + ON_CLICK);
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
        Log.d(LOG_TAG, "Hiking " + ON_CLICK);

        userViewModel.getUser().observe(this, user -> {
            if (user != null) {
                m_fitnessProfileViewModel.getFitnessProfile(user.getId()).observe(this, fp -> {
                    if (fp != null) {
                        String coords = GeocoderLocationUtils.asyncFetchCoordinatesFromApi(fp.getM_city(), fp.getM_country());
                        if (!isNotNullOrEmpty(coords)) {
                            coords = DEFAULT_COORDINATES;
                        }
                        Uri searchUri = Uri.parse("geo:" + coords + "?q=hikes");
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, searchUri);
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                    }
                });
            }
        });
    }

    private void fitnessProfileButtonHandler() {
        Log.d(LOG_TAG, ON_CLICK);

        if (!isWideDisplay()) {
            Intent intent = new Intent(this, ProfileSummaryActivity.class);
            startActivity(intent);
        } else { //Tablet
            m_fTrans.replace(R.id.fl_detail_wd, new ProfileSummaryFragment());
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }



    }

    private void weatherButtonHandler() {
        Log.d(LOG_TAG, ON_CLICK);

        if (!isWideDisplay()) {
            Log.d(LOG_TAG, "weatherButtonHandler mobileView");
            Intent intent = new Intent(this, WeatherActivity.class);
            startActivity(intent);

        } else { //Tablet
            Log.d(LOG_TAG, "weatherButtonHandler tabletView");
            FragmentManager manager = getSupportFragmentManager();
            m_fTrans = manager.beginTransaction();
            WeatherFragment fragment = (WeatherFragment) manager.findFragmentById(R.id.fl_detail_wd);

            m_fTrans.replace(R.id.fl_detail_wd,
                    fragment == null ? WeatherFragment.newInstance() : fragment).setTransition(15);
//                    m_fTrans.addToBackStack(null);
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
            m_fTrans.commit();
        } else { //Tablet default: master fragment left, detail fragment right
            m_fTrans.replace(R.id.fl_master_wd, frag_dashboard, "v_frag_dashboard");
            m_fTrans.replace(R.id.fl_detail_wd, new FitnessDetailsFragment(), "v_frag_fitness");
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "onBackPressed");
        restoreDefaultDashboardView();
    }

    private boolean isWideDisplay() {
        return getResources().getBoolean(R.bool.isWideDisplay);
    }


}
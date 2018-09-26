package com.example.mcresswell.project01;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.mcresswell.project01.util.ValidationUtils;

public class DashboardActivity extends AppCompatActivity implements
        ProfileEntryFragment.OnProfileEntryDataChannel, RV_Adapter.OnAdapterDataChannel {

    //member variables
    private final String DEFAULT_COORDINATES = "40.7608,-111.8910";
    private final String DEFAULT_CITY = "PROVO";
    private final String DEFAULT_COUNTRY_CODE = "US";
    private FragmentTransaction m_fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //If not saved instance state, build initial fragment
        if(savedInstanceState == null){
            //create fragment
//            ProfileEntryFragment frag_profileEntry = new ProfileEntryFragment();
            DashboardFragment frag_dashboard = new DashboardFragment();
            FitnessDetailsFragment frag_fitness = new FitnessDetailsFragment();

            m_fTrans = getSupportFragmentManager().beginTransaction();

            if(isWideDisplay()){
                //present fragment to display
                m_fTrans.replace(R.id.fl_master_wd, frag_dashboard, "v_frag_dashboard"); //master fragment left
                m_fTrans.replace(R.id.fl_detail_wd, frag_fitness, "v_frag_fitness"); //detail fragment right default is fitness
                m_fTrans.addToBackStack(null);
                m_fTrans.commit();
            } else {
                //present fragment to display
                m_fTrans = getSupportFragmentManager().beginTransaction();
                m_fTrans.replace(R.id.fl_master_nd, frag_dashboard, "v_frag_dashboard");
                m_fTrans.addToBackStack(null);
                m_fTrans.commit();
            }
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
                hikingButtonHandler(DEFAULT_COORDINATES);
                break;
            case 2: //User Profile
                m_fTrans.replace(R.id.fl_detail_wd, new ProfileSummaryFragment());
                m_fTrans.addToBackStack(null);
                m_fTrans.commit();
                break;
            case 3: //Weather
                weatherButtonHandler();
                break;
            default:
        }
    }

    private void executeMobileDashboardButtonHandler(int buttonPosition) {
//        Toast.makeText(this, "Position: " + buttonPosition, Toast.LENGTH_SHORT).show();
        switch (buttonPosition) {
            case 0: //FitnessDetails
                fitnesButtonHandler();
                break;
            case 1: //Hiking
                hikingButtonHandler(DEFAULT_COORDINATES);
                break;
            case 2: //User Profile
                profileButtonHandler();
                break;
            case 3: //Weather
                weatherButtonHandler(DEFAULT_CITY, DEFAULT_COUNTRY_CODE);
                break;
        }
    }

    private void fitnesButtonHandler() {
        Intent intent = new Intent(this, FitnessActivity.class);
        startActivity(intent);
    }

    private void profileButtonHandler() {
        Intent intent = new Intent(this, ProfileDetailsActivity.class);
        startActivity(intent);
    }

    private void hikingButtonHandler(String coordinates) {
        Uri searchUri = Uri.parse("geo:" + coordinates + "?q=hikes");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, searchUri);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    private void weatherButtonHandler(String city, String country) {
        if (!isWideDisplay()) {
            Intent intent = new Intent(this, WeatherActivity.class);
            intent.putExtra("city",
                    !ValidationUtils.isNotNullOrEmpty(city) ? DEFAULT_CITY : city);
            intent.putExtra("country",
                    !ValidationUtils.isNotNullOrEmpty(country) ? DEFAULT_COUNTRY_CODE : country);
            startActivity(intent);
        } else {
            getIntent().putExtra("city", city);
            getIntent().putExtra("country", country);
            //TODO: Handle tablet logic

            m_fTrans.replace(R.id.fl_detail_wd, new WeatherFragment());
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    private boolean isWideDisplay(){
        return getResources().getBoolean(R.bool.isWideDisplay);
    }

}
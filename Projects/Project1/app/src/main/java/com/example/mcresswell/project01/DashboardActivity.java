package com.example.mcresswell.project01;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity implements ProfileEntryFragment.OnDataChannel, RV_Adapter.OnDataChannel {

    //memeber variables
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


            if(isWideDisplay()){
                //present fragment to display
                m_fTrans = getSupportFragmentManager().beginTransaction();
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
    public void onDataPass(String fname, String lname, int age, Bitmap image) {

    }

    boolean isWideDisplay(){
        return getResources().getBoolean(R.bool.isWideDisplay);
    }

    @Override
    public void onDataPass(int position) {
        if(isWideDisplay()){
            //begin fragment transaction
            m_fTrans = getSupportFragmentManager().beginTransaction();

            switch (position) {
                case 0:
                    FitnessDetailsFragment frag_fitness = new FitnessDetailsFragment();
                    m_fTrans.replace(R.id.fl_detail_wd, frag_fitness, "v_frag_fitness");
                    m_fTrans.addToBackStack(null);
                    break;
                case 1:
                    HikingFragment frag_hike = new HikingFragment();
                    m_fTrans.replace(R.id.fl_detail_wd, frag_hike, "v_frag_hike");
                    m_fTrans.addToBackStack(null);
                    break;
                case 2:
                    ProfileSummaryFragment frag_profileDetails = new ProfileSummaryFragment();
                    m_fTrans.replace(R.id.fl_detail_wd, frag_profileDetails, "v_frag_profile_details");
                    m_fTrans.addToBackStack(null);
                    break;
                case 3:
                    WeatherFragment frag_weather = new WeatherFragment();
                    m_fTrans.replace(R.id.fl_detail_wd, frag_weather, "v_frag_weather");
                    m_fTrans.addToBackStack(null);
                    break;
                default:

            }

            //commit to fragment transaction
            m_fTrans.commit();
        } else {

            switch (position) {
                case 0:
                    Toast.makeText(this, "Position: " + position, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(this, "Position: " + position, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(this, "Position: " + position, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(this, "Position: " + position, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(this, "Unused Position: " + position, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
package com.example.mcresswell.project01;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DashboardActivity extends AppCompatActivity implements ProfileEntryFragment.OnDataChannel {

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

            //present fragment to display
            m_fTrans = getSupportFragmentManager().beginTransaction();
            m_fTrans.replace(R.id.fl_master_nd, frag_dashboard, "v_frag_dashboard");
//            m_fTrans.replace(R.id.fl_master_nd, frag_profileEntry, "v_frag_profile_entry");
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    @Override
    public void onDataPass(String fname, String lname, int age, Bitmap image) {

    }

    boolean isWideDisplay(){
        return getResources().getBoolean(R.bool.isWideDisplay);
    }
}

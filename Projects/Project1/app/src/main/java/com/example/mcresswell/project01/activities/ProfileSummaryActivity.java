package com.example.mcresswell.project01.activities;

import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.fragments.ProfileSummaryFragment;
import com.example.mcresswell.project01.util.Constants;

import static com.example.mcresswell.project01.util.Constants.BACK_PRESSED;

public class ProfileSummaryActivity extends AppCompatActivity {

    private final String LOG = getClass().getSimpleName();

    private FragmentTransaction m_fTrans;
    private MutableLiveData<FitnessProfile> m_fitnessProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_summary);
        loadProfileSummaryFragment();
    }

    private void loadProfileSummaryFragment() {
        m_fTrans = getSupportFragmentManager().beginTransaction();
        m_fTrans.replace(R.id.fl_activity_profile_details, new ProfileSummaryFragment(), "v_frag_profile");
        m_fTrans.commit();
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG, BACK_PRESSED);
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

}
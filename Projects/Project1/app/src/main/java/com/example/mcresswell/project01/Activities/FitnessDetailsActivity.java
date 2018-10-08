package com.example.mcresswell.project01.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.fragments.FitnessDetailsFragment;

public class FitnessDetailsActivity extends AppCompatActivity {

    private static final String LOG = FitnessDetailsActivity.class.getSimpleName();
    private FragmentTransaction m_fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        FitnessProfile profile = null;
        if (savedInstanceState != null) {
            profile = getIntent().getParcelableExtra("profile");
            setResult(Activity.RESULT_OK, getIntent());
        }

        //present fragment to display
        m_fTrans = getSupportFragmentManager().beginTransaction();
        m_fTrans.replace(R.id.fl_master_nd_activity_fitness, FitnessDetailsFragment.newInstance(profile), "v_frag_dashboard");
        m_fTrans.commit();
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG, "onBackPressed");

        Intent intent = new Intent(this, DashboardActivity.class);

        startActivity(intent);
    }
}
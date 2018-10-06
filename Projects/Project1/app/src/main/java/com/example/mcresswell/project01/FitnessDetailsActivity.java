package com.example.mcresswell.project01;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.mcresswell.project01.db.entity.UserProfile;

public class FitnessDetailsActivity extends AppCompatActivity {
    private FragmentTransaction m_fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        UserProfile profile = null;
        if (savedInstanceState != null) {
            profile = getIntent().getParcelableExtra("profile");
        }

        //present fragment to display
        m_fTrans = getSupportFragmentManager().beginTransaction();
        m_fTrans.replace(R.id.fl_master_nd_activity_fitness, FitnessDetailsFragment.newInstance(profile), "v_frag_dashboard");
        m_fTrans.commit();
    }
}
package com.example.mcresswell.project01;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.mcresswell.project01.db.entity.UserProfile;
import com.example.mcresswell.project01.fragments.FitnessDetailsFragment;

public class FitnessDetailsActivity extends AppCompatActivity {
    private FragmentTransaction m_fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        UserProfile profile = null;
        if (savedInstanceState != null) {
            profile = getIntent().getParcelableExtra("profile");
            setResult(Activity.RESULT_OK, getIntent());
        }

        //present fragment to display
        m_fTrans = getSupportFragmentManager().beginTransaction();
        m_fTrans.replace(R.id.fl_master_nd_activity_fitness, FitnessDetailsFragment.newInstance(profile), "v_frag_dashboard");
        m_fTrans.commit();
    }
}
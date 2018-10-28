package com.example.mcresswell.project01.activities;

import android.arch.lifecycle.MutableLiveData;
import android.content.Intent;
import android.arch.lifecycle.ViewModelProviders;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.viewmodel.FitnessProfileViewModel;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.fragments.FitnessDetailsFragment;

public class FitnessDetailsActivity extends AppCompatActivity {

    private static final String LOG = FitnessDetailsActivity.class.getSimpleName();
    private FragmentTransaction m_fTrans;
    private FitnessProfileViewModel m_fitnessProfileViewModel;
    private MutableLiveData<FitnessProfile> m_fitnessProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_details);

        initViewModel();

        //present fragment to display
        m_fTrans = getSupportFragmentManager().beginTransaction();
        m_fTrans.replace(R.id.fl_master_nd_activity_fitness, new FitnessDetailsFragment(), "v_frag_dashboard");
        m_fTrans.commit();
    }

    private void initViewModel() {
//        final Observer<FitnessProfile> fitnessProfileObserver = fitnessProfile -> m_fitnessProfile.setValue(fitnessProfile);
        m_fitnessProfileViewModel = ViewModelProviders.of(this)
                .get(FitnessProfileViewModel.class);
//        m_fitnessProfileViewModel.getLDFitnessProfile().observe(this, fitnessProfileObserver);
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG, "onBackPressed");
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
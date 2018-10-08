package com.example.mcresswell.project01.Activities;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.ViewModels.FitnessProfileViewModel;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.fragments.FitnessDetailsFragment;

public class FitnessDetailsActivity extends AppCompatActivity {
    private FragmentTransaction m_fTrans;
    private FitnessProfileViewModel m_fitnessProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);
        initViewModel();
        
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

    private void initViewModel() {
        m_fitnessProfileViewModel = ViewModelProviders.of(this)
                .get(FitnessProfileViewModel.class);
    }
}
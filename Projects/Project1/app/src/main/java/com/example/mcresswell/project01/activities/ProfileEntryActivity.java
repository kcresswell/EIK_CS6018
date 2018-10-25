package com.example.mcresswell.project01.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.viewmodel.FitnessProfileViewModel;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.fragments.ProfileEntryFragment;
import com.example.mcresswell.project01.fragments.ProfileSummaryFragment;
import com.example.mcresswell.project01.util.Constants;

public class ProfileEntryActivity extends AppCompatActivity {
    private final String LOG = getClass().getSimpleName();

    private FragmentTransaction m_fTrans;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_entry);

        loadProfileEntryFragment();
    }

    private void loadProfileEntryFragment() {
        m_fTrans = getSupportFragmentManager().beginTransaction();
        m_fTrans.replace(R.id.fl_activity_profile_entry, new ProfileEntryFragment());
        m_fTrans.commit();
    }

//    private void initViewModel() {
////        final Observer<FitnessProfile> fitnessProfileObserver = fitnessProfile -> m_fitnessProfile = fitnessProfile;
//        m_fitnessProfileViewModel = ViewModelProviders.of(this)
//                .get(FitnessProfileViewModel.class);
////        m_fitnessProfileViewModel.getFitnessProfile().observe(this, fitnessProfileObserver);
//    }

    @Override
    public void onBackPressed() {
        Log.d(LOG, "onBackPressed");
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

//    @Override
//    public void onProfileEntryDataEntered_DoneButtonOnClick(boolean isClicked) {
//        if (isClicked) {
//            if (!isWideDisplay()){
//                Intent intent = new Intent(this, ProfileSummaryActivity.class);
//                startActivity(intent);
//            } else {
//                m_fTrans.replace(R.id.fl_detail_wd, new ProfileSummaryFragment());
//                m_fTrans.addToBackStack(null);
//                m_fTrans.commit();
//            }
//        }
//    }

    private boolean isWideDisplay(){
        return getResources().getBoolean(R.bool.isWideDisplay);
    }
}

package com.example.mcresswell.project01.Activities;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.ViewModels.FitnessProfileViewModel;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.fragments.ProfileEntryFragment;
import com.example.mcresswell.project01.fragments.ProfileSummaryFragment;
import com.example.mcresswell.project01.util.Constants;

import static com.example.mcresswell.project01.util.FitnessProfileUtils.printUserProfileData;

public class ProfileEntryActivity extends AppCompatActivity
        implements
        ProfileEntryFragment.OnProfileEntryFragmentListener
{
    private final String LOG = getClass().getSimpleName();

    private FragmentTransaction m_fTrans;
    private MutableLiveData<FitnessProfile> m_fitnessProfile;
    private FitnessProfileViewModel m_fitnessProfileViewModel;
    private boolean m_isLoggedIn = false;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_entry);

        initViewModel();
        m_fitnessProfile = m_fitnessProfileViewModel.getFitnessProfile();

        loadFragment();
    }

    private void loadFragment() {
        m_fTrans = getSupportFragmentManager().beginTransaction();
        m_fTrans.replace(R.id.fl_activity_profile_entry, new ProfileEntryFragment());
        m_fTrans.commit();
    }

    private void initViewModel() {
//        final Observer<FitnessProfile> fitnessProfileObserver = fitnessProfile -> m_fitnessProfile.setValue(fitnessProfile);
        m_fitnessProfileViewModel = ViewModelProviders.of(this)
                .get(FitnessProfileViewModel.class);
//        m_fitnessProfileViewModel.getFitnessProfile().observe(this, fitnessProfileObserver);
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG, "onBackPressed");
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onProfileEntryDataEntered_DoneButtonOnClick(boolean isClicked) {
        if (isClicked) {
            if (!isWideDisplay()){
                Intent intent = new Intent(this, ProfileSummaryActivity.class);
                startActivity(intent);
            } else {
                m_fTrans.replace(R.id.fl_detail_wd, new ProfileSummaryFragment());
                m_fTrans.addToBackStack(null);
                m_fTrans.commit();
            }
        }
    }

    private boolean isWideDisplay(){
        return getResources().getBoolean(R.bool.isWideDisplay);
    }
}

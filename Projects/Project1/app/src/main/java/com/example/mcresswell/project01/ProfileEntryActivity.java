package com.example.mcresswell.project01;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.userProfile.UserProfile;
import com.example.mcresswell.project01.util.Constants;

public class ProfileEntryActivity extends AppCompatActivity
        implements ProfileEntryFragment.OnProfileEntryFragmentListener{
    private final String LOG = getClass().getSimpleName();

    private FragmentTransaction m_fTrans;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_entry);


        m_fTrans = getSupportFragmentManager().beginTransaction();
        ProfileEntryFragment profileEntryFragment =
                ProfileEntryFragment.newInstance(UserProfile.newTestUserProfileInstance());
//        ProfileEntryFragment profileEntryFragment = new ProfileEntryFragment();

        m_fTrans.replace(R.id.fl_activity_profile_entry, profileEntryFragment);
        m_fTrans.commit();
    }

    @Override
    public void onProfileEntryDataEntered(UserProfile profile) {
        //TODO: pass data from ProfileEntry to ProfileSummary
        Log.d(LOG, "onProfileEntryDataEntered");
        profile.printUserProfileData();

    }
}

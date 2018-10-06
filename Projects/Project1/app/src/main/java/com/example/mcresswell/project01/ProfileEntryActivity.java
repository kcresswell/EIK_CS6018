package com.example.mcresswell.project01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.userProfile.UserProfile;
import com.example.mcresswell.project01.util.Constants;

public class ProfileEntryActivity extends AppCompatActivity
        implements ProfileEntryFragment.OnProfileEntryFragmentListener,
        ProfileSummaryFragment.OnProfileSummaryInteractionListener {
    private final String LOG = getClass().getSimpleName();

    private FragmentTransaction m_fTrans;
    private UserProfile m_userProfile;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_entry);


        m_fTrans = getSupportFragmentManager().beginTransaction();
//        if (savedInstanceState != null) {
        UserProfile profile = getIntent().getParcelableExtra("profile");
//        }
        ProfileEntryFragment profileEntryFragment =
                ProfileEntryFragment.newInstance(profile);

        m_fTrans.replace(R.id.fl_activity_profile_entry, profileEntryFragment);
        m_fTrans.commit();
    }

    @Override
    public void onProfileEntryDataEntered_DoneButtonOnClick(UserProfile profile) {
        //TODO: pass data from ProfileEntry to ProfileSummary
        Log.d(LOG, "onProfileEntryDataEntered_DoneButtonOnClick");
        Intent intent = new Intent(this, ProfileSummaryActivity.class);

        if (profile != null) {
            m_userProfile = profile;
            profile.printUserProfileData();
            intent.putExtra("profile", profile);
        }
        startActivity(intent);

    }

    @Override
    public void onProfileEntryDataPass_DoneButtonClicked(boolean isClicked) {
        if(isClicked){
            m_fTrans = getSupportFragmentManager().beginTransaction();
            m_fTrans.replace(R.id.fl_activity_profile_details, new ProfileSummaryFragment(), "v_frag_profile_entry");
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    @Override
    public void onProfileSummaryEditButton(UserProfile profile) {
        Log.d(LOG, "onProfileSummaryEditButton Listener");

        if (profile != null) {
            Log.d(LOG, "Passing previously entered user data to ProfileEntryFragment");
            m_fTrans.replace(R.id.fl_activity_profile_entry, ProfileEntryFragment.newInstance(profile));
            m_fTrans.commit();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG, "onBackPressed");
        // code here to show dialog
        Intent intent = new Intent(this, DashboardActivity.class);

        if (m_userProfile != null){ //Existing profile data, transfer data
            intent.putExtra("profile", m_userProfile);
        }
        startActivity(intent);
    }
}

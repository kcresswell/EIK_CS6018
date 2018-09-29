package com.example.mcresswell.project01;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.userProfile.UserProfile;
import com.example.mcresswell.project01.util.Constants;

public class ProfileSummaryActivity extends AppCompatActivity
    implements ProfileSummaryFragment.OnProfileSummaryInteractionListener,
        ProfileEntryFragment.OnProfileEntryFragmentListener {

    private final String LOG = getClass().getSimpleName();

    private FragmentTransaction m_fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_summary);

        m_fTrans = getSupportFragmentManager().beginTransaction();

        ProfileSummaryFragment fragment =
                savedInstanceState == null ? new ProfileSummaryFragment() :
                        ProfileSummaryFragment.newInstance(getIntent().getParcelableExtra("profile"));

        m_fTrans.replace(R.id.fl_activity_profile_details, fragment, "v_frag_profile");
        m_fTrans.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onProfileSummaryEditButton(UserProfile profile) {
        Log.d(LOG, "onProfileSummaryEditButton listener");
        Intent intent = new Intent(this, ProfileEntryActivity.class);

        if (profile != null){ //Existing profile data, transfer data
            intent.putExtra("profile", profile);
        }
        startActivity(intent);



    }

    @Override
    public void onProfileEntryDataEntered(UserProfile profile) {
        Log.d(LOG, "onProfileEntryDoneButton listener, user finished adding data");
        m_fTrans = getSupportFragmentManager().beginTransaction();

        ProfileSummaryFragment fragment =
                profile == null ? new ProfileSummaryFragment() :
                        ProfileSummaryFragment.newInstance(profile);

        m_fTrans.replace(R.id.fl_activity_profile_details, fragment);
        m_fTrans.commit();
    }

    @Override
    public void onProfileEntryDataPass_DoneButtonClicked(boolean isClicked) {
        //nothing to implement for this class. This indicates we need to clean up the code structure
    }

//    @Override
//    public void onProfileEntryDataEntered(UserProfile profile) {
////            Log.d(LOG, "onProfileEntryDataEntered");
////            profile.printUserProfileData();
//
////        Intent intent = new Intent(this, ProfileSummaryFragment.new);
//    }
}
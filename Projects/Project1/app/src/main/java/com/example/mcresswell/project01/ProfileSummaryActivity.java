package com.example.mcresswell.project01;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.userProfile.UserProfile;
import com.example.mcresswell.project01.userProfile.UserProfileViewModel;
import com.example.mcresswell.project01.util.Constants;

public class ProfileSummaryActivity extends AppCompatActivity
    implements ProfileSummaryFragment.onProfileSummaryInteractionListener {

    private final String LOG = getClass().getSimpleName();

    private FragmentTransaction m_fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        m_fTrans = getSupportFragmentManager().beginTransaction();
        m_fTrans.replace(R.id.fl_activity_profile_details, new ProfileSummaryFragment(), "v_frag_profile");
        m_fTrans.commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onProfileSummaryEditButton() {

        Log.d(LOG, "onProfileSummaryEditButton listener");
        Intent intent = new Intent(this, ProfileEntryActivity.class);
        startActivity(intent);

    }
}
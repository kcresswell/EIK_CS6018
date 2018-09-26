package com.example.mcresswell.project01;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.mcresswell.project01.userProfile.UserProfileViewModel;

public class ProfileDetailsActivity extends AppCompatActivity {
    private FragmentTransaction m_fTrans;
    private UserProfileViewModel m_userProfileViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        initViewModel();

        m_fTrans = getSupportFragmentManager().beginTransaction();
        m_fTrans.replace(R.id.fl_activity_profile_details, new ProfileSummaryFragment(), "v_frag_profile");
        m_fTrans.commit();
    }

    private void initViewModel() {
        m_userProfileViewModel = ViewModelProviders.of(this)
                .get(UserProfileViewModel.class);
    }
}
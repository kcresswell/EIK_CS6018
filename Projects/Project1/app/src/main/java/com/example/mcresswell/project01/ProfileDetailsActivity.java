package com.example.mcresswell.project01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ProfileDetailsActivity extends AppCompatActivity implements ProfileEntryFragment.OnDataChannel {

    private Bundle m_userProfileBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
    }

    //Bundle comes from ProfileEntryFragment.java
    @Override
    public void onDataPass(Bundle userProfileBundle) {
        m_userProfileBundle = userProfileBundle;
    }

    public void passUserProfileDataToDashboardActivity() {
        //startActivityForResult
        //onActivityResult
        Intent intent_fromDashboardActivity = new Intent();
        intent_fromDashboardActivity.putExtra("userProfileBundle",m_userProfileBundle);
        setResult(Activity.RESULT_OK, intent_fromDashboardActivity);
        finish();
    }

}

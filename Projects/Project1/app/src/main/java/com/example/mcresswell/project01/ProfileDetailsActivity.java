package com.example.mcresswell.project01;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ProfileDetailsActivity extends AppCompatActivity implements ProfileEntryFragment.OnProfileEntryDataChannel {

    private Bundle m_userProfileBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
    }

    //Bundle comes from ProfileEntryFragment.java
    @Override
    public void onProfileEntryDataPass(Bundle userProfileBundle) {
        //create a new fragment
        ProfileEntryFragment profileEntryFragment = new ProfileEntryFragment();

        //add data to bundle and pass to fragment
//        Bundle bundle = new Bundle();
//        bundle.putString("fname", fname);
//        bundle.putString("lname", lname);
//        bundle.putInt("age", age);
//        bundle.putParcelable("BitmapImage", image); //to retrieve image: Bitmap bitmapimage = getIntent().getExtras().getParcelable("BitmapImage");

        if(getResources().getBoolean(R.bool.isWideDisplay)) {
            //is a tablet

        } else {
//            //add fitness score in to bundle
//            ArrayList<UserProfile> userProfiles = new ArrayList<UserProfile>();
//            userProfiles.add(userProfile);
//            UserProfileListParcelable userProfilesParcelable = new UserProfileListParcelable(userProfiles);
//            userProfileBundle.putParcelable("UserProfileList", userProfilesParcelable);
        }
        //intent to the dashboard, in dashboard pass intent to fitness


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

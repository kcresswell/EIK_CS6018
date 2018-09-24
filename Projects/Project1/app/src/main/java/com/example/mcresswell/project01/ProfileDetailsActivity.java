package com.example.mcresswell.project01;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class ProfileDetailsActivity extends AppCompatActivity implements ProfileEntryFragment.OnDataChannel {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
    }

    @Override
    public void onDataPass(String fname, String lname, int age, Bitmap image, UserProfile userProfile) {
        //create a new fragment
        ProfileEntryFragment profileEntryFragment = new ProfileEntryFragment();

        //add data to bundle and pass to fragment
        Bundle bundle = new Bundle();
        bundle.putString("fname", fname);
        bundle.putString("lname", lname);
        bundle.putInt("age", age);
        bundle.putParcelable("BitmapImage", image); //to retrieve image: Bitmap bitmapimage = getIntent().getExtras().getParcelable("BitmapImage");

        if(getResources().getBoolean(R.bool.isWideDisplay)) {
            //is a tablet

        } else {
            //add fitness score in to bundle
            ArrayList<UserProfile> userProfiles = new ArrayList<UserProfile>();
            userProfiles.add(userProfile);
            UserProfileListParcelable userProfilesParcelable = new UserProfileListParcelable(userProfiles);
            bundle.putParcelable("UserProfileList", userProfilesParcelable);
        }
        //intent to the dashboard, in dashboard pass intent to fitness


    }
}

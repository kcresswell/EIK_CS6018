package com.example.mcresswell.project01;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.mcresswell.project01.userProfile.UserProfileViewModel;

public class ProfileDetailsActivity extends AppCompatActivity implements ProfileEntryFragment.OnProfileEntryDataChannel {
    private FragmentTransaction m_fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);

        m_fTrans = getSupportFragmentManager().beginTransaction();
        m_fTrans.replace(R.id.fl_activity_profile_details, new ProfileSummaryFragment(), "v_frag_profile");
        m_fTrans.commit();

//        UserProfileViewModel model = ViewModelProviders.of(this).get(UserProfileViewModel.class);
//        model.getUserProfile().observe(this, users -> {
//            // update UI
//
//
//        });
    }

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
    }
}
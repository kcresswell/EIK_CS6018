package com.example.mcresswell.project01.userProfile;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.mcresswell.project01.db.FitnessProfileRepository;

import static android.support.constraint.Constraints.TAG;

public class UserProfileViewModel extends AndroidViewModel {

    private static final String LOG = UserProfileViewModel.class.getSimpleName();

    private MutableLiveData<UserProfile> userProfile = new MutableLiveData<>();
    private UserProfile profile;

    private String m_fName, m_lName, m_dob, m_sex, m_city, m_country, m_lifestyleSelection, m_weightGoal;
    private int m_Age, m_weight, m_feet, m_inches, m_lbsPerWeek;
    private double m_BMR, m_BMI;
    private int m_calsPerDay;

    private FitnessProfileRepository userProfileRepository;

    public UserProfileViewModel(@NonNull Application application) {
        super(application);
//        loadUserProfileData();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadUserProfileData() {
        new AsyncTask<UserProfile, Void, UserProfile>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected UserProfile doInBackground(UserProfile... userProfiles) {
                UserProfile profile = null;
                if (userProfiles != null){
                    Log.d(TAG, String.format("Retrieving %d userProfiles from userProfile database", userProfiles.length));
                    for (UserProfile user: userProfiles){
//                       profile = userProfileRepository.userProfileFromFitnessProfile(user.getId());
                        user.printUserProfileData();
                    }
                }
                return profile == null ? UserProfile.newTestUserProfileInstance() : profile ;

            }

            @Override
            protected void onPostExecute(UserProfile profile) {
                userProfile.setValue(profile);
            }
        }.execute(profile);
    }

    public void setUserProfile(UserProfile profile) {
        //
        userProfile.setValue(profile);
        this.profile = profile;
        loadUserProfileData();
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }
}

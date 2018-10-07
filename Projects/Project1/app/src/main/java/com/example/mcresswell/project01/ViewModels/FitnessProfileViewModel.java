package com.example.mcresswell.project01.ViewModels;

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

import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.db.repo.FitnessProfileRepository;

import static android.support.constraint.Constraints.TAG;

public class FitnessProfileViewModel extends AndroidViewModel {

    private static final String LOG = FitnessProfileViewModel.class.getSimpleName();

    private MutableLiveData<FitnessProfile> mUserProfile = new MutableLiveData<>();
    private FitnessProfile profile;

    private String m_fName, m_lName, m_dob, m_sex, m_city, m_country, m_lifestyleSelection, m_weightGoal;
    private int m_Age, m_weight, m_feet, m_inches, m_lbsPerWeek;
    private double m_BMR, m_BMI;
    private int m_calsPerDay;

    private FitnessProfileRepository userProfileRepository;

    public FitnessProfileViewModel(@NonNull Application application) {
        super(application);
//        loadUserProfileData();
    }

    @SuppressLint("StaticFieldLeak")
    private void loadUserProfileData() {
        new AsyncTask<FitnessProfile, Void, FitnessProfile>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected FitnessProfile doInBackground(FitnessProfile... fitnessProfiles) {
                FitnessProfile profile = null;
                if (fitnessProfiles != null){
                    Log.d(TAG, String.format("Retrieving %d fitnessProfiles from userProfile database", fitnessProfiles.length));
                    profile = fitnessProfiles[0];
                    if (profile != null) {
//                    for (FitnessProfile user: fitnessProfiles){
//                       profile = userProfileRepository.userProfileFromFitnessProfile(user.getId());
                        profile.printUserProfileData();
                    }
                }
//                return profile == null ? FitnessProfile.newTestUserProfileInstance() : profile ;
                return profile;

            }

            @Override
            protected void onPostExecute(FitnessProfile profile) {
                mUserProfile.setValue(profile);
            }
        }.execute(profile);
    }

    public void setUserProfile(FitnessProfile profile) {
        this.profile = profile;
        loadUserProfileData();
    }

    public LiveData<FitnessProfile> getUserProfile() {
        return mUserProfile;
    }
}

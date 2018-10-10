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
import static com.example.mcresswell.project01.util.FitnessProfileUtils.printUserProfileData;

public class FitnessProfileViewModel extends AndroidViewModel {

    private static final String LOG = FitnessProfileViewModel.class.getSimpleName();

    private MutableLiveData<FitnessProfile> m_fitnessProfile;
    private FitnessProfileRepository m_fitnessProfileRepository;

    public FitnessProfileViewModel(@NonNull Application application) {
        super(application);
        m_fitnessProfileRepository = FitnessProfileRepository.getInsance(application.getApplicationContext());
        m_fitnessProfile = m_fitnessProfileRepository.getFitnessProfileData();
    }

    public MutableLiveData<FitnessProfile> getFitnessProfile() {
        return m_fitnessProfile;
    }

    public void updateFitnessProfile(){
        m_fitnessProfileRepository.updateFitnessProfile();
    }

//    @SuppressLint("StaticFieldLeak")
//    private void loadUserProfileData() {
//        new AsyncTask<FitnessProfile, Void, FitnessProfile>() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            @Override
//            protected FitnessProfile doInBackground(FitnessProfile... fitnessProfiles) {
//                FitnessProfile profile = null;
//                if (fitnessProfiles != null){
//                    Log.d(TAG, String.format("Retrieving %d fitnessProfiles from userProfile database", fitnessProfiles.length));
//                    profile = fitnessProfiles[0];
//                    if (profile != null) {
////                    for (FitnessProfile user: fitnessProfiles){
////                       profile = userProfileRepository.userProfileFromFitnessProfile(user.getId());
//                        printUserProfileData(profile);
//                    }
//                }
////                return profile == null ? FitnessProfile.newTestUserProfileInstance() : profile ;
//                return profile;
//
//            }
//
//            @Override
//            protected void onPostExecute(FitnessProfile profile) {
//                mUserProfile.setValue(profile);
//            }
//        }.execute(profile);
//    }

//    public void setUserProfile(FitnessProfile profile) {
//        this.profile = profile;
//        loadUserProfileData();
//    }

//    public void setFitnessProfile(FitnessProfile profile) {
//        this.profile = profile;
//        loadFitnessProfileData();
//    }
//
//    public LiveData<FitnessProfile> getFitnessProfile() {
//        return mFitnessProfile;
//    }
}

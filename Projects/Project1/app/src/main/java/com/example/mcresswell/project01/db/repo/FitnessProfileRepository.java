package com.example.mcresswell.project01.db.repo;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.mcresswell.project01.db.entity.FitnessProfile;

import static android.support.constraint.Constraints.TAG;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.printUserProfileData;

public class FitnessProfileRepository {
    private final MutableLiveData<FitnessProfile> fitnessProfileData = new MutableLiveData<FitnessProfile>();
    private FitnessProfile m_fitnessProfile;

    FitnessProfileRepository(Application application){ loadData(); }

    public void setFitnessProfile(FitnessProfile fitnessProfile){
        m_fitnessProfile = fitnessProfile;
        loadData();
    }

    public MutableLiveData<FitnessProfile> getFitnessProfileData(){return fitnessProfileData;}

    private void loadData() {
        new AsyncTask<FitnessProfile, Void, FitnessProfile>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            protected FitnessProfile doInBackground(FitnessProfile... fitnessProfiles) {
                FitnessProfile fitnessProfile = null;
                if (fitnessProfiles != null){
                    Log.d(TAG, String.format("Retrieving %d fitnessProfiles from userProfile database", fitnessProfiles.length));
                    fitnessProfile = fitnessProfiles[0];
                    if (fitnessProfile != null) {
                        printUserProfileData(fitnessProfile);
                    }
                }
                return fitnessProfile;

            }

            @Override
            protected void onPostExecute(FitnessProfile profile) {
                fitnessProfileData.setValue(profile);
            }
        }.execute(m_fitnessProfile);
    }





//
//    private FitnessProfile fitnessProfile;
//
//    public static FitnessProfileRepository getInstance() {
//        return ourInstance;
//    }
//
//    private FitnessProfileRepository() {
//
//        //intialize all the member variables
//        //best to get from the Database.
//    }
//
//    public FitnessProfile getFitnessProfile() {
//        return fitnessProfile;
//    }
//
//
//    //TODO: KEEP THIS METHOD. WE'LL NEED THIS METHOD LATER TO BUILD A USER PROFILE OBJECT FROM DATABASE LOOKUP
//    public FitnessProfile userProfileFromFitnessProfile(int fitnessProfileId) {
//        //Retrieve a record from FitnessProfile with the given id
//        //Then generate a FitnessProfile object from the record retrieved
//        return null;
//    }
}

package com.example.mcresswell.project01.db.repo;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.util.SampleProfileData;

import static android.support.constraint.Constraints.TAG;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.printUserProfileData;

public class FitnessProfileRepository {
    private final MutableLiveData<FitnessProfile> fitnessProfileData = new MutableLiveData<FitnessProfile>();
    private FitnessProfile m_fitnessProfile;

    //The following three items make this a singleton
    //make the only call that applies to all accesses of the repository.
    private static final FitnessProfileRepository FPR_instance = new FitnessProfileRepository();
    //the only way to access the instance.
    public static FitnessProfileRepository getInsance() {return FPR_instance;}
    //made private to ensure that nothing can create an instance besides itself
    private FitnessProfileRepository() {
//      m_fitnessProfile = //TODO: Get data from database.
        //TODO: This is just a sample data until the database is established.
        m_fitnessProfile = SampleProfileData.getUserProfiles().get(0);
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


//    public void setFitnessProfile(FitnessProfile fitnessProfile){
//        m_fitnessProfile = fitnessProfile;
//        loadData();
//    }

//    public FitnessProfile getFitnessProfile() {
//        return fitnessProfile;
//    }

// TODO: KEEP THIS METHOD. WE'LL NEED THIS METHOD LATER TO BUILD A USER PROFILE OBJECT FROM DATABASE LOOKUP
//    public FitnessProfile userProfileFromFitnessProfile(int fitnessProfileId) {
//        //Retrieve a record from FitnessProfile with the given id
//        //Then generate a FitnessProfile object from the record retrieved
//        return null;
//    }
}

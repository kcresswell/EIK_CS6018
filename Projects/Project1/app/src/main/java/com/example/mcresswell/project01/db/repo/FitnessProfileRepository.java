package com.example.mcresswell.project01.db.repo;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import com.example.mcresswell.project01.db.InStyleDatabase;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.util.SampleProfileData;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FitnessProfileRepository {
    private MutableLiveData<FitnessProfile> fitnessProfileData;
    private InStyleDatabase m_db;
    private Executor m_executor = Executors.newSingleThreadExecutor();

    //The following three items make this a singleton
    //make the only call that applies to all accesses of the repository.
    private static FitnessProfileRepository FPR_instance;
    //the only way to access the instance.
    public static FitnessProfileRepository getInsance(Context context) {
        if (FPR_instance == null){
             FPR_instance = new FitnessProfileRepository(context);
        }
        return FPR_instance;}
    //made private to ensure that nothing can create an instance besides itself
    private FitnessProfileRepository(Context context) {
//      m_fitnessProfile = //TODO: Get data from database.
        //TODO: This is just a sample data until the database is established.
        m_db = InStyleDatabase.getDatabaseInstance(context);
        fitnessProfileData = getFitnessProfileData();
    }

    //This is where the Repository gets the data.
    public MutableLiveData<FitnessProfile> getFitnessProfileData(){
        //get from database when ready
//       return m_db.fitnessProfileDao().findByuserID();

        //until database is ready, pull from sample data.
        return SampleProfileData.getUserProfiles().get(0);
    }

    public void updateFitnessProfile(FitnessProfile fitnessProfile) {
        //This is only used to updated the fitness profile data for the sample data
        //This is not correctly using the pub sub model of the observer and live data
        fitnessProfileData.setValue(fitnessProfile);
//        MutableLiveData<FitnessProfile> fitProMut = new MutableLiveData<>();
//        fitProMut.setValue(fitnessProfile);
//        SampleProfileData.getUserProfiles().set(0, fitProMut);

//        m_executor.execute(
//                () -> {
//
////                  m_db.fitnessProfileDao().updateExistingFitnessProfileData(fitnessProfileData);
//                }
//        );
    }

    public void addNewFitnessProfile() {
        m_executor.execute(
                new Runnable() {
                    @Override
                    public void run() {
//                        m_db.fitnessProfileDao().insertNewUserData(fitnessProfileData);
                    }
                }
        );
    }
}

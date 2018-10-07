package com.example.mcresswell.project01.db.repo;

import com.example.mcresswell.project01.db.entity.FitnessProfile;

public class FitnessProfileRepository {
    private static final FitnessProfileRepository ourInstance = new FitnessProfileRepository();


    private FitnessProfile fitnessProfile;

    public static FitnessProfileRepository getInstance() {
        return ourInstance;
    }

    private FitnessProfileRepository() {

        //intialize all the member variables
        //best to get from the Database.
    }

    public FitnessProfile getFitnessProfile() {
        return fitnessProfile;
    }


    //TODO: KEEP THIS METHOD. WE'LL NEED THIS METHOD LATER TO BUILD A USER PROFILE OBJECT FROM DATABASE LOOKUP
    public FitnessProfile userProfileFromFitnessProfile(int fitnessProfileId) {
        //Retrieve a record from FitnessProfile with the given id
        //Then generate a FitnessProfile object from the record retrieved
        return null;
    }
}

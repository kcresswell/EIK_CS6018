package com.example.mcresswell.project01.dao;

import com.example.mcresswell.project01.dao.entity.FitnessProfile;
import com.example.mcresswell.project01.userProfile.UserProfile;

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
    public UserProfile userProfileFromFitnessProfile(int fitnessProfileId) {
        //Retrieve a record from FitnessProfile with the given id
        //Then generate a UserProfile object from the record retrieved
        return null;
    }
}

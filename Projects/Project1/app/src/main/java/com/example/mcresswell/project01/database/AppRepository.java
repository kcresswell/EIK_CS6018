package com.example.mcresswell.project01.database;

import com.example.mcresswell.project01.userProfile.UserProfile;

public class AppRepository {
    private static final AppRepository ourInstance = new AppRepository();


    private UserProfile m_userProfile;

    public static AppRepository getInstance() {
        return ourInstance;
    }

    private AppRepository() {

        //intialize all the member variables
        //best to get from the Database.
        m_userProfile = new UserProfile();
    }

    public UserProfile getUserProfile() {
        return m_userProfile;
    }


    //TODO: KEEP THIS METHOD. WE'LL CALL THIS METHOD LATER TO LOOKUP A USER PROFILE IN UserProfile DATABASE BY THEIR PROFILE ID
    public UserProfile getUserProfile(int userPofileId) {
        return null;
    }

}

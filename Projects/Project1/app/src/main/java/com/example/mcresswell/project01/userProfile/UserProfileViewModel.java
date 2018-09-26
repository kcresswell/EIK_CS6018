package com.example.mcresswell.project01.userProfile;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.mcresswell.project01.util.UserProfileUtils;

public class UserProfileViewModel extends AndroidViewModel {

    private static final String LOG = UserProfileViewModel.class.getSimpleName();

    private final MutableLiveData<UserProfile> userProfile =
            new MutableLiveData<>();
    private int userProfileId;
    private FitnessProfile fitnessProfile;


    public UserProfileViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("StaticFieldLeak")
    private void loadData(){
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... strings) {
                if (strings != null) {
                    //use data once its been retreived from database
                }
                return null;

            }

            @Override
            protected void onPostExecute(String s) {
                    userProfile.setValue(new UserProfile());
                    UserProfile user = userProfile.getValue();
                    fitnessProfile = new FitnessProfile(
                            user, user.getBodyData(), UserProfileUtils.calculateCalories(user),
                            UserProfileUtils.calculateBMR(user.getBodyData()));

            }
        }.execute(String.valueOf(userProfileId));
    }

    public void setUserProfile (int userProfileId) {
        this.userProfileId = userProfileId;
        loadData();
    }

    public FitnessProfile getFitnessProfile() {
        return fitnessProfile;
    }

    public MutableLiveData<UserProfile> getUserProfile() {
        return userProfile;
    }
}

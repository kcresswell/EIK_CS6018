package com.example.mcresswell.project01.userProfile;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class UserProfileViewModel extends AndroidViewModel {

    private static final String LOG = UserProfileViewModel.class.getSimpleName();

    private MutableLiveData<List<UserProfile>> userProfiles = new MutableLiveData<>();

    private String m_fName, m_lName, m_dob, m_sex, m_city, m_country, m_lifestyleSelection, m_weightGoal;
    private int m_Age, m_weight, m_feet, m_inches, m_lbsPerWeek;
    private double m_BMR, m_BMI;
    private int m_calsPerDay;

    public LiveData<List<UserProfile>> getUserProfile() {
        if (userProfiles == null) {
            userProfiles = new MutableLiveData<>();
            loadData();
        }
        return userProfiles;
    }

    public UserProfileViewModel(@NonNull Application application) {
        super(application);
    }

    @SuppressLint("StaticFieldLeak")
    private void loadData(){
        new AsyncTask<String,Void,String>() {
            @Override
            protected String doInBackground(String... strings) {
                String retrievedJsonData = null;
                if (strings != null) {
                    String fName = strings[0];
                    String lName = strings[1];
                    String dob = strings[2];
                    String sex = strings[3];
                    String city = strings[4];
                    String country = strings[5];
                    String lifestyleSel = strings[6];
                    String weightGoal = strings[7];
                    String age = strings[8];
                    String weight = strings[9];
                    String feet = strings[10];
                    String inches = strings[11];
                    String lbsPerWeek = strings[12];
                    String bmr = strings[13];
                    String bmi = strings[14];
                    String calsPerDay = strings[15];
                    Log.d(LOG, fName + ", " + lName + ", " + dob + ", " + sex + ", " + city + ", " + country
                            + ", " + lifestyleSel + ", " + weightGoal + ", " + age + ", " + weight + ", " + feet
                            + ", " + inches + ", " + lbsPerWeek + ", " + bmr + ", " + bmi + ", " + calsPerDay);
                }
//                try {
//                    retrievedJsonData = UserProfile.class.getUserData();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                return retrievedJsonData;

            }

            @Override
            protected void onPostExecute(String s) {
//                    userProfiles.setValue(UserProfile.class.getUserData(s));
//
            }
        };
//        }.execute(m_fName, m_lName, m_dob, m_sex, m_city, m_country, m_lifestyleSelection, m_weightGoal,
//                m_Age, m_weight, m_feet, m_inches, m_lbsPerWeek, m_BMR, m_BMI, m_calsPerDay);
    }

    public void setUserProfiles(String fName, String lName, String dob, String sex, String city, String country, String lifestyleSelection,
                                String weightGoal, int weight, int feet, int inches, int lbsPerWeek, double bmr, double bmi, int calsPerDay, int age){
        m_fName = fName;
        m_lName = lName;
        m_dob = dob;
        m_sex = sex;
        m_city = city;
        m_country = country;
        m_lifestyleSelection = lifestyleSelection;
        m_weightGoal = weightGoal;
        m_weight = weight;
        m_feet = feet;
        m_inches = inches;
        m_lbsPerWeek = lbsPerWeek;
        m_BMR = bmr;
        m_BMI = bmi;
        m_calsPerDay = calsPerDay;
        m_Age = age;

        loadData();
    }


//    public MutableLiveData<UserProfile> getUserProfileData() {
//        return userProfiles;
//    }
}

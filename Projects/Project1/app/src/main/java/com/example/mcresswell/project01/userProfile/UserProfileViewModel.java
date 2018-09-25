package com.example.mcresswell.project01.userProfile;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.mcresswell.project01.util.WeatherUtils;
import com.example.mcresswell.project01.weather.WeatherForecast;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class UserProfileViewModel extends AndroidViewModel {

    //need to update this, but left the weatherViewModel code in here as a reference - kaylau

    private static final String LOG = UserProfileViewModel.class.getSimpleName();

    private MutableLiveData<List<UserProfile>> userProfiles;

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
        new AsyncTask<String,Void,String>(){
            @Override
            protected String doInBackground(String... strings) {
                URL weatherDataURL = null;
                String retrievedJsonData = null;
                if (strings != null) {
                    String city = strings[0];
                    String country = strings[1];
                    Log.d(LOG, city + ", " + country);
                    weatherDataURL = WeatherUtils.buildWeatherApiUrl(city, country);
                }
                try {
                    retrievedJsonData = WeatherUtils.getJsonDataFromUrl(weatherDataURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return retrievedJsonData;

            }

            @Override
            protected void onPostExecute(String s) {
                    userProfiles.setValue(UserProfile.class.getUserProfile());

            }
        }.execute(m_fName, m_lName, m_dob, m_sex, m_city, m_country, m_lifestyleSelection, m_weightGoal,
                m_Age, m_weight, m_feet, m_inches, m_lbsPerWeek, m_BMR, m_BMI, m_calsPerDay);
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


    public MutableLiveData<UserProfile> getUserProfileData() {
        return userProfiles;
    }
}

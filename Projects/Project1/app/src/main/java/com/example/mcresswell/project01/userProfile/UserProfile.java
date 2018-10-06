package com.example.mcresswell.project01.userProfile;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.mcresswell.project01.util.UserProfileUtils;
import java.util.Date;

import static com.example.mcresswell.project01.util.UserProfileUtils.calculateAge;
import static com.example.mcresswell.project01.util.UserProfileUtils.calculateBMR;
import static com.example.mcresswell.project01.util.UserProfileUtils.calculateBmi;
import static com.example.mcresswell.project01.util.UserProfileUtils.calculateHeightInInches;

public class UserProfile implements Parcelable {

    private static final String LOG = UserProfile.class.getSimpleName();

    private int m_userID; // Profile ID in database
    private Date m_dateCreated;
    private String m_fName;
    private String m_lName;
    private String m_dob;
    private String m_sex;
    private String m_city;
    private String m_country;
    private String m_lifestyleSelection;
    private String m_weightGoal;  //GAIN/MAINTAIN/LOSE
    private int m_lbsPerWeek;
    private int m_weightInPounds;
    private int m_heightFeet;
    private int m_heightInches;
    private double m_bmi;
    private double m_bmr;
    private Bitmap m_profilePicture;


    public UserProfile() { }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public UserProfile(int userId,
                       Date dateCreated,
                       String fName,
                       String lName,
                       String dob,
                       String sex,
                       String city,
                       String country,
                       String lifestyleSelection,
                       String weightGoal,
                       int lbsPerWeek,
                       int weightInPounds,
                       int heightFeet,
                       int heightInches) {

        m_userID = userId;
        m_dateCreated = dateCreated;
        m_fName = fName;
        m_lName = lName;
        m_dob = dob;
        m_sex = sex;
        m_city = city;
        m_country = country;
        m_lifestyleSelection = lifestyleSelection;
        m_weightGoal = weightGoal;
        m_lbsPerWeek = lbsPerWeek;
        m_weightInPounds = weightInPounds;
        m_heightFeet = heightFeet;
        m_heightInches = heightInches;
        m_bmi = calculateBmi(calculateHeightInInches(heightFeet, heightInches), weightInPounds);
        m_bmr = calculateBMR(heightFeet, heightInches, sex, weightInPounds, calculateAge(dob));
//        m_profilePicture = profileImage;
    }

    protected UserProfile(Parcel in) {
        m_userID = in.readInt();
        m_fName = in.readString();
        m_lName = in.readString();
        m_dob = in.readString();
        m_sex = in.readString();
        m_city = in.readString();
        m_country = in.readString();
        m_lifestyleSelection = in.readString();
        m_weightGoal = in.readString();
        m_lbsPerWeek = in.readInt();
        m_weightInPounds = in.readInt();
        m_heightFeet = in.readInt();
        m_heightInches = in.readInt();
        m_bmi = in.readDouble();
        m_bmr = in.readDouble();
//        m_profilePicture =

    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    public int getM_userID() { return m_userID; }

    public void setM_userID(int id) {
        this.m_userID = id;
    }

    public Date getM_dateCreated() { return m_dateCreated; }

    public void setM_dateCreated(Date m_dateCreated) { this.m_dateCreated = m_dateCreated; }

    public String getM_fName() {
        return m_fName;
    }

    public void setM_fName(String m_fName) {
        this.m_fName = m_fName;
    }

    public String getM_lName() {
        return m_lName;
    }

    public void setM_lName(String m_lName) {
        this.m_lName = m_lName;
    }

    public String getM_dob() {
        return m_dob;
    }

    public void setM_dob(String m_dob) {
        this.m_dob = m_dob;
    }

    public String getM_sex() {
        return m_sex;
    }

    public void setM_sex(String m_sex) {
        this.m_sex = m_sex;
    }

    public String getM_city() {
        return m_city;
    }

    public void setM_city(String m_city) {
        this.m_city = m_city;
    }

    public String getM_country() {
        return m_country;
    }

    public void setM_country(String m_country) {
        this.m_country = m_country;
    }

    public String getM_lifestyleSelection() {
        return m_lifestyleSelection;
    }

    public void setM_lifestyleSelection(String m_lifestyleSelection) {
        this.m_lifestyleSelection = m_lifestyleSelection;
    }

    public String getM_weightGoal() {
        return m_weightGoal;
    }

    public void setM_weightGoal(String m_weightGoal) {
        this.m_weightGoal = m_weightGoal;
    }

    public int getM_lbsPerWeek() {
        return m_lbsPerWeek;
    }

    public void setM_lbsPerWeek(int m_lbsPerWeek) {
        this.m_lbsPerWeek = m_lbsPerWeek;
    }

    public int getM_weightInPounds() { return m_weightInPounds; }

    public void setM_weightInPounds(int m_weightInPounds) { this.m_weightInPounds = m_weightInPounds; }

    public int getM_heightFeet() { return m_heightFeet; }

    public void setM_heightFeet(int m_heightFeet) { this.m_heightFeet = m_heightFeet; }

    public int getM_heightInches() { return m_heightInches; }

    public void setM_heightInches(int m_heightInches) { this.m_heightInches = m_heightInches; }

    public double getM_bmi() { return m_bmi; }

    public void setM_bmi(double m_bmi) { this.m_bmi = m_bmi; }

    public double getM_bmr() { return m_bmr; }

    public void setM_bmr(double m_bmr) { this.m_bmr = m_bmr; }

    public void printUserProfileData(){
        Log.d(LOG, "printUserProfileData");
        Log.d(LOG, "userID: " + this.getM_userID());
        Log.d(LOG, "First Name: " + this.getM_fName());
        Log.d(LOG, "Last Name: " + this.getM_lName());
        Log.d(LOG, "DOB: " + this.getM_dob());
        Log.d(LOG, "Sex: " + this.getM_sex());
        Log.d(LOG, "Location: " + this.getM_city() + ", " + this.getM_country());
        Log.d(LOG, "Lifestyle Selection (ACTIVE/SEDENTERY): " + this.getM_lifestyleSelection());
        Log.d(LOG, "Weight Goal/Objectives (GAIN/MAINTAIN/LOSE): " + this.getM_weightGoal() + " " + this.getM_lbsPerWeek() + " lbs/week");
        Log.d(LOG, "Current Weight (lbs): " + this.getM_weightInPounds());
        Log.d(LOG, "Current Height: " + this.getM_heightFeet() + " Feet and " + this.getM_heightInches() + " Inches");
        Log.d(LOG, "Current Basal Metabolic Weight (BMR): " + this.getM_bmr() + " calories/day");
        Log.d(LOG, "Current BMI: " + this.getM_bmi());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static UserProfile newTestUserProfileInstance() {
        UserProfile testUser = new UserProfile();
        testUser.setM_userID(1);
        testUser.setM_fName("TEST");
        testUser.setM_lName("LASTNAME");
        testUser.setM_dob("01/01/1900");
        testUser.setM_city("SACRAMENTO");
        testUser.setM_country("US");
        testUser.setM_sex("FEMALE");
        testUser.setM_lbsPerWeek(3);
        testUser.setM_lifestyleSelection("ACTIVE");
        testUser.setM_weightGoal("LOSE");
        testUser.setM_lbsPerWeek(2);
        testUser.setM_weightInPounds(150);
        testUser.setM_heightFeet(5);
        testUser.setM_heightInches(9);
        testUser.setM_bmi(calculateBmi(calculateHeightInInches(testUser.getM_heightFeet(),
                testUser.getM_heightInches()), testUser.getM_weightInPounds()));
        int age = calculateAge(testUser.getM_dob());
        double bmr = calculateBMR(testUser.getM_heightFeet(),
                testUser.getM_heightInches(), testUser.getM_sex(),
                testUser.getM_weightInPounds(), age);
        testUser.setM_bmr(bmr);

        return testUser;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(m_userID);
        dest.writeString(m_fName);
        dest.writeString(m_lName);
        dest.writeString(m_dob);
        dest.writeString(m_sex);
        dest.writeString(m_city);
        dest.writeString(m_country);
        dest.writeString(m_lifestyleSelection);
        dest.writeString(m_weightGoal);
        dest.writeInt(m_lbsPerWeek);
        dest.writeInt(m_weightInPounds);
        dest.writeInt(m_heightFeet);
        dest.writeInt(m_heightInches);
        dest.writeDouble(m_bmi);
        dest.writeDouble(m_bmr);
//        m_profilePicture.writeToParcel(dest, flags);
    }
}
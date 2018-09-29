package com.example.mcresswell.project01.userProfile;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.mcresswell.project01.util.UserProfileUtils;

import java.util.Date;



/**
 *  A POJO class representing all of the data associated with a given user.
 *  This is NOT the entity/DAO class, the corresponding entity class for UserProfile
 *  is the FitnessProfile class.
 *
 */
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
    private String m_weightGoal;  //TODO: CHANGE THIS TO AN INT
    private int m_lbsPerWeek;
    private PhysicalStats bodyData;

    public UserProfile() {
    }

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
                       double weightInPounds,
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
        m_lbsPerWeek = lbsPerWeek;
        m_weightGoal = weightGoal;

        bodyData = new PhysicalStats(sex,
                UserProfileUtils.calculateAge(dob),
                weightInPounds, heightFeet, heightInches);

        int height = UserProfileUtils.calculateHeightInInches(heightFeet, heightInches);
        bodyData.setBmi(UserProfileUtils.calculateBmi(height, weightInPounds));
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

    public int getId() {
        return m_userID;
    }

    public void setId(int id) {
        this.m_userID = id;
    }

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

    public PhysicalStats getBodyData() {
        return bodyData;
    }

    public void setBodyData(PhysicalStats bodyData) {
        this.bodyData = bodyData;
    }

    public void printUserProfileData(){
        Log.d(LOG, "printUserProfileData");
        Log.d(LOG, "First Name: " + this.getM_fName());
        Log.d(LOG, "Last Name: " + this.getM_lName());

        //TODO: Finish this later once confirmed data is getting passed

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static UserProfile newTestUserProfileInstance() {
        UserProfile testUser = new UserProfile();
        testUser.setId(1);
        testUser.setM_fName("TEST");
        testUser.setM_lName("LASTNAME");
        testUser.setM_dob("01/01/1900");
        testUser.setM_city("SACRAMENTO");
        testUser.setM_country("US");
        testUser.setM_sex("FEMALE");
        testUser.setM_lbsPerWeek(3);
        PhysicalStats stats =
                new PhysicalStats(testUser.getM_sex(),
                UserProfileUtils.calculateAge(testUser.getM_dob()),
                        120.0, 5,5);
        testUser.setBodyData(stats);

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
    }
}
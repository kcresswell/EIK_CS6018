package com.example.mcresswell.project01.userProfile;

import android.arch.persistence.room.Entity;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.mcresswell.project01.util.UserProfileUtils;

import java.util.Date;

@Entity()
public class UserProfile {
    private int m_userID;
    private Date m_dateCreated;
    private String m_fName;
    private String m_lName;
    private String m_dob;
    private String m_sex;
    private String m_city;
    private String m_country;
    private String m_lifestyleSelection;
    private String m_weightGoal;
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
                       double heightInches) {

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

        double height = UserProfileUtils.calculateHeightInInches(heightFeet, heightInches);
        bodyData.setBmi(UserProfileUtils.calculateBmi(height, weightInPounds));
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
}
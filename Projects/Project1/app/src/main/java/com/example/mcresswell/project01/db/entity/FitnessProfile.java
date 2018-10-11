package com.example.mcresswell.project01.db.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Build;
import android.support.annotation.RequiresApi;

import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateAge;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateBMR;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateBmi;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateHeightInInches;


/**
 *  A POJO class representing all of the data associated with a given user.
 *  This is NOT the entity/DAO class, the corresponding entity class for FitnessProfile
 *  is the FitnessProfile class.
 *
 */

@Entity
public class FitnessProfile {

    @PrimaryKey
    private int m_userID; // Profile ID in database

    @ColumnInfo(name = "first_name")
    private String m_fName;

    @ColumnInfo(name = "last_name")
    private String m_lName;

    //location
    @ColumnInfo(name = "city")
    private String m_city;

    @ColumnInfo(name = "country")
    private String m_country;

    //physical profile
    @ColumnInfo(name = "dob")
    private String m_dob;

    @ColumnInfo(name = "sex")
    private String m_sex;

    @ColumnInfo(name = "weight_lbs")
    private int m_weightInPounds;

    @ColumnInfo(name = "height_ft")
    private int m_heightFeet;

    @ColumnInfo(name = "height_in")
    private int m_heightInches;

    //fitness details
    @ColumnInfo(name = "lifestyle")
    private String m_lifestyleSelection;

    @ColumnInfo(name = "weight_goal")
    private String m_weightGoal;  //GAIN/MAINTAIN/LOSE

    @ColumnInfo(name = "lbs_per_week")
    private int m_lbsPerWeek;

    @ColumnInfo(name = "bmi")
    private double m_bmi;

    @ColumnInfo(name = "bmr")
    private double m_bmr;
//    private Bitmap m_profilePicture;

    public FitnessProfile() { }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public FitnessProfile(
            int userId,
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

    //may not need both constructors, this one is just with all data but no id number
    public FitnessProfile(
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

    public int getM_userID() { return m_userID; }

    public void setM_userID(int id) {
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
}
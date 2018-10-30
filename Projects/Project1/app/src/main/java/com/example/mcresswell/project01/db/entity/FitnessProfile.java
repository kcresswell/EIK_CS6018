package com.example.mcresswell.project01.db.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Build;
import android.support.annotation.RequiresApi;


import java.util.Date;

import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateAge;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateBMR;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateBmi;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateHeightInInches;

@Entity(/*foreignKeys = @ForeignKey(entity = User.class,
                            parentColumns = "id",
                            childColumns = "user_id"),*/
        indices = {@Index(value = {"user_id"}, unique = true)})
public class FitnessProfile {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int m_Id; //THIS IS THE FITNESS PROFILE ID, NOT THE USER ID.

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

    @ColumnInfo(name = "stepCount")
    private float m_stepCount;

    @ColumnInfo(name = "dateLastUpdated")
    private Date m_dateLastUpdated;

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

    @ColumnInfo(name = "user_id")
    private int userId; //THIS IS THE USER ID FOREIGN KEY FIELD.


    public FitnessProfile() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public FitnessProfile(
            int m_userId,
            String m_fName,
            String m_lName,
            String m_dob,
            String m_sex,
            String m_city,
            String m_country,
            String m_lifestyleSelection,
            String m_weightGoal,
            int m_lbsPerWeek,
            int m_weightInPounds,
            int m_heightFeet,
            int m_heightInches,
            float m_stepCount,
            Date m_dateLastUpdated) {

        m_Id = m_userId;
        this.m_fName = m_fName;
        this.m_lName = m_lName;
        this.m_dob = m_dob;
        this.m_sex = m_sex;
        this.m_city = m_city;
        this.m_country = m_country;
        this.m_lifestyleSelection = m_lifestyleSelection;
        this.m_weightGoal = m_weightGoal;
        this.m_lbsPerWeek = m_lbsPerWeek;
        this.m_weightInPounds = m_weightInPounds;
        this.m_heightFeet = m_heightFeet;
        this.m_heightInches = m_heightInches;
        this.m_stepCount = m_stepCount;
        this.m_dateLastUpdated = m_dateLastUpdated;

        m_bmi = calculateBmi(calculateHeightInInches(m_heightFeet, m_heightInches), m_weightInPounds);
        m_bmr = calculateBMR(m_heightFeet, m_heightInches, m_sex, m_weightInPounds, calculateAge(m_dob));
//        m_profilePicture = profileImage;
    }

    public int getM_Id() { return m_Id; }

    public void setM_Id(int m_Id) {
        this.m_Id = m_Id;
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

    public float getM_stepCount() {return m_stepCount; }

    public void setM_stepCount(Float m_stepCount) {this.m_stepCount = m_stepCount; }

    public Date getM_dateLastUpdated() {return m_dateLastUpdated; }

    public void setM_dateLastUpdated(Date m_dateLastUpdated) {this.m_dateLastUpdated = m_dateLastUpdated; }

    public double getM_bmi() { return m_bmi; }

    public void setM_bmi(double m_bmi) { this.m_bmi = m_bmi; }

    public double getM_bmr() { return m_bmr; }

    public void setM_bmr(double m_bmr) { this.m_bmr = m_bmr; }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
package com.example.mcresswell.project01.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * FitnessProfile entity representing the FitnessProfile database table that will
 * store the majority of user fitness/profile data.
 */

@Entity(tableName = "fitnessprofile")
public class FitnessProfile {

    @PrimaryKey
    private int id;
    private String first_name;
    private String last_name;
    private Date dob;
    private String sex;
    private String city;
    private String country;
    private String activity_level;
    private String weight_goal;
    private int lbs_per_week;
    private double weight;
    private double height;
    private String profile_image; // profile image url or filepath

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getActivity_level() {
        return activity_level;
    }

    public void setActivity_level(String activity_level) {
        this.activity_level = activity_level;
    }

    public String getWeight_goal() {
        return weight_goal;
    }

    public void setWeight_goal(String weight_goal) {
        this.weight_goal = weight_goal;
    }

    public int getLbs_per_week() {
        return lbs_per_week;
    }

    public void setLbs_per_week(int lbs_per_week) {
        this.lbs_per_week = lbs_per_week;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}

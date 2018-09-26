package com.example.mcresswell.project01.userProfile;

public class FitnessProfile {

    private UserProfile userProfile;
    private PhysicalStats physicalStats;
    private double computedCalories;
    private double computedBmr;

    public FitnessProfile(UserProfile userProfile, PhysicalStats physicalStats, double computedCalories, double computedBmr) {
        this.userProfile = userProfile;
        this.physicalStats = physicalStats;
        this.computedCalories = computedCalories;
        this.computedBmr = computedBmr;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public PhysicalStats getPhysicalStats() {
        return physicalStats;
    }

    public void setPhysicalStats(PhysicalStats physicalStats) {
        this.physicalStats = physicalStats;
    }

    public double getComputedCalories() {
        return computedCalories;
    }

    public void setComputedCalories(double computedCalories) {
        this.computedCalories = computedCalories;
    }

    public double getComputedBmr() {
        return computedBmr;
    }

    public void setComputedBmr(double computedBmr) {
        this.computedBmr = computedBmr;
    }
}

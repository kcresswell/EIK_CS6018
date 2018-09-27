package com.example.mcresswell.project01.userProfile;

/**
 * POJO class representing at-a-glance fitness summary/details for a user.
 * Objects of this class primarily represent the data that the user will be
 * shown on their main dashboard, as well as when they click the FitnessDetails
 * button.
 */
public class UserFitnessSnapshot {

    private UserProfile userProfile;
    private PhysicalStats physicalStats;

    //Data values for displaying on user's main dashboard
    private double computedCalories;
    private double computedBmr;
    private String weightGoals;
    private String lifestyleActivityLevel;

    public UserFitnessSnapshot(UserProfile userProfile, PhysicalStats physicalStats,
                               double computedCalories, double computedBmr) {
        this.userProfile = userProfile;
        this.physicalStats = physicalStats;
        this.computedCalories = computedCalories;
        this.computedBmr = computedBmr;
        this.lifestyleActivityLevel = userProfile.getM_lifestyleSelection();
        this.weightGoals = userProfile.getM_weightGoal();
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

    public String getWeightGoals() {
        return weightGoals;
    }

    public void setWeightGoals(String weightGoals) {
        this.weightGoals = weightGoals;
    }

    public String getLifestyleActivityLevel() {
        return lifestyleActivityLevel;
    }

    public void setLifestyleActivityLevel(String lifestyleActivityLevel) {
        this.lifestyleActivityLevel = lifestyleActivityLevel;
    }
}

package com.example.mcresswell.project01.userProfile;

public class PhysicalStats {

    private String sex;
    private int age;
    private double weightInPounds;
    private int heightFeet;
    private double heightInches;

    private double bmi;

    public PhysicalStats(String sex, int age, double weightInPounds,
                         int heightFeet, double heightInches) {
        this.sex = sex;
        this.age = age;
        this.weightInPounds = weightInPounds;
        this.heightFeet = heightFeet;
        this.heightInches = heightInches;
        //initialize bmi in UserProfile
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getWeightInPounds() {
        return weightInPounds;
    }

    public void setWeightInPounds(double weightInPounds) {
        this.weightInPounds = weightInPounds;
    }

    public int getHeightFeet() {
        return heightFeet;
    }

    public void setHeightFeet(int heightFeet) {
        this.heightFeet = heightFeet;
    }

    public double getHeightInches() {
        return heightInches;
    }

    public void setHeightInches(double heightInches) {
        this.heightInches = heightInches;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }
}

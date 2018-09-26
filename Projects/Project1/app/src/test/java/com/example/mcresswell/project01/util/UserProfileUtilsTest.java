package com.example.mcresswell.project01.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserProfileUtilsTest {

    @Test
    public void calculateHealthyWeightBmi() {
        double bmi = UserProfileUtils.calculateBmi(65, 150);
        assertTrue(UserProfileUtils.getBmi(bmi).equals(UserProfileUtils.BodyMassIndex.HEALTHY_WEIGHT));
    }

    @Test
    public void calculateUnderWeightBmi() {
        double bmi = UserProfileUtils.calculateBmi(65, 105);
        assertTrue(UserProfileUtils.getBmi(bmi).equals(UserProfileUtils.BodyMassIndex.UNDERWEIGHT));
    }

    @Test
    public void calculateObeseBmi() {
        double bmi = UserProfileUtils.calculateBmi(65, 195);
        assertTrue(UserProfileUtils.getBmi(bmi).equals(UserProfileUtils.BodyMassIndex.OBESE));
    }

    @Test
    public void calculateClinicallyObeseBmi() {
        double bmi = UserProfileUtils.calculateBmi(65, 235);
        assertTrue(UserProfileUtils.getBmi(bmi).equals(UserProfileUtils.BodyMassIndex.CLINICALLY_OBESE));
    }

    @Test
    public void getBmi() {
        assertTrue(UserProfileUtils.getBmi(18.49999).equals(UserProfileUtils.BodyMassIndex.UNDERWEIGHT));
        assertTrue(UserProfileUtils.getBmi(18.5).equals(UserProfileUtils.BodyMassIndex.HEALTHY_WEIGHT));
        assertTrue(UserProfileUtils.getBmi(24.9).equals(UserProfileUtils.BodyMassIndex.HEALTHY_WEIGHT));
        assertTrue(UserProfileUtils.getBmi(25).equals(UserProfileUtils.BodyMassIndex.OVERWEIGHT));
        assertTrue(UserProfileUtils.getBmi(30).equals(UserProfileUtils.BodyMassIndex.OBESE));
        assertTrue(UserProfileUtils.getBmi(34.9).equals(UserProfileUtils.BodyMassIndex.OBESE));
        assertTrue(UserProfileUtils.getBmi(35).equals(UserProfileUtils.BodyMassIndex.CLINICALLY_OBESE));
        assertTrue(UserProfileUtils.getBmi(40).equals(UserProfileUtils.BodyMassIndex.CLINICALLY_OBESE));

    }
}
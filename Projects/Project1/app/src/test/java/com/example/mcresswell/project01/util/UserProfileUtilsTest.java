package com.example.mcresswell.project01.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserProfileUtilsTest {

    @Test
    public void calculateHealthyWeightBmi() {
        double bmi = UserProfileUtils.calculateBmi(65, 150);
        assertTrue(UserProfileUtils.getBmiClassification(bmi).equals(UserProfileUtils.BodyMassIndex.HEALTHY_WEIGHT));
    }

    @Test
    public void calculateUnderWeightBmi() {
        double bmi = UserProfileUtils.calculateBmi(65, 105);
        assertTrue(UserProfileUtils.getBmiClassification(bmi).equals(UserProfileUtils.BodyMassIndex.UNDERWEIGHT));
    }

    @Test
    public void calculateObeseBmi() {
        double bmi = UserProfileUtils.calculateBmi(65, 195);
        assertTrue(UserProfileUtils.getBmiClassification(bmi).equals(UserProfileUtils.BodyMassIndex.OBESE));
    }

    @Test
    public void calculateClinicallyObeseBmi() {
        double bmi = UserProfileUtils.calculateBmi(65, 235);
        assertTrue(UserProfileUtils.getBmiClassification(bmi).equals(UserProfileUtils.BodyMassIndex.CLINICALLY_OBESE));
    }

    @Test
    public void getBmi() {
        assertTrue(UserProfileUtils.getBmiClassification(18.49999).equals(UserProfileUtils.BodyMassIndex.UNDERWEIGHT));
        assertTrue(UserProfileUtils.getBmiClassification(18.5).equals(UserProfileUtils.BodyMassIndex.HEALTHY_WEIGHT));
        assertTrue(UserProfileUtils.getBmiClassification(24.9).equals(UserProfileUtils.BodyMassIndex.HEALTHY_WEIGHT));
        assertTrue(UserProfileUtils.getBmiClassification(25).equals(UserProfileUtils.BodyMassIndex.OVERWEIGHT));
        assertTrue(UserProfileUtils.getBmiClassification(30).equals(UserProfileUtils.BodyMassIndex.OBESE));
        assertTrue(UserProfileUtils.getBmiClassification(34.9).equals(UserProfileUtils.BodyMassIndex.OBESE));
        assertTrue(UserProfileUtils.getBmiClassification(35).equals(UserProfileUtils.BodyMassIndex.CLINICALLY_OBESE));
        assertTrue(UserProfileUtils.getBmiClassification(40).equals(UserProfileUtils.BodyMassIndex.CLINICALLY_OBESE));

    }
}
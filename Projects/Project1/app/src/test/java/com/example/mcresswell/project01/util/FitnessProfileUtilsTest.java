package com.example.mcresswell.project01.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class FitnessProfileUtilsTest {

    @Test
    public void calculateHealthyWeightBmi() {
        double bmi = FitnessProfileUtils.calculateBmi(65, 150);
        assertTrue(FitnessProfileUtils.getBmiClassification(bmi).equals(FitnessProfileUtils.BodyMassIndex.HEALTHY_WEIGHT));
    }

    @Test
    public void calculateUnderWeightBmi() {
        double bmi = FitnessProfileUtils.calculateBmi(65, 105);
        assertTrue(FitnessProfileUtils.getBmiClassification(bmi).equals(FitnessProfileUtils.BodyMassIndex.UNDERWEIGHT));
    }

    @Test
    public void calculateObeseBmi() {
        double bmi = FitnessProfileUtils.calculateBmi(65, 195);
        assertTrue(FitnessProfileUtils.getBmiClassification(bmi).equals(FitnessProfileUtils.BodyMassIndex.OBESE));
    }

    @Test
    public void calculateClinicallyObeseBmi() {
        double bmi = FitnessProfileUtils.calculateBmi(65, 235);
        assertTrue(FitnessProfileUtils.getBmiClassification(bmi).equals(FitnessProfileUtils.BodyMassIndex.CLINICALLY_OBESE));
    }

    @Test
    public void getBmi() {
        assertTrue(FitnessProfileUtils.getBmiClassification(18.49999).equals(FitnessProfileUtils.BodyMassIndex.UNDERWEIGHT));
        assertTrue(FitnessProfileUtils.getBmiClassification(18.5).equals(FitnessProfileUtils.BodyMassIndex.HEALTHY_WEIGHT));
        assertTrue(FitnessProfileUtils.getBmiClassification(24.9).equals(FitnessProfileUtils.BodyMassIndex.HEALTHY_WEIGHT));
        assertTrue(FitnessProfileUtils.getBmiClassification(25).equals(FitnessProfileUtils.BodyMassIndex.OVERWEIGHT));
        assertTrue(FitnessProfileUtils.getBmiClassification(30).equals(FitnessProfileUtils.BodyMassIndex.OBESE));
        assertTrue(FitnessProfileUtils.getBmiClassification(34.9).equals(FitnessProfileUtils.BodyMassIndex.OBESE));
        assertTrue(FitnessProfileUtils.getBmiClassification(35).equals(FitnessProfileUtils.BodyMassIndex.CLINICALLY_OBESE));
        assertTrue(FitnessProfileUtils.getBmiClassification(40).equals(FitnessProfileUtils.BodyMassIndex.CLINICALLY_OBESE));

    }
}
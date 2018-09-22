package com.example.mcresswell.project01.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class BmiUtilsTest {

    @Test
    public void calculateHealthyWeightBmi() {
        double bmi = BmiUtils.calculateBmi(65, 150);
        assertTrue(BmiUtils.getBmi(bmi).equals(BmiUtils.BodyMassIndex.HEALTHY_WEIGHT));
    }

    @Test
    public void getBmi() {
        assertTrue(BmiUtils.getBmi(18.49).equals(BmiUtils.BodyMassIndex.UNDERWEIGHT));
        assertTrue(BmiUtils.getBmi(18.5).equals(BmiUtils.BodyMassIndex.HEALTHY_WEIGHT));
        assertTrue(BmiUtils.getBmi(25).equals(BmiUtils.BodyMassIndex.HEALTHY_WEIGHT));
        assertTrue(BmiUtils.getBmi(30).equals(BmiUtils.BodyMassIndex.OVERWEIGHT));
        assertTrue(BmiUtils.getBmi(35).equals(BmiUtils.BodyMassIndex.OBESE));
        assertTrue(BmiUtils.getBmi(40).equals(BmiUtils.BodyMassIndex.CLINICALLY_OBESE));

    }
}
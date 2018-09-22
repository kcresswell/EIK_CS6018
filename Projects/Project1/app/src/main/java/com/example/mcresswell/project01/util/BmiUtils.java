package com.example.mcresswell.project01.util;

public class BmiUtils {

    public enum BodyMassIndex {
        UNDERWEIGHT,
        HEALTHY_WEIGHT,
        OVERWEIGHT,
        OBESE,
        CLINICALLY_OBESE
    }

    public static double calculateBmi(double heightInInches, double weightInPounds) {
        final double BMI_METRIC_TO_IMPERIAL_SCALE_FACTOR = 703;
        return weightInPounds/Math.pow(heightInInches, 2) * BMI_METRIC_TO_IMPERIAL_SCALE_FACTOR;
    }

    public static BodyMassIndex getBmi(double bmiValue) {
        final double UNDERWEIGHT_THRESHOLD = 18.5;
        final double AVERAGE_THRESHOLD = 25.0;
        final double OVERWEIGHT_THRESHOLD = 30.0;
        final double OBESE_THRESHOLD = 35.0;

        return bmiValue < UNDERWEIGHT_THRESHOLD ? BodyMassIndex.UNDERWEIGHT :
                bmiValue < AVERAGE_THRESHOLD ? BodyMassIndex.HEALTHY_WEIGHT :
                        bmiValue < OVERWEIGHT_THRESHOLD ? BodyMassIndex.OVERWEIGHT :
                                bmiValue < OBESE_THRESHOLD ? BodyMassIndex.OBESE :
                                        BodyMassIndex.CLINICALLY_OBESE;
    }
}

package com.example.mcresswell.project01.util;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.example.mcresswell.project01.db.entity.FitnessProfile;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FitnessProfileUtils {

    private static final String LOG_TAG = FitnessProfileUtils.class.getSimpleName();

    public enum BodyMassIndex {
        UNDERWEIGHT,
        HEALTHY_WEIGHT,
        OVERWEIGHT,
        OBESE,
        CLINICALLY_OBESE
    }

    public static double calculateBmi(int heightInInches, int weightInPounds) {
        final double BMI_METRIC_TO_IMPERIAL_SCALE_FACTOR = 703;
        return weightInPounds/Math.pow(heightInInches, 2) * BMI_METRIC_TO_IMPERIAL_SCALE_FACTOR;
    }

    public static BodyMassIndex getBmiClassification(double bmiValue) {
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

    public static double calculateBMR(int heightFeet, int heightInches,
                                      String sex, int weightInPounds, int age) {
        double BMR = 0.0;
        double totalHeightInInches = (heightFeet * 12.0) + heightInches;

        //calculate BMR based on sex of user
        if(sex.equalsIgnoreCase("F")){
            //user is female, s = -161
            BMR = (9.99 * weightInPounds) +
                    (6.25 * totalHeightInInches) - 4.92 * age - 161;
        }
        else if (sex.equalsIgnoreCase("M")) {
            //user is male, s = 5
            BMR = (9.99 * weightInPounds) +
                    (6.25 * totalHeightInInches) - 4.92 * age + 5;
        }
        return BMR;
    }

    //http://www.bmrcalculator.org
    //You exercise moderately (3-5 days per week)	Calories Burned a Day = BMR x 1.55
    //Low	You get little to no exercise	Calories Burned a Day = BMR x 1.2
    //In order to lose 1 pound of fat each week, you must have a deficit of 3,500 calories over the course of a week.[5]
    // https://www.wikihow.com/Calculate-How-Many-Calories-You-Need-to-Eat-to-Lose-Weight
    public static double calculateCalories(FitnessProfile profile) {
        double numOfCalories;
        double BMR = 1500;

        if (profile.getM_lifestyleSelection().equalsIgnoreCase("Sedentary")) {
            numOfCalories = BMR * 1.2;

            if(profile.getM_weightGoal().equalsIgnoreCase("Gain")) {
                numOfCalories += profile.getM_lbsPerWeek() + 3500;
            } else if(profile.getM_weightGoal().equalsIgnoreCase("Lose")){
                numOfCalories += profile.getM_lbsPerWeek() - 3500;
            }

        }
        else {
            //active
            numOfCalories = BMR * 1.55;

            if(profile.getM_weightGoal().equalsIgnoreCase("Gain")) {
                numOfCalories += BMR + profile.getM_lbsPerWeek() + 3500;
            } else if(profile.getM_weightGoal().equalsIgnoreCase("Lose")){
                numOfCalories += BMR + profile.getM_lbsPerWeek() - 3500;
            }
        }

        return numOfCalories;
    }

    public static int calculateHeightInInches(int feet, int inches) {
        return feet * 12 + inches;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static int calculateAge(String m_dob) {
        DateTimeFormatter dob_format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        dob_format = dob_format.withLocale(Locale.US);

        LocalDate dob = LocalDate.parse(m_dob, dob_format);
        LocalDate today = LocalDate.now();

        return Period.between(dob, today).getYears();
    }

    public static void printUserProfileData(FitnessProfile fitnessProfile){
        Log.d(LOG_TAG, "printUserProfileData");
        Log.d(LOG_TAG, "FitnessProfile ID: " + fitnessProfile.getM_Id());
        Log.d(LOG_TAG, "First Name: " + fitnessProfile.getM_fName());
        Log.d(LOG_TAG, "Last Name: " + fitnessProfile.getM_lName());
        Log.d(LOG_TAG, "DOB: " + fitnessProfile.getM_dob());
        Log.d(LOG_TAG, "Sex: " + fitnessProfile.getM_sex());
        Log.d(LOG_TAG, "Location: " + fitnessProfile.getM_city() + ", " + fitnessProfile.getM_country());
        Log.d(LOG_TAG, "Lifestyle Selection (ACTIVE/SEDENTERY): " + fitnessProfile.getM_lifestyleSelection());
        Log.d(LOG_TAG, "Weight Goal/Objectives (GAIN/MAINTAIN/LOSE): " + fitnessProfile.getM_weightGoal() + " " + fitnessProfile.getM_lbsPerWeek() + " lbs/week");
        Log.d(LOG_TAG, "Current Weight (lbs): " + fitnessProfile.getM_weightInPounds());
        Log.d(LOG_TAG, "Current Height: " + fitnessProfile.getM_heightFeet() + " Feet and " + fitnessProfile.getM_heightInches() + " Inches");
        Log.d(LOG_TAG, "Current Basal Metabolic Weight (BMR): " + fitnessProfile.getM_bmr() + " calories/day");
        Log.d(LOG_TAG, "Current BMI: " + fitnessProfile.getM_bmi());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static FitnessProfile newTestUserProfileInstance() {
        FitnessProfile testUser = new FitnessProfile();
//        testUser.setM_Id(1);
        testUser.setM_fName("TEST");
        testUser.setM_lName("LASTNAME");
        testUser.setM_dob("01/01/1900");
        testUser.setM_city("SACRAMENTO");
        testUser.setM_country("US");
        testUser.setM_sex("F");
        testUser.setM_lbsPerWeek(3);
        testUser.setM_lifestyleSelection("ACTIVE");
        testUser.setM_weightGoal("LOSE");
        testUser.setM_lbsPerWeek(2);
        testUser.setM_weightInPounds(150);
        testUser.setM_heightFeet(5);
        testUser.setM_heightInches(9);
        testUser.setM_bmi(calculateBmi(calculateHeightInInches(testUser.getM_heightFeet(),
                testUser.getM_heightInches()), testUser.getM_weightInPounds()));
        int age = calculateAge(testUser.getM_dob());
        double bmr = calculateBMR(testUser.getM_heightFeet(),
                testUser.getM_heightInches(), testUser.getM_sex(),
                testUser.getM_weightInPounds(), age);
        testUser.setM_bmr(bmr);

        return testUser;
    }
}

package com.example.mcresswell.project01;

import android.os.Bundle;

import com.example.mcresswell.project01.util.BmiUtils;

public class FitnessScore {
    private String m_fName, m_lName, m_dob, m_sex, m_city, m_country, m_weight, m_feet, m_inches, m_lbsPerWeek, m_lifestyleSelection, m_weightGoal;
    private int m_Age;

    public FitnessScore(String fName, String lName, String dob, String sex, String city, String country, String weight, String feet, String inches, String lbsPerWeek, String lifestyleSelection, String weightGoal, int age){
        m_fName = fName;
        m_lName = lName;
        m_dob = dob;
        m_sex = sex;
        m_city = city;
        m_country = country;
        m_weight = weight;
        m_feet = feet;
        m_inches = inches;
        m_lbsPerWeek = lbsPerWeek;
        m_lifestyleSelection = lifestyleSelection;
        m_weightGoal = weightGoal;
        m_Age = age;
    }

    //getters for member variables
    public String getM_fName() {return m_fName;}
    public String getM_lName() {return m_lName;}
    public String getM_dob() {return m_dob;}
    public String getM_sex(){return m_sex;}
    public String getM_city() {return m_city;}
    public String getM_country() {return m_country;}
    public String getM_weight() {return m_weight;}
    public String getM_feet() {return m_feet;}
    public String getM_inches() {return m_inches;}
    public String getM_lbsPerWeek() {return m_lbsPerWeek;}
    public String getM_lifestyleSelection() {return m_lifestyleSelection;}
    public String getM_weightGoal() {return m_weightGoal;}
    public int getM_Age() {return m_Age;}




//    public void getDataFromBundle(Bundle userDataBundle) {
//        m_fName = userDataBundle.getString("fname");
//        m_lName = userDataBundle.getString("lname");
//        m_dob = userDataBundle.getString("dob");
//        m_sex = userDataBundle.getString("sex");
//        m_city = userDataBundle.getString("city");
//        m_country = userDataBundle.getString("country");
//        m_weight = userDataBundle.getString("weight");
//        m_feet = userDataBundle.getString("feet");
//        m_inches = userDataBundle.getString("inches");
//        m_Age = userDataBundle.getInt("age");
//        m_lbsPerWeek = userDataBundle.getString("lbsPerWeek");
//        m_lifestyleSelection = userDataBundle.getString("lifestyle");
//        m_weightGoal = userDataBundle.getString("weightGoal");
//    }


    ////    http://www.bmrcalculator.org
////    BMR = (9.99 x weight + 6.25 x height – 4.92 x age + s ) kcal/day
////    Here, weight is in Kilograms, height is in centimeters and age is in years.
////    s is a factor to adjust for gender and adopts the value +5 for males and -161 for females.
    private double calculateBMR() {
        double BMR = 0.0;

        //get weight value
        double weight = Double.parseDouble(m_weight);

        //get height values
        double heightFeet = Double.parseDouble(m_feet);
        double heightInches = Double.parseDouble(m_inches);
        double totalHeightInInches = (heightFeet * 12.0) + heightInches;

        //get age value

        //calculate BMR based on sex of user

        if(m_sex.equals("Female") || m_sex.equals("female") || m_sex.equals("F") || m_sex.equals("f")) {
            //user is female, s = -161
            BMR = (9.99 * weight) + (6.25 * totalHeightInInches) - 4.92 * m_Age - 161;
        } else if (m_sex.equals("Male") || m_sex.equals("male") || m_sex.equals("M") || m_sex.equals("m")){
            //user is male, s = 5
            BMR = (9.99 * weight) + (6.25 * totalHeightInInches) - 4.92 * m_Age + 5;
        } else {
            //--TODO: throw an error here if not male or female--//
        }

        return BMR;
    }

    //http://www.bmrcalculator.org
    //You exercise moderately (3-5 days per week)	Calories Burned a Day = BMR x 1.55
    //Low	You get little to no exercise	Calories Burned a Day = BMR x 1.2
    //In order to lose 1 pound of fat each week, you must have a deficit of 3,500 calories over the course of a week.[5]
    // https://www.wikihow.com/Calculate-How-Many-Calories-You-Need-to-Eat-to-Lose-Weight
    private double calculateCalories(int lbToGainOrLoose) {
        double numOfCalories;
        double BMR = calculateBMR();

        if(m_lifestyleSelection.equals("Sedentary")){
            numOfCalories = BMR * 1.2;

            if(m_weightGoal.equals("Gain")) {
                numOfCalories += lbToGainOrLoose + 3500;
            } else if(m_weightGoal.equals("Loose")){
                numOfCalories += lbToGainOrLoose - 3500;
            }
        }
        else {
            //active
            numOfCalories = BMR * 1.55;

            if(m_weightGoal.equals("Gain")) {
                numOfCalories += BMR + lbToGainOrLoose + 3500;
            } else if(m_weightGoal.equals("Loose")){
                numOfCalories += BMR + lbToGainOrLoose - 3500;
            }
        }

        return numOfCalories;
    }

    private double calculateBMI() {
        final int INCHES_TO_FT = 12;

        //TODO: Input validation needs to be done here to eliminate possibility of an exception being thrown

        double heightInInches = (Double.parseDouble(m_feet) * INCHES_TO_FT) + Double.parseDouble(m_inches);
        double weightInLbs = Double.parseDouble(m_weight);

        return BmiUtils.calculateBmi(heightInInches, weightInLbs);
    }
}
package com.example.mcresswell.project01;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mcresswell.project01.util.BmiUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FitnessDetailsFragment extends Fragment {


    private String mfName, mlName, mdob, msex, mcity, mcountry, mweight, mfeet, minches, mlbsPerWeek, m_lifestyleSelection, m_weightGoal;
    private int mAge;
    private TextView m_tvcalsToEat, m_tvBMR;

    public FitnessDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        m_tvcalsToEat = (TextView) container.findViewById(R.id.tv_calPerDay);
        m_tvBMR = (TextView) container.findViewById(R.id.tv_BMR);

        return inflater.inflate(R.layout.fragment_fitness_details, container, false);
    }

    public void getDataFromBundle(Bundle userDataBundle) {
        mfName = userDataBundle.getString("fname");
        mlName = userDataBundle.getString("lname");
        mdob = userDataBundle.getString("dob");
        msex = userDataBundle.getString("sex");
        mcity = userDataBundle.getString("city");
        mcountry = userDataBundle.getString("country");
        mweight = userDataBundle.getString("weight");
        mfeet = userDataBundle.getString("feet");
        minches = userDataBundle.getString("inches");
        mAge = userDataBundle.getInt("age");
        mlbsPerWeek = userDataBundle.getString("lbsPerWeek");
        m_lifestyleSelection = userDataBundle.getString("lifestyle");
        m_weightGoal = userDataBundle.getString("weightGoal");
    }

////    http://www.bmrcalculator.org
////    BMR = (9.99 x weight + 6.25 x height – 4.92 x age + s ) kcal/day
////    Here, weight is in Kilograms, height is in centimeters and age is in years.
////    s is a factor to adjust for gender and adopts the value +5 for males and -161 for females.
    private double calculateBMR(Bundle userDataBundle) {
        getDataFromBundle(userDataBundle);

        double BMR = 0.0;

        //get weight value
        double weight = Double.parseDouble(mweight);

        //get height values
        double heightFeet = Double.parseDouble(mfeet);
        double heightInches = Double.parseDouble(minches);
        double totalHeightInInches = (heightFeet * 12.0) + heightInches;

        //get age value

        //calculate BMR based on sex of user

        if(msex.equals("Female") || msex.equals("female") || msex.equals("F") || msex.equals("f")) {
            //user is female, s = -161
            BMR = (9.99 * weight) + (6.25 * totalHeightInInches) - 4.92 * mAge - 161;
        } else {
            //user is male, s = 5
            BMR = (9.99 * weight) + (6.25 * totalHeightInInches) - 4.92 * mAge + 5;
        }


        String bmrString = "" + BMR;
        m_tvcalsToEat.setText(bmrString);

        return BMR;
    }

    //http://www.bmrcalculator.org
    //You exercise moderately (3-5 days per week)	Calories Burned a Day = BMR x 1.55
    //Low	You get little to no exercise	Calories Burned a Day = BMR x 1.2
    //In order to lose 1 pound of fat each week, you must have a deficit of 3,500 calories over the course of a week.[5]
    // https://www.wikihow.com/Calculate-How-Many-Calories-You-Need-to-Eat-to-Lose-Weight
    private double calculateCalories(Bundle userDataBundle, int lbToGainOrLoose) {
        double numOfCalories;
        double BMR = calculateBMR(userDataBundle);

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

        String calString = "" + numOfCalories;
        m_tvcalsToEat.setText(calString);

        return numOfCalories;
    }

    private double calculateBMI() {
        final int INCHES_TO_FT = 12;

        //TODO: Input validation needs to be done here to eliminate possibility of an exception being thrown

        double heightInInches = (Double.parseDouble(mfeet) * INCHES_TO_FT) + Double.parseDouble(minches);
        double weightInLbs = Double.parseDouble(mweight);

        return BmiUtils.calculateBmi(heightInInches, weightInLbs);
    }
}

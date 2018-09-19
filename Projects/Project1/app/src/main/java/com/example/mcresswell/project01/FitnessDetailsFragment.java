package com.example.mcresswell.project01;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class FitnessDetailsFragment extends Fragment {


    private String mfName, mlName, mdob, msex, mcity, mcountry, mweight, mfeet, minches;
    private int mAge;

    public FitnessDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        return BMR;
    }

    //You exercise moderately (3-5 days per week)	Calories Burned a Day = BMR x 1.55
    //Low	You get little to no exercise	Calories Burned a Day = BMR x 1.2
    private int calculateCalories(Bundle userDataBundle, int lbToGainOrLoose) {
        int numOfCalories = 0;
        double BMR = calculateBMR(userDataBundle);



        return numOfCalories;
    }

    //http://bmi.emedtv.com/bmi/how-to-calculate-bmi.html
//    Therefore, to calculate BMI, take the weight (lbs) and divide it by height (in).
//     Take the result of that calculation and divide it by height again. Then, multiply that number by 703. Round to the second decimal place.
//
//    An example of calculating body mass index using the BMI formula: Weight = 150 lbs, Height = 5'5" (65 inches)
//
//    BMI Calculation: [150 ÷ (65)2] x 703 = 24.96
    private double calculateBMI(){
        double feet = Double.parseDouble(mfeet);
        double inches = Double.parseDouble(minches);

        double heightInInches = (feet * 12) + inches;
        double weight = Double.parseDouble(mweight);

        double BMI_step1 = (weight/heightInInches);

        return (BMI_step1/heightInInches) * 703;
    }
}

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


    public FitnessDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fitness_details, container, false);
    }

////    http://www.bmrcalculator.org
////    BMR = (9.99 x weight + 6.25 x height – 4.92 x age + s ) kcal/day
////    Here, weight is in Kilograms, height is in centimeters and age is in years.
////    s is a factor to adjust for gender and adopts the value +5 for males and -161 for females.
//    private double calculateBMR() {
//        double BMR = 0.0;
//
//        //get weight value
//        double weight = Double.parseDouble(m_etxt_weight.toString());
//
//        //get height values
//        double heightFeet = Double.parseDouble(m_etxt_feet.toString());
//        double heightInches = Double.parseDouble(m_etxt_inches.toString());
//        double totalHeightInInches = (heightFeet * 12.0) + heightInches;
//
//        //get age value
//        int age = calculateAge();
//
//        //calculate BMR based on sex of user
//        String sex = m_etxt_sex.toString();
//
//        if(sex.equals("Female") || sex.equals("female") || sex.equals("F") || sex.equals("f")) {
//            //user is female, s = -161
//            BMR = (9.99 * weight) + (6.25 * totalHeightInInches) - 4.92 * age - 161;
//        } else {
//            //user is male, s = 5
//            BMR = (9.99 * weight) + (6.25 * totalHeightInInches) - 4.92 * age + 5;
//        }
//
//        return BMR;
//    }

}

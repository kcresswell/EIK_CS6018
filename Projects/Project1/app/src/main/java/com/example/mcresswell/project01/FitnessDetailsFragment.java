package com.example.mcresswell.project01;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mcresswell.project01.util.UserProfileUtils;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class FitnessDetailsFragment extends Fragment {

    private static final String LOG = FitnessDetailsFragment.class.getSimpleName();

    private static final String DEFAULT_CALORIES = "2000 calories";
    private static final String DEFAULT_BMR = "1500 calories";
    private static final int DEFAULT_HEIGHT = 65;
    private static final int DEFAULT_WEIGHT = 120;

    private TextView m_tvcalsToEat, m_tvBMR, bodyMassIndex;


    public FitnessDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fitness_details, container, false);
        m_tvcalsToEat =  view.findViewById(R.id.tv_calPerDay);
        m_tvBMR = view.findViewById(R.id.tv_BMR);
//        bodyMassIndex = view.findViewById(R.id.tv_bmi);
        String calories = String.valueOf(getArguments() == null ? DEFAULT_CALORIES :
                getArguments().getDouble("calories"));
        String bmr = String.valueOf(getArguments() == null ? DEFAULT_BMR : getArguments().getDouble("bmr"));
        String bmi = String.format(Locale.US, "%.1f", UserProfileUtils.calculateBmi(DEFAULT_HEIGHT, DEFAULT_WEIGHT));
        m_tvcalsToEat.setText(calories);
        m_tvBMR.setText(bmr);
//        bodyMassIndex.setText(bmi);
        return view;
    }

}

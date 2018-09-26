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

    private static final String DEFAULT_CALORIES = "2000 calories";
    private static final String DEFAULT_BMR = "1500 calories";
    private TextView m_tvcalsToEat, m_tvBMR;

    public FitnessDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fitness_details, container, false);

        m_tvcalsToEat = view.findViewById(R.id.tv_calPerDay);
        m_tvBMR = view.findViewById(R.id.tv_BMR);
        m_tvcalsToEat.setText(DEFAULT_CALORIES);
        m_tvBMR.setText(DEFAULT_BMR);
        return view;

    }
}

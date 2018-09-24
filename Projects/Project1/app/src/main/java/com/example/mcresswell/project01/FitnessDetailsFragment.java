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
}
package com.example.mcresswell.project01;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mcresswell.project01.util.BmiUtils;
import com.example.mcresswell.project01.util.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class FitnessDetailsFragment extends Fragment {

    private static final String LOG = FitnessDetailsFragment.class.getSimpleName();

    private TextView m_tvcalsToEat, m_tvBMR; //Calculated calorie and bmr data
    private onFitnessDetailsInteractionListener listener;

    public FitnessDetailsFragment() {
        // Required empty public constructor
    }

    public static FitnessDetailsFragment newInstance(double numCalories, double bmr) {
        Log.d(LOG, Constants.NEW);
        FitnessDetailsFragment fragment = new FitnessDetailsFragment();
        Bundle args = new Bundle();
        args.putDouble("calories", numCalories);
        args.putDouble("bmr", bmr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fitness_details, container, false);
        m_tvcalsToEat =  view.findViewById(R.id.tv_calPerDay);
        m_tvBMR = view.findViewById(R.id.tv_BMR);

        String calories = String.valueOf(getArguments().getDouble("calories"));
        String bmr = String.valueOf(getArguments().getDouble("bmr"));
        m_tvcalsToEat.setText(calories);
        m_tvBMR.setText(bmr);
        return view;
    }

    public interface onFitnessDetailsInteractionListener {
        void onFitnessDetailsFragmentInteraction();
    }
}

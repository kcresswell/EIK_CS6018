package com.example.mcresswell.project01;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FitnessDetailsActivity extends AppCompatActivity
    implements FitnessDetailsFragment.onFitnessDetailsInteractionListener {

    public static final double DEFAULT_CALORIES = 2000;
    public static final double DEFAULT_BASAL_METABOLIC_RATE = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        double calsToConsume = getIntent().getDoubleExtra("calories", DEFAULT_CALORIES);
        double calculatedBmr = getIntent().getDoubleExtra("bmr", DEFAULT_BASAL_METABOLIC_RATE);

        FragmentManager manager = getSupportFragmentManager();

        FitnessDetailsFragment fragment = savedInstanceState == null ?
              FitnessDetailsFragment.newInstance(calsToConsume, calculatedBmr):
                (FitnessDetailsFragment) manager.findFragmentById(R.id.fl_master_nd_activity_fitness);

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(fragment, null);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onFitnessDetailsFragmentInteraction() {

    }
}

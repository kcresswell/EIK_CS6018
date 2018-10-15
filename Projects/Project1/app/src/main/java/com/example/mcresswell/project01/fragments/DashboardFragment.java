package com.example.mcresswell.project01.fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mcresswell.project01.DashButton;
import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.RV_Adapter;
import com.example.mcresswell.project01.ViewModels.FitnessProfileViewModel;

import java.util.ArrayList;

import static com.example.mcresswell.project01.util.Constants.CREATE;
import static com.example.mcresswell.project01.util.Constants.CREATE_VIEW;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private static final String LOG_TAG = DashboardFragment.class.getSimpleName();

    private RecyclerView m_RecyclerView;
    private RecyclerView.Adapter m_Adaptor;
    private RecyclerView.LayoutManager layoutManager;
    private FitnessProfileViewModel fitnessProfileViewModel;


    public DashboardFragment() { }

    @Override
    public void onCreate(Bundle bundle) {
        Log.d(LOG_TAG, CREATE);
        super.onCreate(bundle);

        //TODO: ONCE FITNESS PROFILE IS WORKING, IMPLEMENT THE METHOD getExistingFitnessProfile(int fitnessProfileId) and then uncomment the code below
        //TODO: THIS WILL ALLOW AN EXISTING USER'S FITNESS PROFILE DATA TO LOAD AFTER THEY LOG_TAGIN
//        if (getArguments() != null && getArguments().getInt("id") != 0) { //i.e., if existing user has logged in
//            fitnessProfileViewModel.getExistingFitnessProfile(getArguments().getInt("id")); //Method that makes
//        }
//
//        //TODO: implement call to fitnessProfileViewModel.loadExistingFitnessProfile(int fitnessProfileId) that retrieves existing fitnessProfile for user after they login
//        fitnessProfileViewModel = ViewModelProviders.of(this).get(FitnessProfileViewModel.class);
//        fitnessProfileViewModel.getFitnessProfile().observe(this, fitnessProfile -> {
//            if (fitnessProfile != null) {
//                Log.d(LOG_TAG, "Update to fitness profile view model");
//                Log.d(LOG_TAG, String.format("Fitness profile for %s\t%s:\tDOB:%s\tSEX:%s\tCITY:%s\tCOUNTRY:%s",
//                        fitnessProfile.getM_fName(),
//                        fitnessProfile.getM_lName(),
//                        fitnessProfile.getM_dob(),
//                        fitnessProfile.getM_sex(),
//                        fitnessProfile.getM_city(),
//                        fitnessProfile.getM_country()));
//            }
//        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, CREATE_VIEW);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //get recycler view
        m_RecyclerView = (RecyclerView) view.findViewById(R.id.rcycV_List);

        //fix size of view
        m_RecyclerView.setHasFixedSize(true);

        //set layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        m_RecyclerView.setLayoutManager(layoutManager);

        //needs to be implemented
        ArrayList<DashButton> buttons = new ArrayList<>();

        //build image for button
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_fitness, null), "Fitness"));
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_hike, null), "Hikes"));
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_profile, null), "Profile"));
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_weather, null), "Weather"));

        m_Adaptor = new RV_Adapter(buttons);

        if(!getResources().getBoolean(R.bool.isWideDisplay)){
            m_RecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }

        m_RecyclerView.setAdapter(m_Adaptor);

        return view;
    }

    public void getButtonClickedPosition() {
        notify();
        m_RecyclerView.getChildAdapterPosition(this.m_RecyclerView);
    }



}

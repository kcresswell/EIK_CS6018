package com.example.mcresswell.project01.fragments;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.activities.AccountSettingsActivity;
import com.example.mcresswell.project01.activities.LoginActivity;
import com.example.mcresswell.project01.db.entity.Weather;
import com.example.mcresswell.project01.ui.DashButton;
import com.example.mcresswell.project01.ui.RV_Adapter;
import com.example.mcresswell.project01.viewmodel.FitnessProfileViewModel;
import com.example.mcresswell.project01.viewmodel.UserViewModel;
import com.example.mcresswell.project01.viewmodel.WeatherListViewModel;
import com.example.mcresswell.project01.viewmodel.WeatherViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.example.mcresswell.project01.util.Constants.CREATE;
import static com.example.mcresswell.project01.util.Constants.CREATE_VIEW;
import static com.example.mcresswell.project01.util.Constants.ON_CLICK;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private static final String LOG_TAG = DashboardFragment.class.getSimpleName();

    private RecyclerView m_RecyclerView;
    private RecyclerView.Adapter m_Adaptor;
    private RecyclerView.LayoutManager layoutManager;
    private FitnessProfileViewModel fitnessProfileViewModel;
    private UserViewModel userViewModel;
    private WeatherViewModel weatherViewModel;
    private WeatherListViewModel weatherListViewModel;


    private Button m_logoutButton, m_settingsButton;


    public DashboardFragment() { }

    @Override
    public void onCreate(Bundle bundle) {
        Log.d(LOG_TAG, CREATE);

        super.onCreate(bundle);

        initializeViewModels();

        initializeWeatherListViewModel();

    }

    private void initializeWeatherListViewModel() {
        weatherListViewModel = ViewModelProviders.of(getActivity()).get(WeatherListViewModel.class);
        Observer weatherListObserver = new Observer<List<Weather>>() {
            @Override
            public void onChanged(@Nullable List<Weather> weatherList) {
                if (weatherList != null) {

                    weatherListViewModel.getWeatherDataFromDatabase().removeObserver(this);

                    ArrayList<Integer> idList = new ArrayList<>();
                    Log.d(LOG_TAG, "Update to weather list view model");
                    Log.d(LOG_TAG, "Number of weather records in Weather database: " + weatherList.size());
                    Log.d(LOG_TAG, "------------------------------------------");
                    Log.d(LOG_TAG, "PRINTING WEATHER RECORDS IN WEATHER DATABASE");
                    Log.d(LOG_TAG, "\n");
                    weatherList.forEach(weather -> {
                        idList.add(weather.getId());
                        Log.d(LOG_TAG, "\nWeather Data record: " + weather.getId() + "\t'" +
                                weather.getCity() + "'\t'" + weather.getCountryCode() + "'\t'" + weather.getLastUpdated() + "'");
                    });
                    Log.d(LOG_TAG, "\n");
                    Log.d(LOG_TAG, "------------------------------------------");
                }
            }
        };

        weatherListViewModel.getWeatherDataFromDatabase().observe(this, weatherListObserver);

    }

    private void initializeViewModels() {
        fitnessProfileViewModel = ViewModelProviders.of(getActivity()).get(FitnessProfileViewModel.class);
        userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);
        weatherViewModel = ViewModelProviders.of(getActivity()).get(WeatherViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, CREATE_VIEW);

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initializeRecyclerViewAdapater(view);

        initializeFragmentView(view);

        setOnClickListeners();

        return view;
    }

    private void initializeRecyclerViewAdapater(View view) {
        m_RecyclerView = view.findViewById(R.id.rcycV_List);
        m_RecyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        m_RecyclerView.setLayoutManager(layoutManager);

        ArrayList<DashButton> buttons = new ArrayList<>();

        //build image for button
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_fitness, null), "FITNESS"));
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_hike, null), "HIKING"));
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_profile, null), "PROFILE"));
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_weather, null), "WEATHER"));

        m_Adaptor = new RV_Adapter(buttons);

        if(!getResources().getBoolean(R.bool.isWideDisplay)){
            m_RecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        m_RecyclerView.setAdapter(m_Adaptor);
    }

    private void initializeFragmentView(View view){
        m_logoutButton = view.findViewById(R.id.btn_logout);
        m_settingsButton = view.findViewById(R.id.btn_settings);
    }

    private void setOnClickListeners() {
        m_logoutButton.setOnClickListener(listener -> logoutButtonHandler());
        m_settingsButton.setOnClickListener(listener -> settingsButtonHandler());
    }

    private void settingsButtonHandler() {
        Log.d(LOG_TAG, ON_CLICK);
        FragmentTransaction m_fTrans = getActivity().getSupportFragmentManager().beginTransaction();
        if (!getResources().getBoolean(R.bool.isWideDisplay)) {
            Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
            startActivityForResult(intent, Activity.RESULT_OK);
        } else {
            m_fTrans.replace(R.id.fl_detail_wd, new AccountSettingsFragment());
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    private void logoutButtonHandler() {
        Log.d(LOG_TAG, ON_CLICK);

        FragmentTransaction m_fTrans = getActivity().getSupportFragmentManager().beginTransaction();
        if (!getResources().getBoolean(R.bool.isWideDisplay)) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, Activity.RESULT_OK);
        } else {
            m_fTrans.replace(R.id.fl_detail_wd, new LoginFragment());
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }

        resetViewModelData();
    }

    private void resetViewModelData() {
        fitnessProfileViewModel = null;
        userViewModel = null;
        weatherViewModel = null;
    }

}

package com.example.mcresswell.project01.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.activities.DashboardActivity;
import com.example.mcresswell.project01.activities.ProfileEntryActivity;
import com.example.mcresswell.project01.util.Constants;
import com.example.mcresswell.project01.viewmodel.FitnessProfileViewModel;
import com.example.mcresswell.project01.viewmodel.UserViewModel;

import java.util.Locale;

import retrofit2.http.HEAD;

import static com.example.mcresswell.project01.util.Constants.CREATE_VIEW;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateAge;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateBMR;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateBmi;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateDailyCaloricIntake;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateHeightInInches;

public class FitnessDetailsFragment extends Fragment {

    private static final String LOG_TAG = FitnessDetailsFragment.class.getSimpleName();

    private static final double DEFAULT_CALS = 2000;
    private static final double DEFAULT_BMR = 1500;
    private static final double DEFAULT_BMI = 20.0;
    private static final int STEP_COUNT_PLACEHOLDER = 789; //Temp placeholder for step count

    private static final String DOUBLE_FORMAT = "%.1f";
    private static final String INT_FORMAT = "%d";

    private static final String CALORIC_INTAKE = " calories/day";
    private static final String BMR = " calories/day";
    private static final String STEPS = " steps";

    private TextView m_tvcalsToEat, m_tvBMR, m_bodyMassIndex, m_tvstepCount, fitnessDetailsHeading, fitnessDetailsSubHeading;
    private TextView  m_tvbmiClassification; //Implement this later when the fitness profile is working
    private FitnessProfileViewModel m_fitnessProfileViewModel;
    private UserViewModel m_userViewModel;

    private SensorManager mSensorManager;
    private Sensor mStepCounter;
    private float m_numberOfSteps;
<<<<<<< HEAD

=======
>>>>>>> b0e54e5f654342fc002ac4f68415a85096e80ecc

    public FitnessDetailsFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, Constants.CREATE);
        super.onCreate(savedInstanceState);
    }

    private final SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
<<<<<<<HEAD
            m_tvstepCount.setText("" + String.valueOf(sensorEvent.values[0]));
            m_numberOfSteps = sensorEvent.values[0];
=======
            m_numberOfSteps = (int) sensorEvent.values[0];
            Log.d(LOG_TAG, String.format(Locale.US, "Total step count: %d steps", m_numberOfSteps));
            m_tvstepCount.setText(String.format(Locale.US, "%d %s", m_numberOfSteps, STEPS));
>>>>>>> b0e54e5f654342fc002ac4f68415a85096e80ecc
            m_fitnessProfileViewModel.setStepCount(m_numberOfSteps);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "onResume");
        super.onResume();
        if(mStepCounter!=null){
            mSensorManager.registerListener(mListener,mStepCounter,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "onPause");
        super.onPause();
        if(mStepCounter!=null){
            mSensorManager.unregisterListener(mListener);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, CREATE_VIEW);

        View view = inflater.inflate(R.layout.fragment_fitness_details, container, false);

        m_tvcalsToEat = view.findViewById(R.id.tv_calPerDay);
        m_tvBMR = view.findViewById(R.id.tv_BMR);
        m_bodyMassIndex = view.findViewById(R.id.tv_bmi);
        m_tvstepCount = view.findViewById(R.id.tv_step_count);
        fitnessDetailsHeading = view.findViewById(R.id.fitness_details_main_header);
        fitnessDetailsSubHeading = view.findViewById(R.id.fitness_details_sub_header);

        m_tvcalsToEat.setText(String.format(Locale.US, DOUBLE_FORMAT + CALORIC_INTAKE, DEFAULT_CALS));
        m_tvBMR.setText(String.format(Locale.US, DOUBLE_FORMAT + BMR, DEFAULT_BMR));
        m_bodyMassIndex.setText(String.format(Locale.US, DOUBLE_FORMAT, DEFAULT_BMI));

        mSensorManager = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);
        mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        m_numberOfSteps = Float.valueOf(m_numberOfSteps) == null ? 0 : m_numberOfSteps;
        m_tvstepCount.setText(String.format(Locale.US, "%s %s", m_numberOfSteps, STEPS));
        fitnessDetailsHeading.setText(String.format(Locale.US,"INSTYLE"));
        fitnessDetailsSubHeading.setText(String.format(Locale.US, "FITNESS DATA"));

<<<<<<< HEAD
            //null check for sensor count
            if(Float.valueOf(m_numberOfSteps) == null) {
//                m_tvstepCount.setText(String.format(Locale.US, INT_FORMAT + STEPS, 0));
                m_numberOfSteps = 0;
            } else {
                m_tvstepCount.setText(String.format(Locale.US, DOUBLE_FORMAT + STEPS, m_numberOfSteps));
            }
        } else {
            double caloricIntake = calculateCalories(m_fitnessProfile);
            m_tvcalsToEat.setText(String.format(Locale.US,"%.1f calories", caloricIntake));
            m_tvBMR.setText(String.format(Locale.US, "%.1f calories/day", m_fitnessProfile.getM_bmr()));
            m_bodyMassIndex.setText(String.format(Locale.US, "%.1f", m_fitnessProfile.getM_bmi()));
        }
=======
>>>>>>> b0e54e5f654342fc002ac4f68415a85096e80ecc

        configureViewModels();

        return view;
    }

    private void configureViewModels() {
        m_fitnessProfileViewModel = ViewModelProviders.of(getActivity()).get(FitnessProfileViewModel.class);

        m_userViewModel = ViewModelProviders.of(getActivity()).get(UserViewModel.class);

        m_userViewModel.getUser().observe(this, user -> {
            Log.d(LOG_TAG, "UserViewModel observer for getUser()");
            if (user != null) {
                m_fitnessProfileViewModel.getFitnessProfile(user.getId()).observe(this, fp -> {
                    if (fp != null) {
                        //testing if writing to database
//                        m_fitnessProfileViewModel.setStepCount(8.0f);
                        int calcAge = calculateAge(fp.getM_dob());
                        double basalMetabolicRate = calculateBMR(fp.getM_heightFeet(),
                                fp.getM_heightInches(),
                                fp.getM_sex(),
                                fp.getM_weightInPounds(),
                                calcAge);
                        double bodyMassIndex = calculateBmi(calculateHeightInInches(
                                fp.getM_heightFeet(),
                                fp.getM_heightInches()),
                                fp.getM_weightInPounds());

//                        SpannableString spannableString = new SpannableString(String.format(Locale.US,"FITNESS DATA FOR %s", fp.getM_fName()));
//                        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 16,
//                                spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                        spannableString.setSpan(new RelativeSizeSpan(2.0f), 16, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        fitnessDetailsSubHeading.setText(String.format(Locale.US,"FITNESS DATA FOR %s", fp.getM_fName()));
                        m_numberOfSteps = fp.getM_stepCount();
                        m_tvcalsToEat.setText(String.format(Locale.US, DOUBLE_FORMAT + CALORIC_INTAKE, calculateDailyCaloricIntake(fp)));
                        m_tvBMR.setText(String.format(Locale.US, DOUBLE_FORMAT + BMR, basalMetabolicRate));
                        m_bodyMassIndex.setText(String.format(Locale.US, DOUBLE_FORMAT, bodyMassIndex));
<<<<<<< HEAD
                        m_tvstepCount.setText(String.format(Locale.US, DOUBLE_FORMAT + STEPS, m_numberOfSteps));

=======
                        m_tvstepCount.setText(String.format(Locale.US, "%s %s",m_numberOfSteps, STEPS));
                    } else {
                        displayNoExistingFitnessProfileAlertDialog();
>>>>>>> b0e54e5f654342fc002ac4f68415a85096e80ecc
                    }
                });

            } else {
                displayNoExistingFitnessProfileAlertDialog();
            }
        });
    }

    private void displayNoExistingFitnessProfileAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_message);
        builder.setIcon(R.drawable.ic_directions_run);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(LOG_TAG, "Dialog OK button clicked");
                viewTransitionHandler();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Log.d(LOG_TAG, "Dialog cancel button clicked");
                //Go back to dashboard but display weather fragment instead of fitness details to prevent loop
                if (!getResources().getBoolean(R.bool.isWideDisplay)) {
                    Intent intent = new Intent(getContext(), DashboardActivity.class);
                    startActivity(intent);
                } else {
                    FragmentTransaction m_fTrans = getActivity().getSupportFragmentManager().beginTransaction();
                    m_fTrans.replace(R.id.fl_master_wd, new DashboardFragment(), "v_frag_dashboard");
                    m_fTrans.replace(R.id.fl_detail_wd, new WeatherFragment(), "v_frag_fitness");
//            m_fTrans.replace(R.id.fl_master_, new ProfileEntryFragment(), "v_frag_profile");
                    m_fTrans.commit();
                }
            }
        });

        builder.create().show();
    }

    private void viewTransitionHandler() {
        if (!getResources().getBoolean(R.bool.isWideDisplay)) {
            Intent intent = new Intent(getContext(), ProfileEntryActivity.class);
            startActivity(intent);
        } else {
            FragmentTransaction m_fTrans = getActivity().getSupportFragmentManager().beginTransaction();
            m_fTrans.replace(R.id.fl_master_wd, new DashboardFragment(), "v_frag_dashboard");
            m_fTrans.replace(R.id.fl_detail_wd, new ProfileEntryFragment(), "v_frag_fitness");
//            m_fTrans.replace(R.id.fl_master_, new ProfileEntryFragment(), "v_frag_profile");
            m_fTrans.commit();
        }
    }
}

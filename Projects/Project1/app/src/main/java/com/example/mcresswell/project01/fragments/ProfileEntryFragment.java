package com.example.mcresswell.project01.fragments;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.activities.ProfileSummaryActivity;
import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.viewmodel.FitnessProfileViewModel;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.util.Constants;
import com.example.mcresswell.project01.viewmodel.UserViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import static com.example.mcresswell.project01.util.ValidationUtils.isNotNullOrEmpty;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidCity;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidDobFormat;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidHeight;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidName;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidSex;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidWeight;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidWeightPlan;
import static com.example.mcresswell.project01.util.mapper.CountryCodeMapper.getCountryNames;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEntryFragment extends Fragment implements View.OnClickListener {
    private static final String LOG_TAG = ProfileEntryFragment.class.getSimpleName();

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private FitnessProfileViewModel m_fitnessProfileViewModel;
    private UserViewModel userViewModel;

    //UI Elements
    private EditText etxt_firstName, etxt_lastName, etxt_dob, etxt_sex, etxt_city,
            etxt_weight, etxt_heightFeet, etxt_heightInches, etxt_lbsPerWeek;
    private Button profileEntryButton;
    private ImageButton takeProfileImageButton;
    private Bitmap profileImage;
    private RadioGroup lifestyleSelector;
    private RadioButton activeLifestyle, sedentaryLifestyle;
    private RadioGroup weightGoal;
    private RadioButton gain, maintain, lose;
    private Spinner countrySpinner;
    private SeekBar lifestyleSlider, weightGoalSlider;

    //Data Elements
    private FitnessProfile m_fitnessProfile; //FIXME: YOU DONT NEED MEMBER VARIABLES FOR THE FITNESS PROFILE OR THE USER, THEY WONT SURVIVE CONFIGURATION CHANGES. THATS THE WHOLE POINT OF THE VIEW MODELS.
    private User m_user; //FIXME: YOU DONT NEED MEMBER VARIABLES FOR THE FITNESS PROFILE OR THE USER, THEY WONT SURVIVE CONFIGURATION CHANGES. THATS THE WHOLE POINT OF THE VIEW MODELS.
    private String lifestyleSelectorString = "Active"; //Default lifestyle selector of 'Active' if no radio button selected
    private String weightGoalString = "Lose"; //Default weight goal of 'Lose' if no radio button is selected

    public ProfileEntryFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, Constants.CREATE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, Constants.CREATE_VIEW);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_entry, container, false);

        initializeViewElements(view);

        setOnClickListeners();

        configureViewModels();

        return view;
    }

    private void setOnClickListeners() {
        profileEntryButton.setOnClickListener(this);
        takeProfileImageButton.setOnClickListener(this);
        activeLifestyle.setOnClickListener(this);
        sedentaryLifestyle.setOnClickListener(this);
        gain.setOnClickListener(this);
        maintain.setOnClickListener(this);
        lose.setOnClickListener(this);
        etxt_dob.setOnClickListener(l->displayDatePickerDialog());
    }

    private void configureViewModels() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        m_fitnessProfileViewModel =
                ViewModelProviders.of(this).get(FitnessProfileViewModel.class);

        userViewModel.getUser().observe(this, user -> {
            Log.d(LOG_TAG, "UserViewModel observer for getUser()");
            if (user != null) { //Autofill first and last name passed from create account page
                etxt_firstName.setText(user.getFirstName());
                etxt_lastName.setText(user.getLastName());

                observeFitnessProfileViewModel(user);

            }
        });

    }

    private void observeFitnessProfileViewModel(User user) {
        m_fitnessProfileViewModel.getFitnessProfile(user.getId()).observe(this, fp -> {
            if (fp != null && fp.getUserId() == user.getId()) {
                autofillExistingFitnessProfileData(fp);
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        Log.d(LOG_TAG, "onClick");
        switch (view.getId()){
            case R.id.btn_img_takeImage: {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(cameraIntent.resolveActivity(getActivity().getPackageManager()) != null){
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            }
            case R.id.btn_submit: {
                userProfileSubmitButtonHandler();
                break;
            }
            case R.id.btn_radio_active: {
                lifestyleSelectorString = "Active";
                break;
            }
            case R.id.btn_radio_sedentary: {
                lifestyleSelectorString = "Sedentary";
                break;
            }
            case R.id.btn_radio_gain: {
                weightGoalString = "Gain";
                break;
            }
            case R.id.btn_radio_maintain: {
                weightGoalString = "Maintain";
                break;
            }
            case R.id.btn_radio_lose: {
                weightGoalString = "Lose";
                break;
            }
            case R.id.txtv_dob: {
                displayDatePickerDialog();
                break;
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void displayDatePickerDialog() {
        Log.d(LOG_TAG, "clicked dob field");
        Date defaultDate = Date.from(Instant.now());
        if (isValidDobFormat(etxt_dob.getText().toString())) {
            try {
                defaultDate = new SimpleDateFormat("MM/dd/yyyy").parse(etxt_dob.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(defaultDate);

        Log.d(LOG_TAG, String.format("Default date set on dialog: '%s'", defaultDate.toString()));

        DatePickerDialog dialog = new DatePickerDialog(getContext(), android.R.style.Theme_Holo_Dialog, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                Log.d(LOG_TAG, String.format(Locale.US, "birthdate was set: %d-%d-%d",
                        year, month, day));

                etxt_dob.setText(String.format(Locale.US, "%d/%d/%d", month+1, day, year));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.show();
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
            super.onActivityResult(requestCode, resultCode, intent);
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK){
                if (intent != null && intent.getExtras() != null) {
                    profileImage = (Bitmap) intent.getExtras().get("data");
                }
                if (profileImage != null) {
                    takeProfileImageButton.setImageBitmap(profileImage);
                }
            }
        }

        @Override
        public void onViewStateRestored (Bundle savedInstanceState) {
            Log.d(LOG_TAG, "onViewStateRestored");

            super.onViewStateRestored(savedInstanceState);

            if(savedInstanceState != null) {
                profileImage = savedInstanceState.getParcelable("M_IMG_DATA");
                takeProfileImageButton.setImageBitmap(profileImage);
            }
        }

        private void initializeViewElements(View view) {
            etxt_firstName = view.findViewById(R.id.txtv_fname);
            etxt_lastName = view.findViewById(R.id.txtv_lname);
            etxt_dob = view.findViewById(R.id.txtv_dob);
            etxt_sex = view.findViewById(R.id.txtv_sex);
            etxt_city = view.findViewById(R.id.txtv_city);
            etxt_weight = view.findViewById(R.id.txtv_weight);
            etxt_heightFeet = view.findViewById(R.id.txtv_feet);
            etxt_heightInches = view.findViewById(R.id.txtv_inches);
            etxt_lbsPerWeek = view.findViewById(R.id.txtv_weight_weekly_goal);

            lifestyleSelector = view.findViewById(R.id.radiogp_lifestyle);
            weightGoal = view.findViewById(R.id.radiogp_weightGoal);

            activeLifestyle = view.findViewById(R.id.btn_radio_active);
            sedentaryLifestyle = view.findViewById(R.id.btn_radio_sedentary);
            gain = view.findViewById(R.id.btn_radio_gain);
            maintain = view.findViewById(R.id.btn_radio_maintain);
            lose = view.findViewById(R.id.btn_radio_lose);

            profileEntryButton = view.findViewById(R.id.btn_submit);
            takeProfileImageButton = view.findViewById(R.id.btn_img_takeImage);

            countrySpinner = view.findViewById(R.id.spinner_country);

            ArrayList<String> countryOptions = getCountryNames();
            countryOptions.add(0, "United States");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                    android.R.layout.simple_spinner_item,
                    countryOptions);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countrySpinner.setAdapter(adapter);

            lifestyleSlider = view.findViewById(R.id.slider_lifestyle);
            lifestyleSlider.setMin(0);
            lifestyleSlider.setMax(2);
        }

        private void autofillExistingFitnessProfileData(FitnessProfile fp) {
            Log.d(LOG_TAG, "Autofilling existing FitnessProfile data");

            etxt_firstName.setText(fp.getM_fName());
            etxt_lastName.setText(fp.getM_lName());
            etxt_dob.setText(fp.getM_dob());
            etxt_sex.setText(fp.getM_sex());
            etxt_heightFeet.setText(String.valueOf(fp.getM_heightFeet()));
            etxt_heightInches.setText(String.valueOf(fp.getM_heightInches()));
            etxt_city.setText(fp.getM_city());

            int pos = getCountryNames().indexOf(fp.getM_country());
            countrySpinner.setSelection(pos);

            etxt_weight.setText(String.valueOf(fp.getM_weightInPounds()));
            etxt_lbsPerWeek.setText(String.valueOf(fp.getM_lbsPerWeek()));

            restoreRadioButtonSelections(fp);
        }

        private void restoreRadioButtonSelections(FitnessProfile fp) {
            if (fp.getM_lifestyleSelection().equalsIgnoreCase("ACTIVE")) {
                lifestyleSelector.check(R.id.btn_radio_active);
            } else {
                lifestyleSelector.check(R.id.btn_radio_sedentary);
            }

            if (fp.getM_lbsPerWeek() > 0) {
                assert(fp.getM_weightGoal().equalsIgnoreCase("GAIN"));
                weightGoal.check(R.id.btn_radio_gain);
            }
            else if (fp.getM_lbsPerWeek() == 0) {
                assert(fp.getM_weightGoal().equalsIgnoreCase("MAINTAIN"));
                weightGoal.check(R.id.btn_radio_maintain);
            } else {
                assert(fp.getM_weightGoal().equalsIgnoreCase("LOSE"));
                weightGoal.check(R.id.btn_radio_lose);
                etxt_lbsPerWeek.setText(String.valueOf(Math.abs(fp.getM_lbsPerWeek())));
            }
        }

        @TargetApi(Build.VERSION_CODES.O)
        @RequiresApi(api = Build.VERSION_CODES.N)
        private void userProfileSubmitButtonHandler() {
            Log.d(LOG_TAG, "User Profile Entry Done Button clicked");

            if (!isUserInputDataValid()) {
                Log.d(LOG_TAG, "invalid user data input");

                return;
            }

            FitnessProfile tempFitnessProfile = instantiateFitnessProfile();

            userViewModel.getUser().observe(this, user -> {
                if (user != null) {
                    Log.d(LOG_TAG, String.format(
                            "HURRAH, WE CAN NOW CREATE THE FITNESS PROFILE RECORD WITH THE " +
                                    "CORRECT VALUE OF THE USER ID, USER ID = %d", user.getId()));

                    tempFitnessProfile.setUserId(user.getId());

                    m_fitnessProfileViewModel.insertNewFitnessProfile(tempFitnessProfile);

                    //Verify that the fitnessProfile record for the user was successfully inserted into the database table
                    m_fitnessProfileViewModel.getFitnessProfile(user.getId()).observe(this, fitnessProfile -> {
                        if (fitnessProfile != null) {
                            Log.d(LOG_TAG, "Successfully retrieved fitness profile after insertion into database");
                            Log.d(LOG_TAG, "Fitness profile data:");
                            Log.d(LOG_TAG, "First name: " + fitnessProfile.getM_fName());
                            Log.d(LOG_TAG, "Last name: " + fitnessProfile.getM_lName());
                            Log.d(LOG_TAG, "User id: " + fitnessProfile.getUserId());
                            Log.d(LOG_TAG, "Sex: '" + fitnessProfile.getM_sex() + "'");
                            Log.d(LOG_TAG, "Height: " + fitnessProfile.getM_heightFeet() + "Ft " + fitnessProfile.getM_heightInches() + " Inches");

                        }
                    });


                }
            });

            viewTransitionHandler();
        }

        @NonNull
        private FitnessProfile instantiateFitnessProfile() {
            FitnessProfile tempFitnessProfile = new FitnessProfile();
            tempFitnessProfile.setM_fName(etxt_firstName.getText().toString());
            tempFitnessProfile.setM_lName(etxt_lastName.getText().toString());
            tempFitnessProfile.setM_dob(etxt_dob.getText().toString());
            tempFitnessProfile.setM_sex(etxt_sex.getText().toString());
            tempFitnessProfile.setM_city(etxt_city.getText().toString());

            tempFitnessProfile.setM_country(getCountryNames().get(countrySpinner.getSelectedItemPosition()));
            tempFitnessProfile.setM_lifestyleSelection(lifestyleSelectorString);
            tempFitnessProfile.setM_weightGoal(weightGoalString);
            tempFitnessProfile.setM_weightInPounds(Integer.parseInt(etxt_weight.getText().toString()));
            tempFitnessProfile.setM_heightFeet(Integer.parseInt(etxt_heightFeet.getText().toString()));

            String heightInches = etxt_heightInches.getText().toString();
            tempFitnessProfile.setM_heightInches(!isNotNullOrEmpty(heightInches) ? 0 : Integer.parseInt(heightInches));

            final int lbsPerWeek = Integer.parseInt(etxt_lbsPerWeek.getText().toString());

            if (weightGoalString.equalsIgnoreCase("Maintain")) {
                tempFitnessProfile.setM_lbsPerWeek(0); //If they're trying to maintain, ignore the lbs/week field, this should be 0
            }
            else if (weightGoalString.equalsIgnoreCase("Lose")) { //If they're trying to lose weight, set this to be negative
                tempFitnessProfile.setM_lbsPerWeek(-1 * lbsPerWeek);
            }
            else {
                tempFitnessProfile.setM_lbsPerWeek(lbsPerWeek);
            }
            return tempFitnessProfile;
        }

        private void viewTransitionHandler() {
            if (!getResources().getBoolean(R.bool.isWideDisplay)){
                Intent intent = new Intent(getContext(), ProfileSummaryActivity.class);
                startActivity(intent);
            } else {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fl_detail_wd, new ProfileSummaryFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }

        private boolean isUserInputDataValid() {
            if (!isValidName(etxt_firstName.getText().toString())) {
                Toast.makeText(getContext(), "Invalid first name.", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (!isValidName(etxt_lastName.getText().toString())) {
                Toast.makeText(getContext(), "Invalid last name.", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (!isValidDobFormat(etxt_dob.getText().toString())) {
                Toast.makeText(getContext(), "Invalid date of birth.", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (!isValidSex(etxt_sex.getText().toString())) {
                Toast.makeText(getContext(), "Invalid sex.", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (!isValidCity(etxt_city.getText().toString())) {
                Toast.makeText(getContext(), "Invalid city.", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (!isValidWeight(etxt_weight.getText().toString())) {
                Toast.makeText(getContext(), "Invalid weight.", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (!isValidHeight(etxt_heightFeet.getText().toString(), etxt_heightInches.getText().toString())) {
                Toast.makeText(getContext(), "Please enter a valid height in feet/inches.", Toast.LENGTH_SHORT).show();
                return false;
            }
            else if (!isValidWeightPlan(etxt_lbsPerWeek.getText().toString())) {
                Toast.makeText(getContext(), "Please enter a valid weekly weight goal (i.e., a maximum weight management change of 5lbs/week)", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
        }

    }

package com.example.mcresswell.project01.fragments;


import android.annotation.TargetApi;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.ViewModels.FitnessProfileViewModel;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.util.Constants;

import java.util.HashMap;
import java.util.Map;

import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateAge;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateBMR;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateBmi;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateHeightInInches;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.printUserProfileData;
import static com.example.mcresswell.project01.util.ValidationUtils.isNotNullOrEmpty;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidAlphaChars;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidCity;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidCountryCode;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidDobFormat;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidHeight;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidSex;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidWeight;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidWeightPlan;
/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEntryFragment extends Fragment implements View.OnClickListener {

    private static final String LOG = ProfileEntryFragment.class.getSimpleName();

    //request code for camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private OnProfileEntryFragmentListener m_dataListener;
    private FitnessProfileViewModel m_fitnessProfileViewModel;

    //UI Elements
    private EditText firstName, lastName, dob, sex, city, country,
            weight, heightFeet, heightInches, lbsPerWeek;
    private Button profileEntryButton;
    private ImageButton takeProfileImageButton;
    private Bitmap profileImage;
    private RadioGroup lifestyleSelector;
    private RadioButton activeLifestyle, sedentaryLifestyle;
    private RadioGroup weightGoal;
    private RadioButton gain, maintain, lose;

    //
    private String lifestyleSelectorString = "Active"; //Default lifestyle selector of 'Active' if no radio button selected
    private String weightGoalString = "Lose"; //Default weight goal of 'Lose' if no radio button is selected

    //Data fields

    private Map<String, String> userEnteredData = new HashMap<>();
    private MutableLiveData<FitnessProfile> m_fitnessProfile;

    public ProfileEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);
        super.onCreate(savedInstanceState);

        initViewModel();
        m_fitnessProfile = m_fitnessProfileViewModel.getFitnessProfile();
    }

    private void initViewModel() {
//        final Observer<FitnessProfile> fitnessProfileObserver = fitnessProfile -> m_fitnessProfile.setValue(fitnessProfile);
        m_fitnessProfileViewModel = ViewModelProviders.of(this)
                .get(FitnessProfileViewModel.class);
//        m_fitnessProfileViewModel.getFitnessProfile().observe(this, fitnessProfileObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE_VIEW);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_entry, container, false);

        initializeProfileEntryUIElements(view);

        //set buttons to listen to this class
        setButtonListeners();

        if (m_fitnessProfile != null) { //If FitnessProfile has existing data, autopopulate fields
            autofillExistingUserProfileData(m_fitnessProfile.getValue());
        }

        return view;
    }

    private void setButtonListeners() {
        profileEntryButton.setOnClickListener(this);
        takeProfileImageButton.setOnClickListener(this);
        activeLifestyle.setOnClickListener(this);
        sedentaryLifestyle.setOnClickListener(this);
        gain.setOnClickListener(this);
        maintain.setOnClickListener(this);
        lose.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        Log.d(LOG, "onClick");
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
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK){
            Bundle extras = data.getExtras();
            if (extras != null) {
                profileImage = (Bitmap) extras.get("data");
            }
            if (profileImage != null) {
                takeProfileImageButton.setImageBitmap(profileImage);
            }
        }
    }

    @Override
    public void onViewStateRestored (Bundle savedInstanceState) {
        Log.d(LOG, "onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);
        //retrieve data
        if(savedInstanceState != null) {
            profileImage = savedInstanceState.getParcelable("M_IMG_DATA");
            takeProfileImageButton.setImageBitmap(profileImage);
        }
    }

    private void initializeProfileEntryUIElements(View view) {
        //--get EditText fields, assign member variables appropriately--//
        firstName = (EditText) view.findViewById(R.id.txtv_fname);
        lastName = (EditText) view.findViewById(R.id.txtv_lname);
        dob = (EditText) view.findViewById(R.id.txtv_dob);
        sex = (EditText) view.findViewById(R.id.txtv_sex);
        city = (EditText) view.findViewById(R.id.txtv_city);
        country = (EditText) view.findViewById(R.id.txtv_country);
        weight = (EditText) view.findViewById(R.id.txtv_weight);
        heightFeet = (EditText) view.findViewById(R.id.txtv_feet);
        heightInches = (EditText) view.findViewById(R.id.txtv_inches);
        lbsPerWeek = (EditText) view.findViewById(R.id.txtv_weight2);

        //Radio Buttons
        lifestyleSelector = view.findViewById(R.id.radiogp_lifestyle);
        weightGoal = view.findViewById(R.id.radiogp_weightGoal);

        activeLifestyle = view.findViewById(R.id.btn_radio_active);
        sedentaryLifestyle = view.findViewById(R.id.btn_radio_sedentary);
        gain = view.findViewById(R.id.btn_radio_gain);
        maintain = view.findViewById(R.id.btn_radio_maintain);
        lose = view.findViewById(R.id.btn_radio_lose);

        //--get submit and image buttons--//
        profileEntryButton = (Button) view.findViewById(R.id.btn_submit);
        takeProfileImageButton = (ImageButton) view.findViewById(R.id.btn_img_takeImage);
    }

    private void autofillExistingUserProfileData(FitnessProfile fitnessProfile) {
        Log.d(LOG, "Autofilling existing FitnessProfile data");
        firstName.setText(fitnessProfile.getM_fName());
        lastName.setText(fitnessProfile.getM_lName());
        dob.setText(fitnessProfile.getM_dob());
        sex.setText(fitnessProfile.getM_sex());
        heightFeet.setText(String.valueOf(fitnessProfile.getM_heightFeet()));
        heightInches.setText(String.valueOf(fitnessProfile.getM_heightInches()));
        city.setText(fitnessProfile.getM_city());
        country.setText(fitnessProfile.getM_country());
        weight.setText(String.valueOf(fitnessProfile.getM_weightInPounds()));
        lbsPerWeek.setText(String.valueOf(fitnessProfile.getM_lbsPerWeek()));

        if (fitnessProfile.getM_lifestyleSelection().equalsIgnoreCase("ACTIVE")) {
            lifestyleSelector.check(R.id.btn_radio_active);
        } else {
            lifestyleSelector.check(R.id.btn_radio_sedentary);
        }

        if (fitnessProfile.getM_weightGoal().equalsIgnoreCase("GAIN")) {
            weightGoal.check(R.id.btn_radio_gain);
        }
        else if (fitnessProfile.getM_weightGoal().equalsIgnoreCase("MAINTAIN")) {
            weightGoal.check(R.id.btn_radio_maintain);
        } else {
            weightGoal.check(R.id.btn_radio_lose);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void userProfileSubmitButtonHandler() {
        Log.d(LOG, "User Profile Entry Done Button clicked");

        if (!isUserInputDataValid()) {
            Log.d(LOG, "invalid user data input");
            return;
        }

        m_dataListener.onProfileEntryDataEntered_DoneButtonOnClick(true);

        //Handle height measurements where inches field is left empty but feet is valid
//        String heightInchesValue =
//                isNotNullOrEmpty(heightInches.getText().toString()) ? heightInches.getText().toString() :
//                        String.valueOf(0);



//        userEnteredData.put("firstName", firstName.getText().toString());
//        userEnteredData.put("lastName", lastName.getText().toString());
//        userEnteredData.put("dob", dob.getText().toString());
//        userEnteredData.put("sex", sex.getText().toString());
//        userEnteredData.put("city", city.getText().toString());
//        userEnteredData.put("country", country.getText().toString());
//        userEnteredData.put("weight", weight.getText().toString());
//        userEnteredData.put("heightFeet", heightFeet.getText().toString());
//        userEnteredData.put("heightInches", heightInchesValue);
//        userEnteredData.put("lbsPerWeek", lbsPerWeek.getText().toString());
//        userEnteredData.put("lifestyle", lifestyleSelectorString);
//        userEnteredData.put("goal", weightGoalString);
//
//        userEnteredData.forEach((k,v)-> {
//            Log.d(LOG, "Key: '" + k + "' Value: '" + v + "'");
//        });

//        FitnessProfile m_fitnessProfile = new FitnessProfile();
//        m_fitnessProfile.setM_fName(userEnteredData.get("firstName"));
//        m_fitnessProfile.setM_lName(userEnteredData.get("lastName"));
//        m_fitnessProfile.setM_sex(userEnteredData.get("sex"));
//        m_fitnessProfile.setM_dob(userEnteredData.get("dob"));
//        m_fitnessProfile.setM_city(userEnteredData.get("city"));
//        m_fitnessProfile.setM_country(userEnteredData.get("country"));
//        m_fitnessProfile.setM_lbsPerWeek(Integer.parseInt(userEnteredData.get("llbsPerWeek")));
////        m_fitnessProfile.setM_lbsPerWeek(2);
//        m_fitnessProfile.setM_lifestyleSelection(userEnteredData.get("lifestyle"));
//        m_fitnessProfile.setM_weightGoal(userEnteredData.get("goal"));
//
//        String userSex = userEnteredData.get("sex");
//        int userAge = FitnessProfileUtils.calculateAge(userEnteredData.get("dob"));
//        double userWeight = Double.parseDouble(userEnteredData.get("weight"));
//        int heightFt = Integer.parseInt(userEnteredData.get("heightFeet"));
//        int heightIn = Integer.parseInt(userEnteredData.get("heightInches"));

//        FitnessProfile fitnessProfile = new FitnessProfile();
//        fitnessProfile.setM_fName(firstName.getText().toString());
//        fitnessProfile.setM_lName(lastName.getText().toString());
//        fitnessProfile.setM_sex(sex.getText().toString());
//        fitnessProfile.setM_dob(dob.getText().toString());
//        fitnessProfile.setM_city(city.getText().toString());
//        fitnessProfile.setM_country(country.getText().toString());
//        fitnessProfile.setM_lbsPerWeek(Integer.parseInt(lbsPerWeek.getText().toString()));
//        fitnessProfile.setM_lifestyleSelection(lifestyleSelectorString);
//        fitnessProfile.setM_weightGoal(weightGoalString);
//
//        int userWeight = Integer.parseInt(weight.getText().toString());
//        int heightFt = Integer.parseInt(heightFeet.getText().toString());
//        int heightIn = Integer.parseInt(heightInchesValue);
//        int totalHeightInches = calculateHeightInInches(heightFt, heightIn);
//        int userAge = calculateAge(fitnessProfile.getM_dob());
//
//        fitnessProfile.setM_weightInPounds(userWeight);
//        fitnessProfile.setM_heightFeet(heightFt);
//        fitnessProfile.setM_heightInches(heightIn);
//        fitnessProfile.setM_bmi(calculateBmi(totalHeightInches, userWeight));
//        fitnessProfile.setM_bmr(calculateBMR(heightFt, heightIn, fitnessProfile.getM_sex(), userWeight, userAge));

//        if (m_fitnessProfileViewModel.getFitnessProfile().getValue() != null){
//            Log.d(LOG, "submit button handler, view model has data");
//            FitnessProfile p = m_fitnessProfileViewModel.getFitnessProfile().getValue();
//            printUserProfileData(p);
//            m_dataListener.onProfileEntryDataEntered_DoneButtonOnClick(true);
//        } else {
//            Log.d(LOG, "view model null");
//            m_dataListener.onProfileEntryDataEntered_DoneButtonOnClick(true);
//        }
    }

    private boolean isUserInputDataValid() {
        if (!isValidAlphaChars(firstName.getText().toString())) {
            Toast.makeText(getContext(), "Invalid first name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isValidAlphaChars(lastName.getText().toString())) {
            Toast.makeText(getContext(), "Invalid last name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isValidDobFormat(dob.getText().toString())) {
            Toast.makeText(getContext(), "Invalid date of birth.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isValidSex(sex.getText().toString())) {
            Toast.makeText(getContext(), "Invalid sex.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isValidCity(city.getText().toString())) {
            Toast.makeText(getContext(), "Invalid city.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isValidCountryCode(country.getText().toString())) {
            Toast.makeText(getContext(), "Please enter a valid 2-character country code.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isValidWeight(weight.getText().toString())) {
            Toast.makeText(getContext(), "Invalid weight.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isValidHeight(heightFeet.getText().toString(), heightInches.getText().toString())) {
            Toast.makeText(getContext(), "Please enter a valid height in feet/inches.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isValidWeightPlan(lbsPerWeek.getText().toString())) {
            Toast.makeText(getContext(), "Please enter weekly weight goal", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            m_dataListener = (OnProfileEntryFragmentListener) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString() + " must implement OnProfileEntryFragmentListener");
        }
    }

    public interface OnProfileEntryFragmentListener {
        void onProfileEntryDataEntered_DoneButtonOnClick(boolean isClicked);
    }

    private void updateFitnessProfile(){
        m_fitnessProfileViewModel.updateFitnessProfile();
    }
}

package com.example.mcresswell.project01.fragments;


import android.annotation.TargetApi;
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

import com.example.mcresswell.project01.Activities.DashboardActivity;
import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.ViewModels.FitnessProfileViewModel;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.util.Constants;

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
    private EditText etxt_firstName, etxt_lastName, etxt_dob, etxt_sex, etxt_city, etxt_country,
            etxt_weight, etxt_heightFeet, etxt_heightInches, etxt_lbsPerWeek;
    private Button profileEntryButton;
    private ImageButton takeProfileImageButton;
    private Bitmap profileImage;
    private RadioGroup lifestyleSelector;
    private RadioButton activeLifestyle, sedentaryLifestyle;
    private RadioGroup weightGoal;
    private RadioButton gain, maintain, lose;

    //Data Elements
    private FitnessProfile m_fitnessProfile = new FitnessProfile();
    private String lifestyleSelectorString = "Active"; //Default lifestyle selector of 'Active' if no radio button selected
    private String weightGoalString = "Lose"; //Default etxt_weight goal of 'Lose' if no radio button is selected

//    private Map<String, String> userEnteredData = new HashMap<>();



    public ProfileEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);
        super.onCreate(savedInstanceState);

        initViewModel();

    }

    private void initViewModel() {
        final Observer<FitnessProfile> fitnessProfileObserver = fitnessProfile -> {
            m_fitnessProfile = fitnessProfile;
            if (m_fitnessProfile != null) {
                autofillExistingUserProfileData();
            }
        };

        m_fitnessProfileViewModel = ViewModelProviders.of(getActivity())
                .get(FitnessProfileViewModel.class);
        m_fitnessProfile = m_fitnessProfileViewModel.getFitnessProfile().getValue();
        m_fitnessProfileViewModel.getFitnessProfile().observe(getActivity(), fitnessProfileObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE_VIEW);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_entry, container, false);

        initializeProfileEntryUIElements(view);
        setButtonListeners();

        if (m_fitnessProfile != null) { //If FitnessProfile has existing data, autopopulate fields
            autofillExistingUserProfileData();
        }

        return view;
    }

    private void setButtonListeners() {
        //set buttons to listen to this class
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
        etxt_firstName = (EditText) view.findViewById(R.id.txtv_fname);
        etxt_lastName = (EditText) view.findViewById(R.id.txtv_lname);
        etxt_dob = (EditText) view.findViewById(R.id.txtv_dob);
        etxt_sex = (EditText) view.findViewById(R.id.txtv_sex);
        etxt_city = (EditText) view.findViewById(R.id.txtv_city);
        etxt_country = (EditText) view.findViewById(R.id.txtv_country);
        etxt_weight = (EditText) view.findViewById(R.id.txtv_weight);
        etxt_heightFeet = (EditText) view.findViewById(R.id.txtv_feet);
        etxt_heightInches = (EditText) view.findViewById(R.id.txtv_inches);
        etxt_lbsPerWeek = (EditText) view.findViewById(R.id.txtv_weight2);

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

    private void autofillExistingUserProfileData() {
        Log.d(LOG, "Autofilling existing FitnessProfile data");
        etxt_firstName.setText(m_fitnessProfile.getM_fName());
        etxt_lastName.setText(m_fitnessProfile.getM_lName());
        etxt_dob.setText(m_fitnessProfile.getM_dob());
        etxt_sex.setText(m_fitnessProfile.getM_sex());
        etxt_heightFeet.setText(String.valueOf(m_fitnessProfile.getM_heightFeet()));
        etxt_heightInches.setText(String.valueOf(m_fitnessProfile.getM_heightInches()));
        etxt_city.setText(m_fitnessProfile.getM_city());
        etxt_country.setText(m_fitnessProfile.getM_country());
        etxt_weight.setText(String.valueOf(m_fitnessProfile.getM_weightInPounds()));
        etxt_lbsPerWeek.setText(String.valueOf(m_fitnessProfile.getM_lbsPerWeek()));

        if (m_fitnessProfile.getM_lifestyleSelection().equalsIgnoreCase("ACTIVE")) {
            lifestyleSelector.check(R.id.btn_radio_active);
        } else {
            lifestyleSelector.check(R.id.btn_radio_sedentary);
        }

        if (m_fitnessProfile.getM_weightGoal().equalsIgnoreCase("GAIN")) {
            weightGoal.check(R.id.btn_radio_gain);
        }
        else if (m_fitnessProfile.getM_weightGoal().equalsIgnoreCase("MAINTAIN")) {
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

        //Handle height measurements where inches field is left empty but feet is valid
        String heightInchesValue =
                isNotNullOrEmpty(etxt_heightInches.getText().toString()) ? etxt_heightInches.getText().toString() :
                        String.valueOf(0);

        m_fitnessProfile.setM_fName(etxt_firstName.getText().toString());
        m_fitnessProfile.setM_lName(etxt_lastName.getText().toString());
        m_fitnessProfile.setM_dob(etxt_dob.getText().toString());
        m_fitnessProfile.setM_sex(etxt_sex.getText().toString());
        m_fitnessProfile.setM_city(etxt_city.getText().toString());
        m_fitnessProfile.setM_country(etxt_country.getText().toString());
        m_fitnessProfile.setM_lifestyleSelection(lifestyleSelectorString);
        m_fitnessProfile.setM_weightGoal(weightGoalString);
        m_fitnessProfile.setM_lbsPerWeek(Integer.parseInt(etxt_lbsPerWeek.getText().toString()));
        m_fitnessProfile.setM_weightInPounds(Integer.parseInt(etxt_weight.getText().toString()));
        m_fitnessProfile.setM_heightFeet(Integer.parseInt(etxt_heightFeet.getText().toString()));
        m_fitnessProfile.setM_heightInches(Integer.parseInt(heightInchesValue));

        //TODO: Is this method needed?
        updateFitnessProfile();

        //send the signal to the ProfileEntryFragment that the button was clicked
        m_dataListener.onProfileEntryDataEntered_DoneButtonOnClick(true);


//        userEnteredData.put("etxt_firstName", etxt_firstName.getText().toString());
//        userEnteredData.put("etxt_lastName", etxt_lastName.getText().toString());
//        userEnteredData.put("etxt_dob", etxt_dob.getText().toString());
//        userEnteredData.put("etxt_sex", etxt_sex.getText().toString());
//        userEnteredData.put("etxt_city", etxt_city.getText().toString());
//        userEnteredData.put("etxt_country", etxt_country.getText().toString());
//        userEnteredData.put("etxt_weight", etxt_weight.getText().toString());
//        userEnteredData.put("etxt_heightFeet", etxt_heightFeet.getText().toString());
//        userEnteredData.put("etxt_heightInches", heightInchesValue);
//        userEnteredData.put("etxt_lbsPerWeek", etxt_lbsPerWeek.getText().toString());
//        userEnteredData.put("lifestyle", lifestyleSelectorString);
//        userEnteredData.put("goal", weightGoalString);
//
//        userEnteredData.forEach((k,v)-> {
//            Log.d(LOG, "Key: '" + k + "' Value: '" + v + "'");
//        });

//        FitnessProfile m_fitnessProfile = new FitnessProfile();
//        m_fitnessProfile.setM_fName(userEnteredData.get("etxt_firstName"));
//        m_fitnessProfile.setM_lName(userEnteredData.get("etxt_lastName"));
//        m_fitnessProfile.setM_sex(userEnteredData.get("etxt_sex"));
//        m_fitnessProfile.setM_dob(userEnteredData.get("etxt_dob"));
//        m_fitnessProfile.setM_city(userEnteredData.get("etxt_city"));
//        m_fitnessProfile.setM_country(userEnteredData.get("etxt_country"));
//        m_fitnessProfile.setM_lbsPerWeek(Integer.parseInt(userEnteredData.get("llbsPerWeek")));
////        m_fitnessProfile.setM_lbsPerWeek(2);
//        m_fitnessProfile.setM_lifestyleSelection(userEnteredData.get("lifestyle"));
//        m_fitnessProfile.setM_weightGoal(userEnteredData.get("goal"));
//
//        String userSex = userEnteredData.get("etxt_sex");
//        int userAge = FitnessProfileUtils.calculateAge(userEnteredData.get("etxt_dob"));
//        double userWeight = Double.parseDouble(userEnteredData.get("etxt_weight"));
//        int heightFt = Integer.parseInt(userEnteredData.get("etxt_heightFeet"));
//        int heightIn = Integer.parseInt(userEnteredData.get("etxt_heightInches"));

//        FitnessProfile fitnessProfile = new FitnessProfile();
//        fitnessProfile.setM_fName(etxt_firstName.getText().toString());
//        fitnessProfile.setM_lName(etxt_lastName.getText().toString());
//        fitnessProfile.setM_sex(etxt_sex.getText().toString());
//        fitnessProfile.setM_dob(etxt_dob.getText().toString());
//        fitnessProfile.setM_city(etxt_city.getText().toString());
//        fitnessProfile.setM_country(etxt_country.getText().toString());
//        fitnessProfile.setM_lbsPerWeek(Integer.parseInt(etxt_lbsPerWeek.getText().toString()));
//        fitnessProfile.setM_lifestyleSelection(lifestyleSelectorString);
//        fitnessProfile.setM_weightGoal(weightGoalString);
//
//        int userWeight = Integer.parseInt(etxt_weight.getText().toString());
//        int heightFt = Integer.parseInt(etxt_heightFeet.getText().toString());
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
        if (!isValidAlphaChars(etxt_firstName.getText().toString())) {
            Toast.makeText(getContext(), "Invalid first name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isValidAlphaChars(etxt_lastName.getText().toString())) {
            Toast.makeText(getContext(), "Invalid last name.", Toast.LENGTH_SHORT).show();
            return false;
        }
//        else if (!isValidDobFormat(etxt_dob.getText().toString())) {
//            Toast.makeText(getContext(), "Invalid date of birth.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        else if (!isValidSex(etxt_sex.getText().toString())) {
//            Toast.makeText(getContext(), "Invalid sex.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        else if (!isValidCity(etxt_city.getText().toString())) {
//            Toast.makeText(getContext(), "Invalid city.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        else if (!isValidCountryCode(etxt_country.getText().toString())) {
//            Toast.makeText(getContext(), "Please enter a valid 2-character country code.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        else if (!isValidWeight(etxt_weight.getText().toString())) {
//            Toast.makeText(getContext(), "Invalid weight.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        else if (!isValidHeight(etxt_heightFeet.getText().toString(), etxt_heightInches.getText().toString())) {
//            Toast.makeText(getContext(), "Please enter a valid height in feet/inches.", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        else if (!isValidWeightPlan(etxt_lbsPerWeek.getText().toString())) {
//            Toast.makeText(getContext(), "Please enter a valid weekly weight goal (i.e., a maximum weight management change of 5lbs/week)", Toast.LENGTH_SHORT).show();
//            return false;
//        }
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
        //TODO: This method needs work to update the fitnessProfile data in the database.
        m_fitnessProfileViewModel.updateFitnessProfile(m_fitnessProfile);
    }
}

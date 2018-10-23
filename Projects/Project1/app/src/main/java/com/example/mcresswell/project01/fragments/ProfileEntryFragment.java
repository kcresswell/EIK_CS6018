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

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.viewmodel.FitnessProfileViewModel;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.util.Constants;
import com.example.mcresswell.project01.viewmodel.UserViewModel;

import static com.example.mcresswell.project01.util.ValidationUtils.isNotNullOrEmpty;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidCity;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidCountryName;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidDobFormat;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidHeight;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidName;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidSex;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidWeight;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidWeightPlan;
/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEntryFragment extends Fragment implements View.OnClickListener {

    private static final String LOG_TAG = ProfileEntryFragment.class.getSimpleName();

    //request code for camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private OnProfileEntryFragmentListener m_dataListener;
    private FitnessProfileViewModel m_fitnessProfileViewModel;
    private UserViewModel userViewModel;

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
    private FitnessProfile m_fitnessProfile;
    private User m_user;
    private String lifestyleSelectorString = "Active"; //Default lifestyle selector of 'Active' if no radio button selected
    private String weightGoalString = "Lose"; //Default etxt_weight goal of 'Lose' if no radio button is selected

    private int test_user_num = 1;
//    private Map<String, String> userEnteredData = new HashMap<>();



    public ProfileEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, Constants.CREATE);

        super.onCreate(savedInstanceState);

        initUserViewModel();
        initFitnessProfileViewModel();

    }

    private void initUserViewModel() {
        final Observer<User> userObserver = user -> m_user = user;
        userViewModel = ViewModelProviders.of(this)
                .get(UserViewModel.class);
        m_user = userViewModel.getUser().getValue();
        userViewModel.getUser().observe(this, userObserver);
    }

    private void initFitnessProfileViewModel() {
        final Observer<FitnessProfile> fitnessProfileObserver = fitnessProfile -> {
            m_fitnessProfile = fitnessProfile;
            if (m_fitnessProfile != null) {
                autofillExistingUserProfileData();
            }
        };

        m_fitnessProfileViewModel = ViewModelProviders.of(getActivity())
                .get(FitnessProfileViewModel.class);
        if (m_user != null ) {
            m_fitnessProfile = m_fitnessProfileViewModel.getFitnessProfile(m_user.getId()).getValue();
            m_fitnessProfileViewModel.getFitnessProfile(m_user.getId()).observe(getActivity(), fitnessProfileObserver);
        } else {
            m_fitnessProfile = m_fitnessProfileViewModel.getFitnessProfile(test_user_num).getValue();
            m_fitnessProfileViewModel.getFitnessProfile(test_user_num).observe(getActivity(), fitnessProfileObserver);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, Constants.CREATE_VIEW);

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
        Log.d(LOG_TAG, "onViewStateRestored");
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
        Log.d(LOG_TAG, "Autofilling existing FitnessProfile data");
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
        Log.d(LOG_TAG, "User Profile Entry Done Button clicked");

        if (!isUserInputDataValid()) {
            Log.d(LOG_TAG, "invalid user data input");
            return;
        }

        //Handle height measurements where inches field is left empty but feet is valid
        String heightInchesValue =
                isNotNullOrEmpty(etxt_heightInches.getText().toString()) ? etxt_heightInches.getText().toString() :
                        String.valueOf(0);

        FitnessProfile tmp_fitnessProfile = new FitnessProfile();

        tmp_fitnessProfile.setM_fName(etxt_firstName.getText().toString());
        tmp_fitnessProfile.setM_lName(etxt_lastName.getText().toString());
        tmp_fitnessProfile.setM_dob(etxt_dob.getText().toString());
        tmp_fitnessProfile.setM_sex(etxt_sex.getText().toString());
        tmp_fitnessProfile.setM_city(etxt_city.getText().toString());
        tmp_fitnessProfile.setM_country(etxt_country.getText().toString());
        tmp_fitnessProfile.setM_lifestyleSelection(lifestyleSelectorString);
        tmp_fitnessProfile.setM_weightGoal(weightGoalString);
        tmp_fitnessProfile.setM_lbsPerWeek(Integer.parseInt(etxt_lbsPerWeek.getText().toString()));
        tmp_fitnessProfile.setM_weightInPounds(Integer.parseInt(etxt_weight.getText().toString()));
        tmp_fitnessProfile.setM_heightFeet(Integer.parseInt(etxt_heightFeet.getText().toString()));
        tmp_fitnessProfile.setM_heightInches(Integer.parseInt(heightInchesValue));

        m_fitnessProfile = tmp_fitnessProfile;
        //add to database
        insertNewFitnessProfile();

        //send the signal to the ProfileEntryFragment that the button was clicked
        m_dataListener.onProfileEntryDataEntered_DoneButtonOnClick(true);


        //TODO: Uncomment the code below once the fitness profile view model is working
        userViewModel.getUser().observe(this, user -> {
            if (user != null) {
                //Retrieve the userID from UserViewModel for entering in profile_id field of new fitness profile record
                tmp_fitnessProfile.setM_userID(m_user.getId());
                m_fitnessProfileViewModel.insertNewFitnessProfile(tmp_fitnessProfile);
            }
        });
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
        else if (!isValidCountryName(etxt_country.getText().toString())) {
            Toast.makeText(getContext(), "Please enter a valid 2-letter country code.", Toast.LENGTH_SHORT).show();
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
        try {
            m_dataListener = (OnProfileEntryFragmentListener) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString() + " must implement OnProfileEntryFragmentListener");
        }
    }

    public interface OnProfileEntryFragmentListener {
        void onProfileEntryDataEntered_DoneButtonOnClick(boolean isClicked);
    }

    private void insertNewFitnessProfile(){
        //TODO: This method needs work to update the fitnessProfile data in the database.
        m_fitnessProfileViewModel.insertNewFitnessProfile(m_fitnessProfile);
    }
}

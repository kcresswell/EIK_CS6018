package com.example.mcresswell.project01;


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
import android.widget.Toast;

import com.example.mcresswell.project01.userProfile.UserProfile;
import com.example.mcresswell.project01.userProfile.UserProfileViewModel;
import com.example.mcresswell.project01.util.Constants;
import com.example.mcresswell.project01.util.UserProfileUtils;
import com.example.mcresswell.project01.util.ValidationUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEntryFragment extends Fragment implements View.OnClickListener {

    private static final String LOG = ProfileEntryFragment.class.getSimpleName();

    //request code for camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private OnProfileEntryFragmentListener m_dataListener;
    private UserProfileViewModel viewModel;

    //UI Elements
    private EditText firstName, lastName, dob, sex, city, country,
            weight, heightFeet, heightInches, lbsPerWeek;
    private Button profileEntryButton;
    private ImageButton takeProfileImageButton;
    private Bitmap profileImage;
//
//    private EditText lifestyleSelectorText;
//    private EditText weightGoal;

    //Data fields

    private Map<String, String> userEnteredData = new HashMap<>();
    //    private String m_fname, m_lname, m_dob, m_sex, m_city, m_country, str_lifestyle_selection, str_weight_goal_selection;
    private int age, weightLbs, feet, inches, lbsPerWeekGoal;

    private Bundle m_userProfileBundle = new Bundle();

    public ProfileEntryFragment() {
        // Required empty public constructor
    }

    public static ProfileEntryFragment newInstance(UserProfile profile){
        ProfileEntryFragment fragment = new ProfileEntryFragment();

        Bundle args = new Bundle();
        if (profile != null) {
            Log.d(LOG, "newInstance being initialized with previously entered user profile data");
            args.putParcelable("profile", profile);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);
        super.onCreate(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);

        final Observer<UserProfile> nameObserver = new Observer<UserProfile>() {
            @Override
            public void onChanged(@Nullable final UserProfile userProfile) {
                // Update the UI, in this case, a TextView.
                firstName.setText(userProfile.getM_fName());
                lastName.setText(userProfile.getM_lName());
            }
        };

        viewModel.getUserProfile().observe(this, nameObserver);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE_VIEW);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_entry, container, false);

        initializeProfileEntryUIElements(view);

        //set buttons to listen to this class
        profileEntryButton.setOnClickListener(this);
        takeProfileImageButton.setOnClickListener(this);

        if (savedInstanceState != null) {
            UserProfile userProfile = getArguments().getParcelable("profile");
            //TODO: RESTORE PREVIOUSLY ENTERED USER DATA FIELDS IF A USER WITH AN EXISTING PROFILE CLICKS THE EDIT BUTTON
            //TODO: IN THIS CASE, THE PROFILE ENTRY FRAGMENT SHOULD AUTOPOPULATE WITH THE EXISTING USER DATA
        }

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        Log.d(LOG, "onClick");
        switch (view.getId()){
            case R.id.btn_img_takeImage: {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(cameraIntent.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            }
            case R.id.btn_submit: {
                userProfileSubmitButtonHandler();
            }
            break;
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
            //place data in fields
//            firstName.setText("" + savedInstanceState.getString("M_FN_DATA"));
//            lastName.setText("" + savedInstanceState.getString("M_LN_DATA"));
//            dob.setText("" + savedInstanceState.getString("M_DOB_DATA"));
            profileImage = savedInstanceState.getParcelable("M_IMG_DATA");
            takeProfileImageButton.setImageBitmap(profileImage);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        Log.d(LOG, Constants.SAVE_INSTANCE_STATE);
        super.onSaveInstanceState(bundle);
        //Save user data strings in bundle
        userEnteredData.forEach(bundle::putString);
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

        //TODO: Need to add back lifestyle handler and weight loss goal fields back later
//
//        //--get submit and image buttons--//
        profileEntryButton = (Button) view.findViewById(R.id.btn_submit);
        takeProfileImageButton = (ImageButton) view.findViewById(R.id.btn_img_takeImage);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void userProfileSubmitButtonHandler() {
        //Validate input fields before adding to hashmap
        if (!isUserInputDataValid()) {
            Toast.makeText(getContext(), "Invalid input was entered", Toast.LENGTH_SHORT);
            return;
        }
        //
        userEnteredData.put("firstName", firstName.getText().toString());
        userEnteredData.put("lastName", lastName.getText().toString());
        userEnteredData.put("dob", dob.getText().toString());
        userEnteredData.put("sex", sex.getText().toString());
        userEnteredData.put("city", city.getText().toString());
        userEnteredData.put("country", country.getText().toString());
        userEnteredData.put("weight", weight.getText().toString());
        userEnteredData.put("heightFeet", heightFeet.getText().toString());
        userEnteredData.put("heightInches", heightInches.getText().toString());
        userEnteredData.put("lbsPerWeek", lbsPerWeek.getText().toString());

        userEnteredData.forEach((k,v)-> {
            Log.d(LOG, "Key: '" + k + "' Value: '" + v + "'");
        });

        //TODO: Need to add back lifestyle handler and weight loss goal fields back later

    }
    private boolean isUserInputDataValid() {
        return ValidationUtils.isValidAlphaChars(firstName.getText().toString()) &&
                ValidationUtils.isValidAlphaChars(lastName.getText().toString()) &&
                ValidationUtils.isValidDobFormat(dob.getText().toString()) &&
                ValidationUtils.isValidSex(sex.getText().toString()) &&
                ValidationUtils.isValidCity(city.getText().toString()) &&
                ValidationUtils.isValidCountryCode(country.getText().toString()) &&
                ValidationUtils.isValidWeight(weight.getText().toString()) &&
                ValidationUtils.isValidHeight(
                        heightFeet.getText().toString(), heightInches.getText().toString()) &&
                UserProfileUtils.isWeightChangeWithinAcceptableRange(lbsPerWeek.getText().toString());
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

    public void loadUserProfileData(UserProfile profile) {
        Log.d(LOG, "loadUserProfileData");

       viewModel.setUserProfile(profile);
    }


public interface OnProfileEntryFragmentListener {
    void onProfileEntryDataEntered(UserProfile profile);
}
}

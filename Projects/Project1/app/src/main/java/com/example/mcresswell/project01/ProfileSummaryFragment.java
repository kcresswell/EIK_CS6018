package com.example.mcresswell.project01;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mcresswell.project01.userProfile.UserProfile;
import com.example.mcresswell.project01.userProfile.UserProfileViewModel;
import com.example.mcresswell.project01.util.Constants;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileSummaryFragment extends Fragment
        implements View.OnClickListener {

    private static final String LOG = ProfileSummaryFragment.class.getSimpleName();

    private Button editButton;
    private OnProfileSummaryInteractionListener listener;
    private UserProfileViewModel userProfileViewModel;
    private UserProfile userProfile;

    //UI Elements
    private TextView firstName, lastName, sex, dob, heightFeet, heightInches, lbsperweek, city, country, weight, activity, weightGoal;

    public ProfileSummaryFragment() {
        // Required empty public constructor
    }

    public static ProfileSummaryFragment newInstance(UserProfile profile) {
        Log.d(LOG, Constants.NEW);
        ProfileSummaryFragment fragment = new ProfileSummaryFragment();
        Bundle args = new Bundle();
        args.putParcelable("profile", profile);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            weatherForecast = getArguments().getParcelable("data");
//            Log.d(LOG, "City: " + getArguments().getString("city"));
//            Log.d(LOG, "Country: " + getArguments().getString("country"));

        }
        userProfileViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        userProfileViewModel.getUserProfile().observe(this, nameObserver);
        userProfile = getActivity().getIntent().getParcelableExtra("profile");
        if (userProfile != null) {
            loadUserData(userProfile);
        } else {
            Log.d(LOG, "no data in bundle, loadUserData using test profile");
//            loadUserData(UserProfile.newTestUserProfileInstance());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE_VIEW);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_summary, container, false);

        firstName = v.findViewById(R.id.txtv_fname);
        lastName = v.findViewById(R.id.txtv_lname);
        sex = v.findViewById(R.id.txtv_sex);
        dob = v.findViewById(R.id.txtv_dob);
        heightFeet = v.findViewById(R.id.txtv_feet);
        heightInches = v.findViewById(R.id.txtv_inches);
        editButton = v.findViewById(R.id.btn_edit);
        weight = v.findViewById(R.id.txtv_weight);

//        weightGoal = v.findViewById(R.id);

        editButton.setOnClickListener(this);

        if (userProfile != null) {
            firstName.setText(userProfile.getM_fName());
            lastName.setText(userProfile.getM_lName());
            sex.setText(userProfile.getM_sex());
            dob.setText(userProfile.getM_dob());
//            heightFeet.setText(userProfile.getBodyData().getHeightFeet());
//            heightInches.setText(userProfile.getBodyData().getHeightInches());
//            weight.setText((int) userProfile.getBodyData().getWeightInPounds());
//            city.setText(userProfile.getM_city());
//            country.setText(userProfile.getM_country());
//            weight.setText((int) userProfile.getBodyData().getWeightInPounds());
//            weightGoal.setText(userProfile.getM_weightGoal());
//            lbsperweek.setText(us);

        }

        return v;
    }

    /*
    Create an observer that listens for changes to this data
     */
    final Observer<UserProfile> nameObserver  = new Observer<UserProfile>() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onChanged(@Nullable final UserProfile userProfile) {
            if (userProfile != null) { //UserProfile data has finished being retrieved
                userProfile.printUserProfileData();
                Log.d(LOG, "onChanged, updating data fields");
                //Data now exists for user, update ui fields with data
//                firstName.setText(userProfile.getM_fName());
//                lastName.setText(userProfile.getM_lName());
//                sex.setText(userProfile.getM_sex());
//                dob.setText(userProfile.getM_dob());

            }


        }
    };

    //edit button on click fragment replace with edit fragment
    //id = btn_edit
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_edit) {
            Log.d(LOG, "Edit button onClick");
            passExistingDataOnEditButtonClick();
        }
    }

    @Override
    public void onAttach(Context context) {
        Log.d(LOG, Constants.ATTACH);

        super.onAttach(context);
        if (context instanceof OnProfileSummaryInteractionListener) {
            listener = (OnProfileSummaryInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProfileSummaryInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(LOG, Constants.DETACH);
        super.onDetach();
        listener = null;
    }

    private void loadUserData(UserProfile profile) {
        Log.d(LOG, "loadWeatherData");
        userProfileViewModel.setUserProfile(profile);
    }

    public interface OnProfileSummaryInteractionListener {
        void onProfileSummaryEditButton(UserProfile profile);
    }

    private void passExistingDataOnEditButtonClick() {
        if (userProfileViewModel != null) {
            Log.d(LOG, "userProfileViewModel NOT NULL!");
            listener.onProfileSummaryEditButton(userProfileViewModel.getUserProfile().getValue());
        } else {
            Log.d(LOG, "userProfileViewModel is still null, passing data manually");
            UserProfile userProfile = new UserProfile();
            userProfile.setM_fName(firstName.getText().toString());
            userProfile.setM_lName(lastName.getText().toString());
            userProfile.setM_sex(sex.getText().toString());
            userProfile.setM_dob(dob.getText().toString());

            //TODO: FINISH THIS LATER
            listener.onProfileSummaryEditButton(userProfile);
        }
    }

}

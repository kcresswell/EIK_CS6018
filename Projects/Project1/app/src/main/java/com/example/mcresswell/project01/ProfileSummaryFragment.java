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

import static com.example.mcresswell.project01.util.UserProfileUtils.calculateAge;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileSummaryFragment extends Fragment
        implements View.OnClickListener {

    private static final String LOG = ProfileSummaryFragment.class.getSimpleName();

    private Button m_editButton;
    private OnProfileSummaryInteractionListener m_listener;
    private UserProfileViewModel m_userProfileViewModel;
    private UserProfile m_userProfile;

    //UI Elements
    private TextView m_firstName, m_lastName, m_sex, m_age, m_heightFeet, m_heightInches, m_city, m_country, m_weight, m_activity, m_weightGoal;

    public ProfileSummaryFragment() {
        // Required empty public constructor
    }

    public static ProfileSummaryFragment newInstance(UserProfile profile) {
        Log.d(LOG, Constants.NEW);
        ProfileSummaryFragment fragment = new ProfileSummaryFragment();
        Bundle args = new Bundle();
        if (profile != null) {
            args.putParcelable("profile", profile);
        }
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
//            Log.d(LOG, "City: " + getArguments().getString("m_city"));
//            Log.d(LOG, "Country: " + getArguments().getString("m_country"));
        }
        m_userProfileViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        m_userProfileViewModel.getUserProfile().observe(this, nameObserver);
        m_userProfile = getActivity().getIntent().getParcelableExtra("profile");
        if (m_userProfile != null) {
            loadUserData(m_userProfile);
        }
// if (m_userProfile != null) {
//            loadUserData(m_userProfile);
//        } else {
//            Log.d(LOG, "no data in bundle, loadUserData using test profile");
////            loadUserData(UserProfile.newTestUserProfileInstance());
//        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE_VIEW);

        View v = inflater.inflate(R.layout.fragment_profile_summary, container, false);

        m_firstName = v.findViewById(R.id.txtv_fname);
        m_lastName = v.findViewById(R.id.txtv_lname);
        m_sex = v.findViewById(R.id.txtv_sex);
        m_age = v.findViewById(R.id.txtv_dob);
        m_heightFeet = v.findViewById(R.id.txtv_feet);
        m_heightInches = v.findViewById(R.id.txtv_inches);
        m_weight = v.findViewById(R.id.txtv_weight);
        m_city = v.findViewById(R.id.txtv_city);
        m_country = v.findViewById(R.id.txtv_country);
        m_activity = v.findViewById(R.id.radiogp_lifestyle);
        m_weightGoal = v.findViewById(R.id.radiogp_weightGoal);
        m_editButton = v.findViewById(R.id.btn_edit);

        m_editButton.setOnClickListener(this);

        if (m_userProfile != null) {
            m_firstName.setText(m_userProfile.getM_fName());
            m_lastName.setText(m_userProfile.getM_lName());
            m_sex.setText(m_userProfile.getM_sex());
            m_age.setText(calculateAge(m_userProfile.getM_dob())+ "y");
            m_heightFeet.setText(String.valueOf(m_userProfile.getM_heightFeet()));
            m_heightInches.setText(String.valueOf(m_userProfile.getM_heightInches()));
            m_weight.setText(String.valueOf(m_userProfile.getM_weightInPounds()));
            m_city.setText(m_userProfile.getM_city());
            m_country.setText(m_userProfile.getM_country());
            m_activity.setText(m_userProfile.getM_lifestyleSelection());
            m_weightGoal.setText(String.valueOf(m_userProfile.getM_weightGoal() + " " + m_userProfile.getM_lbsPerWeek() + " lbs/week"));
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
//                m_firstName.setText(m_userProfile.getM_fName());
//                m_lastName.setText(m_userProfile.getM_lName());
//                m_sex.setText(m_userProfile.getM_sex());
//                m_dob.setText(m_userProfile.getM_dob());
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
            m_listener = (OnProfileSummaryInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProfileSummaryInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(LOG, Constants.DETACH);
        super.onDetach();
        m_listener = null;
    }

    private void loadUserData(UserProfile profile) {
        Log.d(LOG, "loadUserData");
        m_userProfileViewModel.setUserProfile(profile);
    }

    public interface OnProfileSummaryInteractionListener {
        void onProfileSummaryEditButton(UserProfile profile);
    }

    private void passExistingDataOnEditButtonClick() {
        if (m_userProfileViewModel != null) {
            Log.d(LOG, "m_userProfileViewModel NOT NULL!");
            m_listener.onProfileSummaryEditButton(m_userProfileViewModel.getUserProfile().getValue());
        } else {
            Log.d(LOG, "m_userProfileViewModel is still null, passing data manually");
            UserProfile userProfile = new UserProfile();
            userProfile.setM_fName(m_firstName.getText().toString());
            userProfile.setM_lName(m_lastName.getText().toString());
            userProfile.setM_sex(m_sex.getText().toString());
//            userProfile.setM_dob(m_d.getText().toString());

            //TODO: FINISH THIS LATER
            m_listener.onProfileSummaryEditButton(userProfile);
        }
    }

}

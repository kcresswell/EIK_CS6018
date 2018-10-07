package com.example.mcresswell.project01.fragments;


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

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.ViewModels.UserProfileViewModel;
import com.example.mcresswell.project01.util.Constants;

import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateAge;
import static com.example.mcresswell.project01.util.FitnessProfileUtils.printUserProfileData;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileSummaryFragment extends Fragment
        implements View.OnClickListener {

    private static final String LOG = ProfileSummaryFragment.class.getSimpleName();

    private Button m_editButton;
    private OnProfileSummaryInteractionListener m_listener;
    private UserProfileViewModel m_userProfileViewModel;
    private FitnessProfile m_fitnessProfile;
//    private Bitmap m_photo;
//    private ImageButton m_profilePhoto;

    //UI Elements
    private TextView m_firstName, m_lastName, m_sex, m_age, m_heightFeet, m_heightInches, m_city, m_country, m_weight, m_activity, m_weightGoal;

    public ProfileSummaryFragment() {
        // Required empty public constructor
    }

    public static ProfileSummaryFragment newInstance(FitnessProfile profile) {
        Log.d(LOG, Constants.NEW);
        ProfileSummaryFragment fragment = new ProfileSummaryFragment();
        Bundle args = new Bundle();
        if (profile != null) {
            Log.d(LOG, "newInstance created with existing FitnessProfile data passed");
//            args.putParcelable("profile", profile);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);
        super.onCreate(savedInstanceState);

        m_userProfileViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        m_userProfileViewModel.getUserProfile().observe(this, nameObserver);

        m_fitnessProfile = getActivity().getIntent().getParcelableExtra("profile");
//        m_photo = getActivity().getIntent().getParcelableExtra("M_IMG_DATA");
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
//        m_profilePhoto = v.findViewById(R.id.btn_img_takeImage);


        m_editButton.setOnClickListener(this);

        if (m_fitnessProfile != null) {
            m_firstName.setText(m_fitnessProfile.getM_fName());
            m_lastName.setText(m_fitnessProfile.getM_lName());
            m_sex.setText(m_fitnessProfile.getM_sex().toUpperCase());
            m_age.setText(calculateAge(m_fitnessProfile.getM_dob())+ "y");
            m_heightFeet.setText(String.valueOf(m_fitnessProfile.getM_heightFeet()));
            m_heightInches.setText(String.valueOf(m_fitnessProfile.getM_heightInches()));
            m_weight.setText(String.valueOf(m_fitnessProfile.getM_weightInPounds()));
            m_city.setText(m_fitnessProfile.getM_city());
            m_country.setText(m_fitnessProfile.getM_country());
            m_activity.setText(m_fitnessProfile.getM_lifestyleSelection());
            m_weightGoal.setText(String.valueOf(m_fitnessProfile.getM_weightGoal() + " " + m_fitnessProfile.getM_lbsPerWeek() + " lbs/week"));
        }

//        if (m_photo != null) {
//            m_profilePhoto.setImageBitmap(m_photo);
//        }
        return v;
    }

    /*
    Create an observer that listens for changes to this data
     */
    final Observer<FitnessProfile> nameObserver  = new Observer<FitnessProfile>() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onChanged(@Nullable final FitnessProfile fitnessProfile) {
            if (fitnessProfile != null) { //FitnessProfile data has finished being retrieved
                printUserProfileData(fitnessProfile);
                Log.d(LOG, "onChanged, updating data fields");
//                m_fitnessProfile = fitnessProfile;
                loadUserData(fitnessProfile);
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

    private void loadUserData(FitnessProfile profile) {
        Log.d(LOG, "loadUserData");
        m_userProfileViewModel.setUserProfile(profile);
    }

    public interface OnProfileSummaryInteractionListener {
        void onProfileSummaryEditButton(FitnessProfile profile);
    }

    private void passExistingDataOnEditButtonClick() {
        if (m_userProfileViewModel.getUserProfile().getValue() != null) {
            Log.d(LOG, "m_userProfileViewModel NOT NULL");
            m_listener.onProfileSummaryEditButton(m_userProfileViewModel.getUserProfile().getValue());
        } else {
            m_listener.onProfileSummaryEditButton(m_fitnessProfile);
        }
    }

}

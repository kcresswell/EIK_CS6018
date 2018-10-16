package com.example.mcresswell.project01.fragments;


import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.viewmodel.FitnessProfileViewModel;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.util.Constants;

import static com.example.mcresswell.project01.util.FitnessProfileUtils.calculateAge;

public class ProfileSummaryFragment extends Fragment
        implements View.OnClickListener {

    private static final String LOG_TAG = ProfileSummaryFragment.class.getSimpleName();
    private Button m_editButton;
    private OnProfileSummaryInteractionListener m_listener;
    private FitnessProfileViewModel m_fitnessProfileViewModel;
    private MutableLiveData<FitnessProfile> m_fitnessProfile;
//    private Bitmap m_photo;
//    private ImageButton m_profilePhoto;

    //UI Elements
    private TextView m_firstName,
            m_lastName,
            m_sex,
            m_age,
            m_heightFeet,
            m_heightInches,
            m_city,
            m_country,
            m_weight,
            m_activity,
            m_weightGoal;

    public ProfileSummaryFragment() { }

//    public static ProfileSummaryFragment newInstance(FitnessProfile profile) {
//        Log.d(LOG_TAG, Constants.NEW);
//        ProfileSummaryFragment fragment = new ProfileSummaryFragment();
//        Bundle args = new Bundle();
//        if (profile != null) {
//            Log.d(LOG_TAG, "newInstance created with existing FitnessProfile data passed");
////            args.putParcelable("profile", profile);
//        }
//        fragment.setArguments(args);
//        return fragment;
//    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, Constants.CREATE);
        super.onCreate(savedInstanceState);

        initViewModel();
        m_fitnessProfile = m_fitnessProfileViewModel.getFitnessProfile();
//        m_photo = getActivity().getIntent().getParcelableExtra("M_IMG_DATA");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, Constants.CREATE_VIEW);

        View v = inflater.inflate(R.layout.fragment_profile_summary, container, false);
        initViewElements(v);
        m_editButton.setOnClickListener(this);
        setDataToViewElements();

        return v;
    }

    private void initViewModel() {
//        final Observer<FitnessProfile> fitnessProfileObserver = fitnessProfile -> m_fitnessProfile.setValue(fitnessProfile);
        m_fitnessProfileViewModel = ViewModelProviders.of(this)
                .get(FitnessProfileViewModel.class);
//        m_fitnessProfileViewModel.getFitnessProfile().observe(this, fitnessProfileObserver);
    }

    private void setDataToViewElements() {
        if (m_fitnessProfile != null) {
            m_firstName.setText(m_fitnessProfile.getValue().getM_fName());
            m_lastName.setText(m_fitnessProfile.getValue().getM_lName());
            m_sex.setText(m_fitnessProfile.getValue().getM_sex().toUpperCase());
            m_age.setText(calculateAge(m_fitnessProfile.getValue().getM_dob())+ "y");
            m_heightFeet.setText(String.valueOf(m_fitnessProfile.getValue().getM_heightFeet()));
            m_heightInches.setText(String.valueOf(m_fitnessProfile.getValue().getM_heightInches()));
            m_weight.setText(String.valueOf(m_fitnessProfile.getValue().getM_weightInPounds()));
            m_city.setText(m_fitnessProfile.getValue().getM_city());
            m_country.setText(m_fitnessProfile.getValue().getM_country());
            m_activity.setText(m_fitnessProfile.getValue().getM_lifestyleSelection());
            m_weightGoal.setText(String.valueOf(
                    m_fitnessProfile.getValue().getM_weightGoal()
                            + " " + m_fitnessProfile.getValue().getM_lbsPerWeek() + " lbs/week")
            );
//
//          if (m_photo != null) {
//            m_profilePhoto.setImageBitmap(m_photo);
//          }
        }
    }

    private void initViewElements(View v) {
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
    }

    //edit button on click fragment replace with edit fragment
    //id = btn_edit
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit: {
                Log.d(LOG_TAG, "Edit button onClick");
                m_listener.onProfileSummary_EditButton(true);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        Log.d(LOG_TAG, Constants.ATTACH);
        super.onAttach(context);
        try {
            m_listener = (OnProfileSummaryInteractionListener) context;
        } catch (ClassCastException cce){
            throw new ClassCastException(
                    context.toString()
                            + " must implement OnProfileSummaryInteractionListener");
        }
    }

    public interface OnProfileSummaryInteractionListener {
        void onProfileSummary_EditButton(boolean isClicked);
    }
}

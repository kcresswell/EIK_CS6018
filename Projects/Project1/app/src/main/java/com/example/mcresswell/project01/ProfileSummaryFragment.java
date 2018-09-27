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
    private onProfileSummaryInteractionListener listener;
    private UserProfileViewModel userProfileViewModel;
    private UserProfile userProfile;

    public ProfileSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE_VIEW);
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile_summary, container, false);

        editButton = v.findViewById(R.id.btn_edit);
        editButton.setOnClickListener(this);

        userProfileViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        userProfileViewModel.getUserProfile().observe(this, nameObserver);

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

//                data = new ArrayList<>();
//                mapper.forEach((key, val) -> {
//                    if (key.equals("")) {
//                        location.setText(val);
//                        return;
//                    }
//                    data.add(key + "\t" + val);
//                });
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
//                        R.layout.item_weather_widget,
//                        data);
//                setListAdapter(adapter);
//                mListener.onWeatherDataLoaded(weatherData);
//                listener.onProfileSummaryEditButton(userProfile);
                listener.onProfileSummaryEditButton();

                userProfileViewModel.setUserProfile(userProfile);
//                this.userProfile = userProfile;
            }


        }
    };

    //edit button on click fragment replace with edit fragment
    //id = btn_edit
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_edit) {
            Log.d(LOG, "Edit button onClick");
            listener.onProfileSummaryEditButton();
//            listener.onProfileSummaryEditButton(userProfileViewModel.getUserProfile().getValue());
        }
    }

    @Override
    public void onAttach(Context context) {
        Log.d(LOG, Constants.ATTACH);

        super.onAttach(context);
        if (context instanceof onProfileSummaryInteractionListener) {
            listener = (onProfileSummaryInteractionListener) context;
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

    public interface onProfileSummaryInteractionListener {
        void onProfileSummaryEditButton();
    }

}

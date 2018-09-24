package com.example.mcresswell.project01;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mcresswell.project01.util.Constants;


/**
 * Hiking fragment class.
 */
public class HikingFragment extends Fragment {

    private static final String LOG = HikingFragment.class.getSimpleName();

    private String city;
    private String country;
    private OnHikingFragmentInteractionListener mListener;

    public HikingFragment() { }

    public static HikingFragment newInstance(String currentLocation) {
        Log.d(LOG, Constants.NEW);
        HikingFragment fragment = new HikingFragment();
        Bundle args = new Bundle();
//        args.putString("location", currentLocation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            city = getArguments().getString("city");
//            country = getArguments().getString("country");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE_VIEW);

        View view = inflater.inflate(R.layout.fragment_hiking, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        Log.d(LOG, Constants.ATTACH);
        super.onAttach(context);
        if (context instanceof OnHikingFragmentInteractionListener) {
            mListener = (OnHikingFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHikingFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(LOG, Constants.DETACH);
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle state){
        super.onSaveInstanceState(state);
        Log.d(LOG, Constants.SAVE_INSTANCE_STATE);

    }

    /**
     *  Interface that is implemented by HikingActivity to allow interactions that occur
     *  in this fragment to be communicated with its hosting activity.
     **/
    public interface OnHikingFragmentInteractionListener {
        void onHikingFragmentInteraction();
    }


}

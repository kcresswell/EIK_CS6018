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


/**
 * Hiking fragment class.
 */
public class HikingFragment extends Fragment {
    private static final String LOG = HikingFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String location;

    private OnHikingFragmentInteractionListener mListener;

    public HikingFragment() { }

    public static HikingFragment newInstance(String currentLocation) {
        Log.d(LOG, "newInstance");

        HikingFragment fragment = new HikingFragment();
        Bundle args = new Bundle();
        args.putString("location", currentLocation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, "onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = getArguments().getString("location");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, "onCreateView");

        View view = inflater.inflate(R.layout.fragment_hiking, container, false);
        TextView locationText = view.findViewById(R.id.txtv_location);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onHikingFragmentInteraction();
        }
    }

    @Override
    public void onAttach(Context context) {
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
        super.onDetach();
        mListener = null;
    }

    /**
     *  Interface that is implemented by HikingActivity to allow interactions that occur
     *  in this fragment to be communicated with its hosting activity.
     **/
    public interface OnHikingFragmentInteractionListener {
        void onHikingFragmentInteraction();
    }


}

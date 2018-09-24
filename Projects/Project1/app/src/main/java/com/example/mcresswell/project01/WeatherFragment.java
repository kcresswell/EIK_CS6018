package com.example.mcresswell.project01;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mcresswell.project01.util.Constants;
import com.example.mcresswell.project01.util.WeatherForecast;
import com.example.mcresswell.project01.util.WeatherUtils;


/**
 * Weather fragment class.
 */
public class WeatherFragment extends Fragment {

    private static final String LOG = DashboardFragment.class.getSimpleName();

    private OnWeatherFragmentInteractionListener mListener;
    private WeatherForecast weatherForecast;
    private String forecast;

    public WeatherFragment() { }

    public static WeatherFragment newInstance(String forecastData) {
        Log.d(LOG, Constants.NEW);
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString("data", forecastData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            forecast = getArguments().getString("data");
            weatherForecast = WeatherUtils.getWeatherForecast(forecast);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE_VIEW);
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onAttach(Context context) {
        Log.d(LOG, Constants.ATTACH);

        super.onAttach(context);
        if (context instanceof OnWeatherFragmentInteractionListener) {
            mListener = (OnWeatherFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnWeatherFragmentInteractionListener");
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
        state.putString("data",forecast);
        Log.d(LOG, Constants.SAVE_INSTANCE_STATE);
        //TODO: Store any data

    }


    /**
     *  Interface that is implemented by WeatherActivity to allow interactions that occur
     *  in this fragment to be communicated with its hosting activity.
     **/
    public interface OnWeatherFragmentInteractionListener {
        void onWeatherFragmentInteraction();
    }
}

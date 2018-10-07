package com.example.mcresswell.project01.weather;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.ViewModels.FitnessProfileViewModel;
import com.example.mcresswell.project01.db.entity.FitnessProfile;
import com.example.mcresswell.project01.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * Weather fragment class.
 */
public class WeatherFragment extends ListFragment {

    private static final String LOG = WeatherFragment.class.getSimpleName();

    private OnWeatherDataLoadedListener mListener;
    //    private WeatherForecast weatherForecast;
    private WeatherViewModel weatherViewModel;
    private FitnessProfileViewModel fitnessProfileViewModel;

    private TextView location;
    Map<String, String> mapper;
    private ArrayList<String> data;
    private ListView mlistView;
    private ProgressDialog dialog;

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance(String city, String country) {
        Log.d(LOG, Constants.NEW);
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString("city", city);
        args.putString("country", country);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            weatherForecast = getArguments().getParcelable("data");
            Log.d(LOG, "City: " + getArguments().getString("city"));
            Log.d(LOG, "Country: " + getArguments().getString("country"));

        }
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        weatherViewModel.getForecastData().observe(this, weatherObserver);

        fitnessProfileViewModel = ViewModelProviders.of(this).get(FitnessProfileViewModel.class);
        subscribeToUserProfileModel();

        String city = getActivity().getIntent().getStringExtra("city");
        String country = getActivity().getIntent().getStringExtra("country");

        dialog = new ProgressDialog(getContext());
        dialog.setMessage(String.format("Loading weather for %s, %s...", city.replace("+", " "), country));
        dialog.show();

        loadWeatherData(city, country);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE_VIEW);
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        location = view.findViewById(R.id.weatherLocation);
        mlistView = view.findViewById(android.R.id.list);
        mlistView.setId(android.R.id.list);

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d(LOG, Constants.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        Log.d(LOG, Constants.ATTACH);

        super.onAttach(context);
        if (context instanceof OnWeatherDataLoadedListener) {
            mListener = (OnWeatherDataLoadedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnWeatherDataLoadedListener");
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
//        state.putParcelable("data", (Parcelable) weatherForecast);
        Log.d(LOG, Constants.SAVE_INSTANCE_STATE);

    }

    final Observer<WeatherForecast> weatherObserver = new Observer<WeatherForecast>() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onChanged(@Nullable final WeatherForecast weatherData) {
            if (weatherData != null) { //Weather data has finished being retrieved
                Log.d(LOG, "weatherObserver onChanged listener: weather data changed and is not null");
                dialog.dismiss();
                weatherData.printWeatherForecast();

                data = new ArrayList<>();
                mapper = createObjectMapper(weatherData);
                mapper.forEach((key, val) -> {
                    if (key.equals("")) {
                        location.setText(val);
                        return;
                    }
                    data.add(key + "\t" + val);
                });
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                        R.layout.item_weather_widget,
                        data);
                setListAdapter(adapter);
                mListener.onWeatherDataLoaded(weatherData);


            }


        }
    };

    private void subscribeToUserProfileModel() {
        fitnessProfileViewModel.getUserProfile().observe(this, new Observer<FitnessProfile>() {
            @Override
            public void onChanged(@Nullable FitnessProfile fitnessProfile) {
                //Now that valid user profile data has been entered, reload
                if (fitnessProfile != null) {
                    Log.d(LOG, "subscribeToUserProfileModel: FitnessProfileViewModel onChanged " +
                            "listener, re-fetching current weather based on user city and country");
                    loadWeatherData(fitnessProfile.getM_city(), fitnessProfile.getM_country());
                    dialog = new ProgressDialog(getContext());
                    dialog.setMessage(String.format("Loading weather for %s, %s...", fitnessProfile.getM_city(), fitnessProfile.getM_country()));
                    dialog.show();
                }
            }
        });
    }

    private void loadWeatherData(String city, String country){
        Log.d(LOG, "loadWeatherData");

        //pass the location in to the view model
        weatherViewModel.setLocation(city, country);
    }

    public Map<String, String> createObjectMapper(WeatherForecast data) {
        Map<String, String> mapper = new HashMap<String, String>();
        mapper.put("", data.getCity() + ", " + data.getCountryCode());
        mapper.put(getResources().getString(R.string.current_conditions_weather_widget), data.getForecastMain());
        mapper.put(getResources().getString(R.string.forecast_detail_weather_widget), data.getForecastDescription());
        mapper.put(getResources().getString(R.string.temp_weather_widget), data.getTemp());
        mapper.put(getResources().getString(R.string.temp_min_weather_widget), data.getTemp_min());
        mapper.put(getResources().getString(R.string.temp_max_weather_widget), data.getTemp_max());
        mapper.put(getResources().getString(R.string.humidity_weather_widget), data.getHumidity());
        mapper.put(getResources().getString(R.string.wind_weather_widget), data.getWindSpeed());
        mapper.put(getResources().getString(R.string.pressure_weather_widget), data.getPressure());

        return mapper;
    }

    /**
     *  Interface that is implemented by WeatherActivity to allow interactions that occur
     *  in this fragment to be communicated with its hosting activity.
     **/
    public interface OnWeatherDataLoadedListener {
        void onWeatherDataLoaded(WeatherForecast forecast);
    }
}
package com.example.mcresswell.project01.fragments;

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
import com.example.mcresswell.project01.viewmodel.FitnessProfileViewModel;
import com.example.mcresswell.project01.viewmodel.WeatherViewModel;
import com.example.mcresswell.project01.db.entity.Weather;
import com.example.mcresswell.project01.util.Constants;
import com.example.mcresswell.project01.util.WeatherUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.mcresswell.project01.util.WeatherUtils.convertAndFormatKelvinTemp;


/**
 * Weather fragment class.
 */
public class WeatherFragment extends ListFragment {

    private static final String LOG_TAG = WeatherFragment.class.getSimpleName();

    private OnWeatherDataLoadedListener mListener;
    private WeatherViewModel weatherViewModel;
    private FitnessProfileViewModel fitnessProfileViewModel;

    private TextView location;
    Map<String, String> mapper;
    private ArrayList<String> data;
    private ListView m_listView;

    public WeatherFragment() {
    }

    public static WeatherFragment newInstance() {
        Log.d(LOG_TAG, Constants.NEW);
        WeatherFragment fragment = new WeatherFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, Constants.CREATE);
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
////            weatherForecast = getArguments().getParcelable("data");
//            Log.d(LOG_TAG, "City: " + getArguments().getString("city"));
//            Log.d(LOG_TAG, "Country: " + getArguments().getString("country"));
//
//        }

        configureViewModels();


    }

    private void configureViewModels() {
        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        weatherViewModel.getWeather().observe(this, weather -> {
            if (weather != null) { //Weather data has finished being retrieved
                Log.d(LOG_TAG, "weatherObserver onChanged listener: weather data changed and is not null");

                WeatherUtils.printWeather(weather);

                displayWeatherWidget(weather);
            }
        });

        fitnessProfileViewModel = ViewModelProviders.of(this).get(FitnessProfileViewModel.class);

        fitnessProfileViewModel.getFitnessProfile().observe(this, fitnessProfile -> {
            //Upon updates to the fitness profile, reload weather data
            if (fitnessProfile != null) {
                Log.d(LOG_TAG, "FitnessProfileViewModel onChanged listener: an update to the value of the fitness" +
                        "profile stored by the view model has occurred");

                Log.d(LOG_TAG, String.format("Loading weather for user's location of %s, %s",
                        fitnessProfile.getM_city(), fitnessProfile.getM_country()));

                weatherViewModel.loadWeather(fitnessProfile.getM_city(), fitnessProfile.getM_country());
            }
        });
    }

    private void displayWeatherWidget(Weather weather) {
        data = new ArrayList<>();
        mapper = createObjectMapper(weather);
        mapper.forEach((key, val) -> {
            if (key.equals("location")) { //
                location.setText(val);
                return;
            }
            data.add(key + "\t" + val);
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                R.layout.item_weather_widget,
                data);
        setListAdapter(adapter);

        mListener.onWeatherDataLoaded(weather);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, Constants.CREATE_VIEW);
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        location = view.findViewById(R.id.weatherLocation);
        m_listView = view.findViewById(android.R.id.list);
        m_listView.setId(android.R.id.list);

        return view;
    }

    @Override
    public void onDestroyView() {
        Log.d(LOG_TAG, Constants.DESTROY_VIEW);
        super.onDestroyView();
    }

    @Override
    public void onAttach(Context context) {
        Log.d(LOG_TAG, Constants.ATTACH);

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
        Log.d(LOG_TAG, Constants.DETACH);
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle state){
        Log.d(LOG_TAG, Constants.SAVE_INSTANCE_STATE);
        super.onSaveInstanceState(state);
    }

    final Observer<Weather> weatherObserver = new Observer<Weather>() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onChanged(@Nullable final Weather weather) {
            if (weather != null) { //Weather data has finished being retrieved
                Log.d(LOG_TAG, "weatherObserver onChanged listener: weather data changed and is not null");

                WeatherUtils.printWeather(weather);

                data = new ArrayList<>();
                mapper = createObjectMapper(weather);
                mapper.forEach((key, val) -> {
                    if (key.equals("location")) { //
                        location.setText(val);
                        return;
                    }
                    data.add(key + "\t" + val);
                });
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                        R.layout.item_weather_widget,
                        data);
                setListAdapter(adapter);

                mListener.onWeatherDataLoaded(weather);


            }


        }
    };

    //    private void subscribeToUserProfileModel() {
//        fitnessProfileViewModel.getFitnessProfile().observe(this, fitnessProfile -> {
//            //Now that valid user profile data has been entered, reload
//            if (fitnessProfile != null) {
//                Log.d(LOG_TAG, "subscribeToUserProfileModel: FitnessProfileViewModel onChanged " +
//                        "listener, re-fetching current weather based on user city and country");
//                loadWeatherData(fitnessProfile.getM_city(), fitnessProfile.getM_country());
//                dialog = new ProgressDialog(getContext());
//                dialog.setMessage(String.format("Loading weather for %s, %s...", fitnessProfile.getM_city(), fitnessProfile.getM_country()));
//                dialog.show();
//            }
//        });

//    final Observer<WeatherForecast> weatherObserver = new Observer<WeatherForecast>() {
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        @Override
//        public void onChanged(@Nullable final WeatherForecast weatherData) {
//            if (weatherData != null) { //Weather data has finished being retrieved
//                Log.d(LOG_TAG, "weatherObserver onChanged listener: weather data changed and is not null");
//                dialog.dismiss();
//                weatherData.printWeatherForecast();
//
//                data = new ArrayList<>();
//                mapper = createObjectMapper(weatherData);
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
//
//
//            }
//
//
//        }
//    };

//    private void subscribeToUserProfileModel() {
//        fitnessProfileViewModel.getFitnessProfile().observe(this, fitnessProfile -> {
//            //Now that valid user profile data has been entered, reload
//            if (fitnessProfile != null) {
//                Log.d(LOG_TAG, "subscribeToUserProfileModel: FitnessProfileViewModel onChanged " +
//                        "listener, re-fetching current weather based on user city and country");
//                loadWeatherData(fitnessProfile.getM_city(), fitnessProfile.getM_country());
//                dialog = new ProgressDialog(getContext());
//                dialog.setMessage(String.format("Loading weather for %s, %s...", fitnessProfile.getM_city(), fitnessProfile.getM_country()));
//                dialog.show();
//            }
//        });




//    private void loadWeatherData(String city, String country){
//        Log.d(LOG_TAG, "loadWeatherData");
//
//        //pass the location in to the view model
//        weatherViewModel.setLocation(city, country);
//    }

    public Map<String, String> createObjectMapper(Weather data) {
        Map<String, String> mapper = new HashMap<String, String>();
        mapper.put("location", data.getCity().replace("+", " ") + ", " + data.getCountryCode());
        mapper.put(getResources().getString(R.string.current_conditions_weather_widget), data.getForecastMain());
        mapper.put(getResources().getString(R.string.forecast_detail_weather_widget), data.getForecastDescription());
        mapper.put(getResources().getString(R.string.temp_weather_widget), convertAndFormatKelvinTemp(data.getTemperature().temp));
        mapper.put(getResources().getString(R.string.temp_min_weather_widget), convertAndFormatKelvinTemp(data.getTemperature().tempMin));
        mapper.put(getResources().getString(R.string.temp_max_weather_widget), convertAndFormatKelvinTemp(data.getTemperature().tempMax));
        mapper.put(getResources().getString(R.string.humidity_weather_widget), String.valueOf(data.getHumidity()));
        mapper.put(getResources().getString(R.string.wind_weather_widget), String.valueOf(data.getWindSpeed()));
        mapper.put(getResources().getString(R.string.pressure_weather_widget), String.valueOf(data.getPressure()));

        return mapper;
    }

    /**
     *  Interface that is implemented by WeatherActivity to allow interactions that occur
     *  in this fragment to be communicated with its hosting activity.
     **/
    public interface OnWeatherDataLoadedListener {
        void onWeatherDataLoaded(Weather weather);
    }
}
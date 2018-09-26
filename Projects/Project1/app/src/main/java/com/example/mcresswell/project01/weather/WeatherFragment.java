package com.example.mcresswell.project01.weather;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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

    private OnWeatherFragmentInteractionListener mListener;
    private WeatherForecast weatherForecast;
    private WeatherViewModel weatherViewModel;


    //UI elements
//    private TextView forecastTitle;
    private TextView location;
    //    private TextView mainForecast, description, temp, tempMin, tempMax;
//    private Image weatherIcon;
//    private TextView pressure, humidity, wind; //optional extra forecast details
    Map<String, String> mapper;
    private ArrayList<String> data;
    private ListView listView;


    public WeatherFragment() { }

    public static WeatherFragment newInstance(WeatherForecast forecastData) {
        Log.d(LOG, Constants.NEW);
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        if (forecastData != null) {
            args.putParcelable("data", (Parcelable) forecastData);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weatherForecast = getArguments().getParcelable("data");
        }
//
//        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
//        weatherViewModel.getForecastData().observe(this, nameObserver);
//
//        String city = getActivity().getIntent().getStringExtra("city");
//        String country = getActivity().getIntent().getStringExtra("country");
//        loadWeatherData(city, country);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE_VIEW);
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        location = v.findViewById(R.id.weatherLocation);
        listView = v.findViewById(android.R.id.list);
        listView.setId(android.R.id.list);

        data = new ArrayList<>();
//        description = v.findViewById(R.id.)
        if (weatherForecast != null) { //Load and display data
            mapper = createObjectMapper(weatherForecast);
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
        }
        return v;
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
        state.putParcelable("data", (Parcelable) weatherForecast);
        Log.d(LOG, Constants.SAVE_INSTANCE_STATE);

    }

//    final Observer<WeatherForecast> nameObserver  = new Observer<WeatherForecast>() {
//        @RequiresApi(api = Build.VERSION_CODES.N)
//        @Override
//        public void onChanged(@Nullable final WeatherForecast weatherData) {
//            if (weatherData != null) { //Weather data has finished being retrieved
//                weatherData.printWeatherForecast();
////                boolean isTabletView = getResources().getBoolean(R.bool.isWideDisplay);
//
//                data = new ArrayList<>();
////        description = v.findViewById(R.id.)
//                if (weatherForecast != null) { //Load and display data
//                    mapper = createObjectMapper(weatherForecast);
//                    mapper.forEach((key, val) -> {
//                        if (key.equals("")) {
//                            location.setText(val);
//                            return;
//                        }
//                        data.add(key + "\t" + val);
//                    });
//                    ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
//                            R.layout.item_weather_widget,
//                            data);
//                    setListAdapter(adapter);
//                    listView.setAdapter(adapter);
//
//                }
//
//
//            }
//        }
//    };

//    private void loadWeatherData(String city, String country){
//        Log.d(LOG, "loadWeatherData");
//
//        //pass the location in to the view model
//        weatherViewModel.setLocation(city, country);
//    }

//    public void displayWeatherWidget(WeatherForecast weatherForecast, boolean isTabletView) {
//        mListener.onWeatherFragmentInteraction(weatherForecast);
////        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        Fragment mostRecentFragment = null;
//        if (isTabletView) {
//            Log.d(LOG, "TABLET");
//            mostRecentFragment = fragmentManager.findFragmentById(R.id.fl_detail_wd);
//
//            if (mostRecentFragment == null) {
//                Log.d(LOG, "mostRecentFragment is null");
//
//                fragmentTransaction.add(R.id.fl_detail_wd, this);
//            } else {
//                fragmentTransaction.replace(R.id.fl_detail_wd, this);
//            }
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        } else { //Mobile
//            Log.d(LOG, "MOBILE");
//
//            mostRecentFragment = fragmentManager.findFragmentById(R.id.fl_activity_weather);
//            fragmentTransaction.show(mostRecentFragment);
////
////            if (mostRecentFragment == null) {
////                Log.d(LOG, "mostRecentFragment is null");
////
////                fragmentTransaction.add(R.id.fl_activity_weather, this);
////            } else {
////                fragmentTransaction.replace(R.id.fl_activity_weather, this);
////
////            }
////            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        }
//    }




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
    public interface OnWeatherFragmentInteractionListener {
        void onWeatherFragmentInteraction(WeatherForecast forecast);
    }
}
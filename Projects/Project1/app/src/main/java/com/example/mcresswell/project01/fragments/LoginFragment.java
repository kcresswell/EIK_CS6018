package com.example.mcresswell.project01.fragments;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mcresswell.project01.Activities.CreateAccountActivity;
import com.example.mcresswell.project01.Activities.DashboardActivity;
import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.ViewModels.UserListViewModel;
import com.example.mcresswell.project01.ViewModels.UserViewModel;
import com.example.mcresswell.project01.ViewModels.WeatherListViewModel;
import com.example.mcresswell.project01.ViewModels.WeatherViewModel;
import com.example.mcresswell.project01.db.entity.User;

import static com.example.mcresswell.project01.util.Constants.CREATE_VIEW;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidEmailAndPassword;

public class LoginFragment extends Fragment {

    private static final String LOG_TAG = LoginFragment.class.getSimpleName();
    private Button m_loginButton;
    private Button m_createUserLink;
    private EditText m_email, m_password;

    private UserListViewModel userListViewModel;
    private UserViewModel userViewModel;
    private WeatherListViewModel weatherListViewModel;
    private WeatherViewModel weatherViewModel;
//    private FitnessProfileViewModel fitnessProfileViewModel;

    public LoginFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configureViewModels();

        weatherViewModel.loadWeather("TOKYO", "JP");

        if (weatherViewModel.getWeather().getValue() != null) {
            Log.d(LOG_TAG, " !!!!!!!!!!!!!!! " + weatherViewModel.getWeather().getValue().getCity() + "\t" + weatherViewModel.getWeather().getValue().getCountryCode() + " !!!!!!!!!!!!!");
        }

    }

    private void configureViewModels() {

        userListViewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
        userListViewModel.getUserList().observe(this, userList -> {
            if (userList != null) {
                Log.d(LOG_TAG, "Update to user list view model");
                Log.d(LOG_TAG, "Number of users in User database: " + userList.size());
            }
        });

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUser().observe(this, user -> {
            if (user != null) {
                Log.d(LOG_TAG, "Update to user view model");
                Log.d(LOG_TAG, String.format("User: %s \t %s", user.getEmail(), user.getPassword()));
            }
        });

        weatherListViewModel = ViewModelProviders.of(this).get(WeatherListViewModel.class);
        weatherListViewModel.getWeatherDataFromDatabase().observe(this, weatherList -> {
            if (weatherList != null) {
                Log.d(LOG_TAG, "Update to weather list view model");
                Log.d(LOG_TAG, "Number of weather records in Weather database: " + weatherList.size());

                weatherList.forEach(weather -> {
                    Log.d(LOG_TAG, "Weather Data record: " + weather.getId() + "\t'" + weather.getCity() + "'\t'" + weather.getCountryCode() + "'\t'" + weather.getLastUpdated() + "'");
                });
            }
        });

        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
        weatherViewModel.getWeather().observe(this, weather -> {
            if (weather != null) {
                Log.d(LOG_TAG, "Update to weather view model");
                Log.d(LOG_TAG, String.format("Weather record for %s, %s", weather.getCity(), weather.getCountryCode()));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, CREATE_VIEW);

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initializeFragmentView(view);

        setOnClickListeners();

        return view;
    }

    private void initializeFragmentView(View view){
        m_email = view.findViewById(R.id.text_email_login);
        m_password = view.findViewById(R.id.text_password_login);
        m_loginButton = view.findViewById(R.id.btn_login);
        m_createUserLink = view.findViewById(R.id.link_create_account);
    }

    private void setOnClickListeners() {

        m_createUserLink.setOnClickListener(listener -> createAccountHandler());

        m_loginButton.setOnClickListener(view -> {

            String email = m_email.getText().toString();
            String password = m_password.getText().toString();

            if (!isValidEmailAndPassword(email, password)) {

                Toast.makeText(getContext(),
                        "Please enter a valid email and password.",
                        Toast.LENGTH_SHORT).show();

                return;
            }

            authenticateUserLoginCredentials(email, password);

        });

    }

    private void authenticateUserLoginCredentials(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        LiveData<User> lookupResult = userViewModel.findUser(email);
        if(userViewModel.getUser() != null && userViewModel.getUser().getValue().getEmail().equals(email)) {
//        if (lookupResult.getValue() != null && userViewModel.authenticateUser(user)) {
            Toast.makeText(getContext(), "Login success", Toast.LENGTH_SHORT).show();

            loginSuccessHandler(userViewModel.getUser().getValue().getId());

            //TODO: use fitnessProfileViewModel to call FitnessProfileRepository to retrieve fitness profile from fitness_profile_id. Either take user to dashboard or take user to ProfileSummaryFragment screen and use the retrieved fitness profile data to display that user's fitness data.

            return;
        }

        Toast.makeText(getContext(), "Invalid login credentials.", Toast.LENGTH_SHORT).show();
        m_password.setText("");
    }

    private void createAccountHandler() {
        Intent intent = new Intent(getActivity(), CreateAccountActivity.class);

        startActivity(intent);
    }

    private void loginSuccessHandler(int fitnessProfileId) {
        Intent intent = new Intent(getActivity(), DashboardActivity.class);

        if (Integer.valueOf(fitnessProfileId) != null) { //Have to cast to Integer type to do null check

            //The following step passes the fitnessProfileId so that the correct fitness profile can be loaded for the corresponding user that just logged in
            intent.putExtra("id", fitnessProfileId);
        }

        startActivity(intent);
    }

}

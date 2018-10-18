package com.example.mcresswell.project01.fragments;

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

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.activities.CreateAccountActivity;
import com.example.mcresswell.project01.activities.DashboardActivity;
import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.viewmodel.UserListViewModel;
import com.example.mcresswell.project01.viewmodel.UserViewModel;
import com.example.mcresswell.project01.viewmodel.WeatherListViewModel;
import com.example.mcresswell.project01.viewmodel.WeatherViewModel;

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

    public LoginFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configureViewModels();

    }

    private void configureViewModels() {

        userListViewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
        userListViewModel.getUserList().observe(this, userList -> {
            if (userList != null) {
                Log.d(LOG_TAG, "Update to user list view model");
                Log.d(LOG_TAG, "Number of users in User database: " + userList.size());

                Log.d(LOG_TAG, "------------------------------------------");

                Log.d(LOG_TAG, "PRINTING USERS IN USER DATABASE");
                Log.d(LOG_TAG, "\n");

                userList.forEach(users -> {
                    Log.d(LOG_TAG, "\nUser data record: " + users.getId() + "\t'" + users.getEmail() + "'\t'" + users.getFirstName() + "'\t'" + users.getLastName() + "'\t'" + users.getJoinDate() + "'\t'" + "'");
                });

                Log.d(LOG_TAG, "\n");
                Log.d(LOG_TAG, "------------------------------------------");

                userListViewModel.resetUserTable(userList.size());
            }
        });

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUser().observe(this, user -> {
            if (user != null) {
                Log.d(LOG_TAG, "Update to user view model");
                Log.d(LOG_TAG, String.format("User: %s \t %s", user.getEmail(), user.getPassword()));

            }
        });

//        weatherListViewModel = ViewModelProviders.of(this).get(WeatherListViewModel.class);
//        weatherListViewModel.getWeatherDataFromDatabase().observe(this, weatherList -> {
//            if (weatherList != null) {
//                Log.d(LOG_TAG, "Update to weather list view model");
//                Log.d(LOG_TAG, "Number of weather records in Weather database: " + weatherList.size());
//
//                Log.d(LOG_TAG, "------------------------------------------");
//
//                Log.d(LOG_TAG, "PRINTING WEATHER RECORDS IN WEATHER DATABASE");
//                Log.d(LOG_TAG, "\n");
//                weatherList.forEach(weather -> {
//                    Log.d(LOG_TAG, "\nWeather Data record: " + weather.getId() + "\t'" + weather.getCity() + "'\t'" + weather.getCountryCode() + "'\t'" + weather.getLastUpdated() + "'");
//                });
//
//                Log.d(LOG_TAG, "\n");
//                Log.d(LOG_TAG, "------------------------------------------");
//
////                weatherViewModel.loadWeather("Tokyo", "Japan");
//            }
//        });
//
//        weatherViewModel = ViewModelProviders.of(this).get(WeatherViewModel.class);
//        weatherViewModel.getWeather().observe(this, weather -> {
//            if (weather != null) {
//                Log.d(LOG_TAG, "Update to weather view model");
//                Log.d(LOG_TAG, String.format("Weather record for %s, %s was last retrieved at %s", weather.getCity(), weather.getCountryCode(), weather.getLastUpdated().toString()));
//            }
//        });
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

        userViewModel.findUser(email).observe(this, user1 -> {
            if (user1 != null && user1.getEmail().equals(email) && user1.getPassword().equals(password)) {
                Toast.makeText(getContext(), "Login success", Toast.LENGTH_SHORT).show();

                loginSuccessHandler(userViewModel.getUser().getValue().getId());
            } else {
                Toast.makeText(getContext(), "Invalid login credentials.", Toast.LENGTH_SHORT).show();

                m_password.setText("");
            }
        });
    }

    private void createAccountHandler() {
        Intent intent = new Intent(getActivity(), CreateAccountActivity.class);

        startActivity(intent);
    }

    private void loginSuccessHandler(int userId) {
        Intent intent = new Intent(getActivity(), DashboardActivity.class);

        if (Integer.valueOf(userId) != null) { //Have to cast to Integer type to do null check

            //The following step passes the fitnessProfileId so that the correct fitness profile can be loaded for the corresponding user that just logged in
            intent.putExtra("id", userId);
        }

        startActivity(intent);
    }

}

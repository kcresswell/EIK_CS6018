package com.example.mcresswell.project01.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mcresswell.project01.BuildConfig;
import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.activities.CreateAccountActivity;
import com.example.mcresswell.project01.activities.DashboardActivity;
import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.db.entity.Weather;
import com.example.mcresswell.project01.viewmodel.UserListViewModel;
import com.example.mcresswell.project01.viewmodel.UserViewModel;
import com.example.mcresswell.project01.viewmodel.WeatherListViewModel;
import com.example.mcresswell.project01.viewmodel.WeatherViewModel;

import java.util.List;
import java.util.Locale;

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

        Observer userListObserver = new Observer <List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> userList) {
                if(userList != null) {
                    Log.d(LOG_TAG, "LOGIN FRAGMENT userListViewModel user list changed and isnt null ");

                    userListViewModel.getUserList().removeObserver(this);

                    return;
                }
            }
        };

        Observer userObserver = new Observer <User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if(user != null) {
                    Log.d(LOG_TAG, "Update to user view model, no longer null");
                    Log.d(LOG_TAG, String.format("User: %s \t %s \t %s", user.getEmail(), user.getFirstName(), user.getLastName()));
                    userViewModel.getUser().removeObserver(this);

                    userViewModel.resetUser();
                    return;
                }
            }
        };

        Observer weatherListObserver = new Observer <List<Weather>>() {
            @Override
            public void onChanged(@Nullable List<Weather> weatherList) {
                if(weatherList != null) {
                    Log.d(LOG_TAG, "Weather list view model has changed and isnt null");
                    weatherListViewModel.getWeatherDataFromDatabase().removeObserver(this);

                    return;
                }
            }
        };

        userListViewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
        userListViewModel.getUserList().observe(this, userListObserver);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUser().observe(this, userObserver);

        weatherListViewModel = ViewModelProviders.of(this).get(WeatherListViewModel.class);
        weatherListViewModel.getWeatherDataFromDatabase().observe(this, weatherListObserver);
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

            loginSuccessHandler();

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
        userViewModel.findUser(email);

        Observer userObserver = new Observer <User>() {
            @Override
            public void onChanged(@Nullable User user1) {
//        userViewModel.getUser().observe(this, user1 -> {
                if (user1 != null) {

                    userViewModel.getUser().removeObserver(this);

                    if (user1.getEmail().equals(email) && user1.getPassword().equals(password)) {
                        Toast.makeText(getContext(), "Login success", Toast.LENGTH_SHORT).show();

                        loginSuccessHandler();
                    }
//                    if (!(user1.getEmail().equals(email) || user1.getPassword().equals(password))) {
                    else {
                        Log.d(LOG_TAG, String.format(Locale.US,"User data stored in view model : %s %s %s %s", user1.getEmail(), user1.getFirstName(), user1.getLastName(), user1.getPassword()));
                        Toast.makeText(getContext(), "Invalid login credentials.", Toast.LENGTH_SHORT).show();

                        m_password.setText("");

                    }
                }
            }
        };
        userViewModel.getUser().observe(this, userObserver);

    }

        private void createAccountHandler() {
            Intent intent = new Intent(getActivity(), CreateAccountActivity.class);

            startActivity(intent);
        }

        private void loginSuccessHandler() {
//        if (!getResources().getBoolean(R.bool.isWideDisplay)) {
            Intent intent = new Intent(getActivity(), DashboardActivity.class);
            startActivity(intent);
//        } else {
//            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fl_master_wd, new DashboardFragment());
//            fragmentTransaction.replace(R.id.fl_detail_wd, new FitnessDetailsFragment());
//            fragmentTransaction.addToBackStack(null);
//            fragmentTransaction.commit();
//        }
        }

    }

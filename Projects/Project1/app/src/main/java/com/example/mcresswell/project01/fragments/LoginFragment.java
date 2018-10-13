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
import com.example.mcresswell.project01.db.entity.User;

import static com.example.mcresswell.project01.util.ValidationUtils.isValidEmailAndPassword;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final String LOG = LoginFragment.class.getSimpleName();

    private Button m_btn_login;
    private Button m_btn_createUser;
    private EditText m_email, m_password;

    private UserListViewModel userListViewModel;
    private UserViewModel userViewModel;
//    private FitnessProfileViewModel fitnessProfileViewModel;


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
                Log.d(LOG, "Update to user list view model");
                Log.d(LOG, "Number of users in User table: " + userList.size());
            }
        });


        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getUser().observe(this, user -> {
            Log.d(LOG, "userviewmodel getuser observer onchanged!!!");
            if (user != null) {
                Log.d(LOG, "Update to user view model");
                Log.d(LOG, String.format("User: %s \t %s", user.getEmail(), user.getPassword()));

            } else {
                Log.d(LOG, "NULL NULL NULL NULL NULL");

            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initializeFragmentView(view);

        setOnClickListeners();

        return view;
    }

    private void initializeFragmentView(View view){
        m_email = view.findViewById(R.id.text_email_login);
        m_password = view.findViewById(R.id.text_password_login);

        m_btn_login = view.findViewById(R.id.btn_login);
        m_btn_createUser = view.findViewById(R.id.link_create_account);
    }

    private void setOnClickListeners() {

        m_btn_createUser.setOnClickListener(listener -> createAccountHandler());

        m_btn_login.setOnClickListener(view -> {

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

//
//            User userResult = userListViewModel.retrieveUser(userToAuth);
//            if (userResult == null) {
//                Toast.makeText(getContext(),
//                        "No account with that email address exists.", Toast.LENGTH_SHORT).show();
//            }
//            else if (!userListViewModel.authenticateUser(userToAuth)) {
//                Toast.makeText(getContext(),
//                        "Incorrect password for user account was entered.", Toast.LENGTH_SHORT).show();
//            }
//             userListViewModel.retrieveAllUsers();

//            LiveData<List<User>> userList = userListViewModel.getUserList();
//            if (userList == null) {
//                Log.d(LOG, "NULL NULL NULL!");
//                return;
//            }
//            userList.getValue().forEach(e -> {
//                Log.d(LOG, "FIRST NAME: " + e.getFirstName());
//                Log.d(LOG, "Last name: " + e.getLastName());
//                Log.d(LOG, "EMAIL: " + e.getEmail());
//                Log.d(LOG, "password: " + e.getPassword());
//
//            });

    }

    private void authenticateUserLoginCredentials(String email, String password) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);

        LiveData<User> lookupResult = userViewModel.findUser(email);

        if (lookupResult.getValue() != null && userViewModel.authenticateUser(user)) {
            Toast.makeText(getContext(), "Login success", Toast.LENGTH_SHORT).show();

            final int fitnessProfileId = lookupResult.getValue().getFitnessProfileId();

            loginSuccessHandler();

            //TODO: use fitnessProfileViewModel to call FitnessProfileRepository to retrieve fitness profile from fitness_profile_id. Either take user to dashboard or take user to ProfileSummaryFragment screen and use the retrieved fitness profile data to display that user's fitness data.

        } else {
            Toast.makeText(getContext(), "Invalid login credentials.", Toast.LENGTH_SHORT).show();
            m_password.setText("");

        }
    }

//    @Override
//    public void onClick(View view) {
//        Log.d(LOG, "onClick");
//        switch (view.getId()){
//            case R.id.btn_login: {
//                Toast.makeText(getContext(),"Login",Toast.LENGTH_SHORT).show();
//                loginSuccessHandler();
//                break;
//            }
//            case R.id.link_create_account: {
//                Toast.makeText(getContext(),"Create Account",Toast.LENGTH_SHORT).show();
//                createAccountHandler();
//                break;
//            }
//
//        }
//    }

    private void createAccountHandler() {
        Intent intent = new Intent(getActivity(), CreateAccountActivity.class);
        startActivity(intent);
    }

    private void loginSuccessHandler() {
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        startActivity(intent);
    }

}

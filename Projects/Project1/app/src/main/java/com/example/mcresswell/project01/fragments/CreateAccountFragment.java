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

import com.example.mcresswell.project01.Activities.ProfileEntryActivity;
import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.ViewModels.UserViewModel;
import com.example.mcresswell.project01.db.entity.User;
import com.example.mcresswell.project01.util.Constants;

import java.time.Instant;
import java.util.Date;

import static com.example.mcresswell.project01.util.ValidationUtils.isNotNullOrEmpty;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidAlphaChars;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidEmail;

/**
 * Fragment class for screen that user sees when the Create Account button is pressed in the Login screen.
 */
public class CreateAccountFragment extends Fragment {

    private static final String LOG = CreateAccountFragment.class.getSimpleName();

    private Button m_btn_createAccount;
    private EditText m_email, m_password, m_firstName, m_lastName;
    private UserViewModel userViewModel;

    public CreateAccountFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE);
        super.onCreate(savedInstanceState);

        configureViewModels();
    }

    private void configureViewModels() {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        userViewModel.getUser().observe(this, user -> {
            Log.d(LOG, "UserViewModel observer for getUser()");
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG, Constants.CREATE_VIEW);

        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        initializeFragmentView(view);

        setOnClickListeners();

        return view;
    }

    private void initializeFragmentView(View view){
        m_email = view.findViewById(R.id.text_email_create_account);
        m_password = view.findViewById(R.id.text_password_create_account);
        m_firstName = view.findViewById(R.id.text_first_name_create_account);
        m_lastName = view.findViewById(R.id.text_last_name_create_account);
        m_btn_createAccount = view.findViewById(R.id.btn_create_account);
    }

    private void setOnClickListeners() {
        m_btn_createAccount.setOnClickListener(listener -> createAccountButtonHandler());
    }

    private void createAccountButtonHandler() {
        if (isAccountDataValid()) {
            if (!isUniqueUserLogin(m_email.getText().toString())) {
                Toast.makeText(getContext(), "A user account with that email already exists.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            User newUser = new User();
            newUser.setEmail(m_email.getText().toString());
            newUser.setPassword(m_password.getText().toString());
            newUser.setFirstName(m_firstName.getText().toString());
            newUser.setLastName(m_lastName.getText().toString());
            newUser.setJoinDate(Date.from(Instant.now()));
            //Add new user to database
            userViewModel.createUser(newUser);

            Intent intent = new Intent(getActivity(), ProfileEntryActivity.class);
            startActivity(intent);
        }
    }

    private boolean isUniqueUserLogin(String email) {
        LiveData<User> userResult = userViewModel.findUser(email);
        if (userResult.getValue() != null && userResult.getValue().getEmail().equals(email)) {
            Log.d(LOG, "Email is not unique. A user with that email already exists.");
//            Log.d(LOG, "EMAIL: " + userViewModel.getUser().getValue().getEmail());
//            Log.d(LOG, "PASSOWRD: " + userViewModel.getUser().getValue().getPassword());
//            Log.d(LOG, "First name: " +userViewModel.getUser().getValue().getFirstName());
//            Log.d(LOG, "Last Name: " + userViewModel.getUser().getValue().getLastName());
//            Log.d(LOG, "Date joined: " +userViewModel.getUser().getValue().getJoinDate());

            Log.d(LOG, "EMAIL: " + userResult.getValue().getEmail());
            Log.d(LOG, "PASSOWRD: " + userResult.getValue().getPassword());
            Log.d(LOG, "First name: " + userResult.getValue().getFirstName());
            Log.d(LOG, "Last Name: " + userResult.getValue().getLastName());
            Log.d(LOG, "Date joined: " + userResult.getValue().getJoinDate());

            return false;
        }
        return true;
    }

    private boolean isAccountDataValid () {
        if (!isValidEmail(m_email.getText().toString())) {
            Toast.makeText(getContext(), "Invalid email.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isNotNullOrEmpty(m_password.getText().toString())) {
            Toast.makeText(getContext(), "Invalid password.", Toast.LENGTH_SHORT).show();

        }
        else if (!isValidAlphaChars(m_firstName.getText().toString())) {
            Toast.makeText(getContext(), "Invalid first name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!isValidAlphaChars(m_lastName.getText().toString())) {
            Toast.makeText(getContext(), "Invalid last name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onViewStateRestored (Bundle savedInstanceState) {
        Log.d(LOG, "onViewStateRestored");
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        Log.d(LOG, Constants.SAVE_INSTANCE_STATE);

        super.onSaveInstanceState(bundle);
    }
}

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
import com.example.mcresswell.project01.ViewModels.UserViewModel;
import com.example.mcresswell.project01.db.entity.User;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import static com.example.mcresswell.project01.util.ValidationUtils.isNotNullOrEmpty;
import static com.example.mcresswell.project01.util.ValidationUtils.isValidEmail;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String LOG = LoginFragment.class.getSimpleName();

    private Button m_btn_login;
    private Button m_btn_createUser;
    private EditText m_email, m_password;

    private UserViewModel m_userViewModel;
//    private FitnessProfileViewModel fitnessProfileViewModel;


    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
//        fitnessProfileViewModel = ViewModelProviders.of(this).get(FitnessProfileViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initializeFragmentView(view);

        setButtonListeners();

        return view;
    }

    private void initializeFragmentView(View view){
        m_email = view.findViewById(R.id.text_email_login);
        m_password = view.findViewById(R.id.text_password_login);

        m_btn_login = view.findViewById(R.id.btn_login);
        m_btn_createUser = view.findViewById(R.id.link_create_account);
    }

    private void setButtonListeners() {

        m_btn_createUser.setOnClickListener(this);

        m_btn_login.setOnClickListener(view -> {

            String email = m_email.getText().toString();
            String pass = m_password.getText().toString();

            if (!isValidEmail(email) || !isNotNullOrEmpty(pass)) {
                Toast.makeText(getContext(),
                        "Please enter a valid email and password.", Toast.LENGTH_SHORT).show();
                return;
            }

            User userToAuth = new User();
            userToAuth.setEmail(email);
            userToAuth.setPassword(pass);
            userToAuth.setFirstName("TestFirst");
            userToAuth.setLastName("TestLast");
            userToAuth.setJoinDate(Date.from(Instant.now()));
            userToAuth.setFitnessProfileId(1);

//            m_userViewModel.getUser().observe(this, user -> up);
//            m_userViewModel.createUser(userToAuth);

            User userResult = m_userViewModel.retrieveUser(userToAuth);
            if (userResult == null) {
                Toast.makeText(getContext(),
                        "No account with that email address exists.", Toast.LENGTH_SHORT).show();
            }
            else if (!m_userViewModel.authenticateUser(userToAuth)) {
                Toast.makeText(getContext(),
                        "Incorrect password for user account was entered.", Toast.LENGTH_SHORT).show();
            }
             m_userViewModel.retrieveAllUsers();

            LiveData<List<User>> userList = m_userViewModel.getUserList();
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


            //Clear input field data
            m_password.setText("");
            m_email.setText("");

        });

    }

    @Override
    public void onClick(View view) {
        Log.d(LOG, "onClick");
        switch (view.getId()){
            case R.id.btn_login: {
                Toast.makeText(getContext(),"Login",Toast.LENGTH_SHORT).show();
                loginButtonHandler();
                break;
            }
            case R.id.link_create_account: {
                Toast.makeText(getContext(),"Create Account",Toast.LENGTH_SHORT).show();
                createAccountHandler();
                break;
            }

        }
    }

    private void createAccountHandler() {
        Intent intent = new Intent(getActivity(), CreateAccountActivity.class);
        startActivity(intent);
    }

    private void loginButtonHandler() {
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        startActivity(intent);
    }


}

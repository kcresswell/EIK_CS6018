package com.example.mcresswell.project01.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.mcresswell.project01.Activities.CreateAccountActivity;
import com.example.mcresswell.project01.Activities.DashboardActivity;
import com.example.mcresswell.project01.Activities.ProfileEntryActivity;
import com.example.mcresswell.project01.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final String LOG = LoginFragment.class.getSimpleName();

    private Button m_btn_login;
    private Button m_btn_createUser;

    public LoginFragment() {
        // Required empty public constructor
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
        m_btn_login = (Button) view.findViewById(R.id.btn_login);
        m_btn_createUser = (Button) view.findViewById(R.id.btn_create_account);
    }

    private void setButtonListeners() {
        m_btn_login.setOnClickListener(this);
        m_btn_createUser.setOnClickListener(this);
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
            case R.id.btn_create_account: {
                Toast.makeText(getContext(),"Create Account",Toast.LENGTH_SHORT).show();
                createAccountButtonHandler();
                break;
            }

        }
    }

    private void createAccountButtonHandler() {
        Intent intent = new Intent(getActivity(), CreateAccountActivity.class);
        startActivity(intent);
    }

    private void loginButtonHandler() {
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        startActivity(intent);
    }


}

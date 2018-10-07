package com.example.mcresswell.project01;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mcresswell.project01.fragments.LoginFragment;

public class LoginActivity extends AppCompatActivity {
    private FragmentTransaction m_fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(isWideDisplay()){
            //present fragment to display
            m_fTrans = getSupportFragmentManager().beginTransaction();
            m_fTrans.replace(R.id.fl_login_wd, new LoginFragment(), "v_frag_dashboard");
            m_fTrans.commit();
        } else {
            //present fragment to display
            m_fTrans = getSupportFragmentManager().beginTransaction();
            m_fTrans.replace(R.id.fl_login_nd, new LoginFragment(), "v_frag_dashboard");
            m_fTrans.commit();
        }
    }

    private boolean isWideDisplay(){
        return getResources().getBoolean(R.bool.isWideDisplay);
    }
}

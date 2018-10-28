package com.example.mcresswell.project01.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.fragments.LoginFragment;
import com.example.mcresswell.project01.ui.RV_Adapter;

import static com.example.mcresswell.project01.util.Constants.CREATE;

public class LoginActivity extends AppCompatActivity
        implements RV_Adapter.OnAdapterDataChannel {

    private static final String LOG_TAG = LoginActivity.class.getSimpleName();
    private FragmentTransaction m_fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, CREATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getResources().getBoolean(R.bool.isWideDisplay)){
            m_fTrans = getSupportFragmentManager().beginTransaction();
            m_fTrans.replace(R.id.fl_detail_wd, new LoginFragment(), "v_frag_dashboard");
            m_fTrans.commit();
        } else {
            m_fTrans = getSupportFragmentManager().beginTransaction();
            m_fTrans.replace(R.id.fl_login_nd, new LoginFragment(), "v_frag_dashboard");
            m_fTrans.commit();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "onBackPressed");
        //Do nothing when back button is pressed on the Login screen

    }

    @Override
    public void onAdapterDataPass(int position) {

    }
}

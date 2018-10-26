package com.example.mcresswell.project01.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.fragments.LoginFragment;
import com.example.mcresswell.project01.ui.RV_Adapter;

public class LoginActivity extends AppCompatActivity implements RV_Adapter.OnAdapterDataChannel{

    private static final String LOG = LoginActivity.class.getSimpleName();
    private FragmentTransaction m_fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(isWideDisplay()){
            //present fragment to display
            m_fTrans = getSupportFragmentManager().beginTransaction();
            m_fTrans.replace(R.id.fl_detail_wd, new LoginFragment(), "v_frag_dashboard");
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

    @Override
    public void onBackPressed() {
        Log.d(LOG, "onBackPressed");

        //Do nothing when back button is pressed on the Login screen

    }

    @Override
    public void onAdapterDataPass(int position) {

    }
}

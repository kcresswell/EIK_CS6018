package com.example.mcresswell.project01.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.fragments.AccountSettingsFragment;
import com.example.mcresswell.project01.fragments.DashboardFragment;
import com.example.mcresswell.project01.fragments.FitnessDetailsFragment;

import static com.example.mcresswell.project01.util.Constants.CREATE;

public class AccountSettingsActivity extends AppCompatActivity {

    private static final String LOG_TAG = CreateAccountActivity.class.getSimpleName();
    private FragmentTransaction m_fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, CREATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        m_fTrans = getSupportFragmentManager().beginTransaction();
        if(isWideDisplay()){
            m_fTrans.replace(R.id.fl_create_account_wd, new AccountSettingsFragment(), "v_frag_dashboard");
            m_fTrans.commit();
        } else {
            m_fTrans.replace(R.id.fl_create_account_nd, new AccountSettingsFragment(), "v_frag_dashboard");
            m_fTrans.commit();
        }
    }

    private boolean isWideDisplay(){
        return getResources().getBoolean(R.bool.isWideDisplay);
    }

    /**
     * Helper method to restore the default app view to
     * make sure user doesn't end up with a blank app screen.
     * from pressing back repeatedly in the app.
     */
    private void restoreDefaultDashboardView() {
        m_fTrans = getSupportFragmentManager().beginTransaction();

        if (!isWideDisplay()) {
            m_fTrans.replace(R.id.fl_master_nd, new DashboardFragment(), "v_frag_dashboard");
            m_fTrans.commit();
        } else {
            m_fTrans.replace(R.id.fl_master_wd, new DashboardFragment(), "v_frag_dashboard");
            m_fTrans.replace(R.id.fl_detail_wd, new FitnessDetailsFragment(), "v_frag_fitness");
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, "onBackPressed");
        restoreDefaultDashboardView();
    }
}

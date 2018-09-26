package com.example.mcresswell.project01;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FitnessActivity extends AppCompatActivity {
    private FragmentTransaction m_fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        //present fragment to display
        m_fTrans = getSupportFragmentManager().beginTransaction();
        m_fTrans.replace(R.id.fl_master_nd, new FitnessDetailsFragment(), "v_frag_dashboard");
        m_fTrans.commit();
    }
}

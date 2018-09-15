package com.example.mcresswell.project01;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements ProfileEntryFrag.OnDataChannel {

    //memeber variables
    private FragmentTransaction m_fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //If not saved instance state, build initial fragment
        if(savedInstanceState == null){
            //create fragment
            ProfileEntryFrag frag_profileEntry = new ProfileEntryFrag();

            //present fragment to display
            m_fTrans = getSupportFragmentManager().beginTransaction();
            m_fTrans.replace(R.id.fl_master_nd, frag_profileEntry, "v_frag_profile_entry");
            m_fTrans.addToBackStack(null);
            m_fTrans.commit();
        }
    }

    @Override
    public void onDataPass(String fname, String lname, int age, Bitmap image) {

    }
}

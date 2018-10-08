package com.example.mcresswell.project01.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.mcresswell.project01.R;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        if(isWideDisplay()){
//            //present fragment to display
////            m_fTrans = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.fl_create_account_nd, new CreateAccountFragment(), "v_frag_dashboard");
//            fragmentTransaction.commit();
//        } else {
            //present fragment to display
            fragmentTransaction.replace(R.id.fl_create_account_nd, new CreateAccountFragment(), "v_frag_dashboard");
            fragmentTransaction.commit();
//        }
    }

    private boolean isWideDisplay(){
        return getResources().getBoolean(R.bool.isWideDisplay);
    }

}

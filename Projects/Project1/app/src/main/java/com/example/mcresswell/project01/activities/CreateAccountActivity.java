package com.example.mcresswell.project01.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.fragments.CreateAccountFragment;

import static com.example.mcresswell.project01.util.Constants.BACK_PRESSED;
import static com.example.mcresswell.project01.util.Constants.CREATE;

public class CreateAccountActivity extends AppCompatActivity {

    private static final String LOG_TAG = CreateAccountActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(LOG_TAG, CREATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(isWideDisplay()){
            fragmentTransaction.replace(R.id.fl_create_account_nd, new CreateAccountFragment(), "v_frag_dashboard");
            fragmentTransaction.commit();
        } else {
            fragmentTransaction.replace(R.id.fl_create_account_nd, new CreateAccountFragment(), "v_frag_dashboard");
            fragmentTransaction.commit();
        }
    }

    private boolean isWideDisplay(){
        return getResources().getBoolean(R.bool.isWideDisplay);
    }

    @Override
    public void onBackPressed() {
        Log.d(LOG_TAG, BACK_PRESSED);

        Intent intent = new Intent(this, LoginActivity.class);

        startActivity(intent);
    }

}

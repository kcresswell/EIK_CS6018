//package com.example.mcresswell.project01;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.View;
//
//public class HikingActivity extends AppCompatActivity
//        implements HikingFragment.OnHikingFragmentInteractionListener {
//
//    private static final String LOG = HikingActivity.class.getSimpleName();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        Log.d(LOG, "onCreate");
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_hiking);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        String location = getIntent().getStringExtra("location");
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        //Instantiate a new fragment instance if none exists, otherwise restore previously instantiated instance
//        HikingFragment fragment = savedInstanceState == null ?
//                HikingFragment.newInstance(location) :
//                (HikingFragment) fragmentManager.findFragmentById(R.id.cl_hikes);
//
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentTransaction.add(R.id.fragment_hiking, fragment);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
//
//    }
//
//    @Override
//    public void onHikingFragmentInteraction() {
//
//    }
//}

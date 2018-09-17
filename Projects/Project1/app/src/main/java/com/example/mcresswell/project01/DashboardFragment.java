package com.example.mcresswell.project01;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mAdaptor;
    private RecyclerView.LayoutManager layoutManager;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //get recycler view
        mRecycleView = (RecyclerView) view.findViewById(R.id.rcycV_List);

        //fix size of view
        mRecycleView.setHasFixedSize(true);

        //set layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(layoutManager);

        //needs to be implemented
//        ArrayList<ImageButton> btns_dashboard = getArguments().getParcelableArrayList("BUTTONS_DASHBOARD_DATA");

        //just a place holder until it is corrected
        ArrayList<ImageButton> btns_dashboard = null;

        mAdaptor = new RV_Adapter(btns_dashboard);
        mRecycleView.setAdapter(mAdaptor);

        return view;
    }

}

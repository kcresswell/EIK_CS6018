package com.example.mcresswell.project01.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mcresswell.project01.DashButton;
import com.example.mcresswell.project01.R;
import com.example.mcresswell.project01.RV_Adapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private RecyclerView m_RecycleView;
    private RecyclerView.Adapter m_Adaptor;
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
        m_RecycleView = (RecyclerView) view.findViewById(R.id.rcycV_List);

        //fix size of view
        m_RecycleView.setHasFixedSize(true);

        //set layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        m_RecycleView.setLayoutManager(layoutManager);

        //needs to be implemented
        ArrayList<DashButton> buttons = new ArrayList<>();

        //build image for button
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_fitness, null), "Fitness"));
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_hike, null), "Hikes"));
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_profile, null), "Profile"));
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_weather, null), "Weather"));

//        ImageButtonListParcelable imageButtonListParcelable = new ImageButtonListParcelable(buttons);
//        ArrayList<DashButton> btns_dashboard = imageButtonListParcelable.getM_buttons();

//        m_Adaptor = new RV_Adapter(btns_dashboard);
        m_Adaptor = new RV_Adapter(buttons);

        if(!getResources().getBoolean(R.bool.isWideDisplay)){
            m_RecycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }

        m_RecycleView.setAdapter(m_Adaptor);

        return view;
    }

    public void getButtonClickedPosition() {
        notify();
        m_RecycleView.getChildAdapterPosition(this.m_RecycleView);
    }

}

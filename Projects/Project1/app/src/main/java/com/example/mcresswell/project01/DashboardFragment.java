package com.example.mcresswell.project01;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;


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

//    public static DashboardFragment newInstance (ImageButtonListParcelable imageButtonListParcelable){
//        Bundle args = new Bundle();
//        args.putParcelable("BUTTONS", imageButtonListParcelable);
//
//        DashboardFragment fragment = new DashboardFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }


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
        ArrayList<DashButton> buttons = new ArrayList<>();

        //build image for button
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_fitness, null), "Fitness"));
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_hike, null), "Hikes"));
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_profile, null), "Profile"));
        buttons.add(new DashButton(getResources().getDrawable(R.drawable.ic_img_weather, null), "Weather"));

//        ImageButtonListParcelable imageButtonListParcelable = new ImageButtonListParcelable(buttons);
//        ArrayList<DashButton> btns_dashboard = imageButtonListParcelable.getM_buttons();

//        mAdaptor = new RV_Adapter(btns_dashboard);
        mAdaptor = new RV_Adapter(buttons);
        mRecycleView.setAdapter(mAdaptor);

        return view;
    }

}

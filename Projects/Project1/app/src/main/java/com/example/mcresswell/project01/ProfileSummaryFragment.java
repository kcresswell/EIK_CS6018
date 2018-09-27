package com.example.mcresswell.project01;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileSummaryFragment extends Fragment implements View.OnClickListener {

    //declare member variables
    private Button m_btn_edit;
    private OnProfileSummaryDataChannel m_dataListener;

    public ProfileSummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_summary, container, false);

        m_btn_edit = (Button) view.findViewById(R.id.btn_edit);
        m_btn_edit.setOnClickListener(this);

        //initialize the dataListener to context of activity as a concrete implementation of the interface
        m_dataListener = (OnProfileSummaryDataChannel) getActivity();
        return view;
    }

    //edit button on click fragment replace with edit fragment
    //id = btn_edit
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_edit: {
                Toast.makeText(getActivity(), "Edit Button Clicked", Toast.LENGTH_SHORT).show();
                //send specific data of "TRUE" value signifying the button was clicked
                m_dataListener.onProfileSummaryDataPass(true);

                break;
            }
        }
    }

    //pass data from fragment to activity via interface
    public interface OnProfileSummaryDataChannel {
        void onProfileSummaryDataPass(boolean isClicked);
    }
}

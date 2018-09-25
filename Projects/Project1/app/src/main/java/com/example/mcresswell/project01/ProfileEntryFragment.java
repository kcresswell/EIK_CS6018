package com.example.mcresswell.project01;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEntryFragment extends Fragment implements View.OnClickListener {

    private String m_fname, m_lname, m_dob, m_sex, m_city, m_country, str_lifestyle_selection, str_weight_goal_selection;
    private int m_age, m_weight, m_feet, m_inches, m_lbsPerWeek;
    private EditText m_etxt_fname, m_etxt_lname, m_etxt_dob, m_etxt_sex, m_etxt_city, m_etxt_country,
            m_etxt_weight, m_etxt_feet, m_etxt_inches, m_etxt_lbPerWeek;
    private Button m_btn_submit;
    private ImageButton m_btn_img_image;
    private Bitmap m_bmap_imageFromCam;
    private RadioGroup m_radiogp_lifestyleSelection, m_radiogp_weightGoal;

    private UserProfile m_userProfile;
    private Bundle m_userProfileBundle = new Bundle();

    //request code for camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private OnProfileEntryDataChannel m_dataListener;

    public ProfileEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_entry, container, false);

        //--get EditText fields, assign member variables appropriately--//
        m_etxt_fname = (EditText) view.findViewById(R.id.txtv_fname);
        m_fname = m_etxt_fname.getText().toString();

        m_etxt_lname = (EditText) view.findViewById(R.id.txtv_lname);
        m_lname = m_etxt_lname.getText().toString();

        m_etxt_dob = (EditText) view.findViewById(R.id.txtv_dob);
        m_dob = m_etxt_dob.getText().toString();

        m_etxt_sex = (EditText) view.findViewById(R.id.txtv_sex);
        m_sex = m_etxt_sex.getText().toString();

        m_etxt_city = (EditText) view.findViewById(R.id.txtv_city);
        m_city = m_etxt_city.getText().toString();

        m_etxt_country = (EditText) view.findViewById(R.id.txtv_country);
        m_country = m_etxt_country.getText().toString();

        m_etxt_weight = (EditText) view.findViewById(R.id.txtv_weight);
        m_weight = (int) Double.parseDouble(m_etxt_weight.getText().toString());

        m_etxt_feet = (EditText) view.findViewById(R.id.txtv_feet);
        m_feet = (int) Double.parseDouble(m_etxt_feet.getText().toString());

        m_etxt_inches = (EditText) view.findViewById(R.id.txtv_inches);
        m_inches = (int) Double.parseDouble(m_etxt_inches.getText().toString());

        m_etxt_lbPerWeek = (EditText) view.findViewById(R.id.txtv_weight2);
        m_lbsPerWeek = (int) Double.parseDouble(m_etxt_lbPerWeek.getText().toString());

        //--get Radio Button selection and assign it to a string value--//
        m_radiogp_lifestyleSelection = (RadioGroup) view.findViewById(R.id.radiogp_lifestyle);
        int btn_radio_lifestyle_id= m_radiogp_lifestyleSelection.getCheckedRadioButtonId();
        RadioButton btn_radio_lifestyle = (RadioButton) m_radiogp_lifestyleSelection.findViewById(btn_radio_lifestyle_id);
        str_lifestyle_selection = (String) btn_radio_lifestyle.getText();

        //--get Radio Group Weight Goal--//
        m_radiogp_weightGoal = (RadioGroup) view.findViewById(R.id.radiogp_weightGoal);
        int btn_radio_weightGoal_id = m_radiogp_weightGoal.getCheckedRadioButtonId();
        RadioButton btn_radio_weight_goal = (RadioButton) m_radiogp_weightGoal.findViewById(btn_radio_weightGoal_id);
        str_weight_goal_selection = (String) btn_radio_weight_goal.getText();

        //--get submit and image buttons--//
        m_btn_submit = (Button) view.findViewById(R.id.btn_submit);
        m_btn_img_image = (ImageButton) view.findViewById(R.id.btn_img_takeImage);

        //set buttons to listen to this class
        m_btn_submit.setOnClickListener(this);
        m_btn_img_image.setOnClickListener(this);

        m_userProfile = new UserProfile(m_fname, m_lname, m_dob, m_sex, m_city, m_country, str_lifestyle_selection,
                str_weight_goal_selection, m_weight, m_feet,m_inches, m_lbsPerWeek);

        Intent i = new Intent(getContext(),FitnessDetailsFragment.class);
        i.putExtras(m_userProfileBundle);
        startActivity(i);

        return view;
    }

    public interface OnProfileEntryDataChannel {
        void onProfileEntryDataPass (Bundle userDataBundle);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            m_dataListener = (OnProfileEntryDataChannel) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString() + " must implement OnProfileEntryDataChannel");
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_img_takeImage: {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(cameraIntent.resolveActivity(getActivity().getPackageManager())!=null){
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
                break;
            }
            case R.id.btn_submit: {
                m_etxt_fname = (EditText) getActivity().findViewById(R.id.txtv_fname);
                m_etxt_lname = (EditText) getActivity().findViewById(R.id.txtv_lname);
                m_etxt_dob = (EditText) getActivity().findViewById(R.id.txtv_dob);
                m_btn_img_image = (ImageButton) view.findViewById(R.id.btn_img_takeImage);

                m_fname = m_etxt_fname.getText().toString();
                m_lname = m_etxt_lname.getText().toString();
                m_dob = m_etxt_dob.getText().toString();

                if(m_fname.matches("") || m_lname.matches("") || m_dob.matches("")){
                    //ask for all fields to have data
                    Toast.makeText(getActivity(), "Enter first and last name and date of birth", Toast.LENGTH_SHORT).show();
                } else {
                    //replace leading and trailing spaces.
                    m_fname = m_fname.replaceAll("^\\s+","");
                    m_lname = m_lname.replaceAll("^\\s+","");
                    m_dob = m_dob.replaceAll("^\\s+","");

                    //extract dob and place on view
                    if(!m_dob.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}")) {
                        //inform user of expected dob input
                        Toast.makeText(getActivity(), "Use mm/dd/yyyy format", Toast.LENGTH_SHORT).show();
                    } else {
                        m_dataListener.onProfileEntryDataPass(m_userProfileBundle);
                    }

                    ArrayList<UserProfile> userProfiles = new ArrayList<UserProfile>();
                    userProfiles.add(m_userProfile);

                    UserProfileListParcelable UserProfilesParcelable = new UserProfileListParcelable(userProfiles);

                    //Put this into a bundle
                    m_userProfileBundle.putParcelable("item_list", UserProfilesParcelable);

                    //Create the fragment
                    FitnessDetailsFragment fitnessDetailsFragment = new FitnessDetailsFragment();

                    //Pass data to the fragment
                    fitnessDetailsFragment.setArguments(m_userProfileBundle);
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK){
            Bundle extras = data.getExtras();
            m_bmap_imageFromCam = (Bitmap) extras.get("data");

            m_btn_img_image = (ImageButton) getActivity().findViewById(R.id.btn_img_takeImage);
            m_btn_img_image.setImageBitmap(m_bmap_imageFromCam);
        }
    }

    @Override
    public void onViewStateRestored (Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //retrieve data
        if(savedInstanceState != null) {
            //place data in fields
            m_etxt_fname.setText("" + savedInstanceState.getString("M_FN_DATA"));
            m_etxt_lname.setText("" + savedInstanceState.getString("M_LN_DATA"));
            m_etxt_dob.setText("" + savedInstanceState.getString("M_DOB_DATA"));
            m_bmap_imageFromCam = savedInstanceState.getParcelable("M_IMG_DATA");
            m_btn_img_image.setImageBitmap(m_bmap_imageFromCam);
        }
    }
}

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

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileEntryFragment extends Fragment implements View.OnClickListener {

    private String m_fname, m_lname, m_dob, m_sex, m_city, m_country;
    private int m_age;
    private EditText m_etxt_fname, m_etxt_lname, m_etxt_dob, m_etxt_sex, m_etxt_city, m_etxt_country, m_etxt_weight, m_etxt_feet, m_etxt_inches, m_etxt_lbPerWeek;
    private Button m_btn_submit;
    private ImageButton m_btn_img_image;
    private Bitmap m_bmap_imageFromCam;
    private RadioGroup m_lifestyleSelection, m_radiogp_weightGoal;

    //request code for camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private OnDataChannel mListener;

    public ProfileEntryFragment() {
        // Required empty public constructor
    }

    public Bundle userData = new Bundle();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_entry, container, false);

        //get EditText fields
        m_etxt_fname = (EditText) view.findViewById(R.id.txtv_fname);
        m_etxt_lname = (EditText) view.findViewById(R.id.txtv_lname);
        m_etxt_dob = (EditText) view.findViewById(R.id.txtv_dob);
        m_etxt_sex = (EditText) view.findViewById(R.id.txtv_sex);
        m_etxt_city = (EditText) view.findViewById(R.id.txtv_city);
        m_etxt_country = (EditText) view.findViewById(R.id.txtv_country);
        m_etxt_weight = (EditText) view.findViewById(R.id.txtv_weight);
        m_etxt_feet = (EditText) view.findViewById(R.id.txtv_feet);
        m_etxt_inches = (EditText) view.findViewById(R.id.txtv_inches);
        m_etxt_lbPerWeek = (EditText) view.findViewById(R.id.txtv_weight2);

        //get Radio Button selection
        m_lifestyleSelection = (RadioGroup) view.findViewById(R.id.radiogp_lifestyle);

//        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        int radioButtonID = m_lifestyleSelection.getCheckedRadioButtonId();

        RadioButton radioButton = (RadioButton) m_lifestyleSelection.findViewById(radioButtonID);

        String selectedtext = (String) radioButton.getText();

        m_radiogp_weightGoal = (RadioGroup) view.findViewById(R.id.radiogp_weightGoal);

//        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);

        int radioButtonID2 = m_lifestyleSelection.getCheckedRadioButtonId();

        RadioButton radioButton2 = (RadioButton) m_lifestyleSelection.findViewById(radioButtonID2);

        String selectedtext2 = (String) radioButton2.getText();

        //get buttons
        m_btn_submit = (Button) view.findViewById(R.id.btn_submit);
        m_btn_img_image = (ImageButton) view.findViewById(R.id.btn_img_takeImage);

        //set buttons to listen to this class
        m_btn_submit.setOnClickListener(this);
        m_btn_img_image.setOnClickListener(this);

        int age = calculateAge();

        //add user data to the bundle
        userData.putString("fname", m_etxt_fname.getText().toString());
        userData.putString("lname", m_etxt_lname.getText().toString());
        userData.putString("dob", m_etxt_dob.getText().toString());
        userData.putString("sex", m_etxt_sex.getText().toString());
        userData.putString("city", m_etxt_city.getText().toString());
        userData.putString("country", m_etxt_country.getText().toString());
        userData.putString("weight", m_etxt_weight.getText().toString());
        userData.putString("feet", m_etxt_feet.getText().toString());
        userData.putString("inches", m_etxt_inches.getText().toString());
        userData.putInt("age", age);
        userData.putString("lbsPerWeek", m_etxt_lbPerWeek.getText().toString());
        userData.putString("lifestyle", selectedtext);
        userData.putString("weightGoal", selectedtext2);

        Intent i = new Intent(getContext(),FitnessDetailsFragment.class);
        i.putExtras(userData);
        startActivity(i);

        return view;
    }

    public interface OnDataChannel {
        void onDataPass(String fname, String lname, int age, Bitmap image, FitnessScore fitnessScore);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (OnDataChannel) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString() + " must implement OnDataChannel");
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
                        m_age = calculateAge();
                        mListener.onDataPass(m_fname, m_lname, m_age, m_bmap_imageFromCam,
                                new FitnessScore(m_fname, m_lname, m_dob, m_sex, m_city,m_country, m_etxt_weight.toString(),
                                        m_etxt_feet.toString(), m_etxt_inches.toString(), m_etxt_lbPerWeek.toString(),
                                        m_lifestyleSelection.toString(), m_radiogp_weightGoal.toString(), m_age));
                    }

                    ArrayList<FitnessScore> fitnessScores = new ArrayList<FitnessScore>();
                    fitnessScores.add(new FitnessScore(m_fname, m_lname, m_dob, m_sex, m_city,m_country, m_etxt_weight.toString(), m_etxt_feet.toString(), m_etxt_inches.toString(), m_etxt_lbPerWeek.toString(), m_lifestyleSelection.toString(), m_radiogp_weightGoal.toString(), m_age));

                    FitnessScoreListParcelable fitnessScoresParcelable = new FitnessScoreListParcelable(fitnessScores);

                    //Put this into a bundle
                    Bundle fragmentBundle = new Bundle();
                    fragmentBundle.putParcelable("item_list", fitnessScoresParcelable);

                    //Create the fragment
                    FitnessDetailsFragment fitnessDetailsFragment = new FitnessDetailsFragment();

                    //Pass data to the fragment
                    fitnessDetailsFragment.setArguments(fragmentBundle);
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

    //helper functions
    private int calculateAge() {
        DateTimeFormatter dob_format = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        dob_format = dob_format.withLocale(Locale.US);

        LocalDate dob = LocalDate.parse(m_dob, dob_format);
        LocalDate today = LocalDate.now();

        return Period.between(dob, today).getYears();
    }

}

package com.example.mcresswell.lab03_try2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

//Implement View.onClickListener to listen to button clicks. This means we have to override onClick().
public class MainActivity extends AppCompatActivity
        implements View.OnClickListener{

    //Create variables to hold the three strings
    private String mFullName, mFirstName, mLastName;

    //Create variables for the UI elements that we need to control
    private TextView mTvFirstName, mTvLastName;
    private Button mButtonSubmit, mButtonCamera;
    private EditText mEtFullName;

    //Define a bitmap
    Bitmap mThumbnailImage;

    //Define a global intent variable
    Intent mDisplayIntent;

    //Define a request code for the camera
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Get the text views where we will display names
        mTvFirstName = (TextView) findViewById(R.id.tv_fn_data);
        mTvLastName = (TextView) findViewById(R.id.tv_ln_data);

        //Get the buttons
        mButtonSubmit = (Button) findViewById(R.id.button_submit);
        mButtonCamera = (Button) findViewById(R.id.button_pic);

        //Say that this class itself contains the listener.
        mButtonSubmit.setOnClickListener(this);
        mButtonCamera.setOnClickListener(this);

        //Create the intent but don't start the activity yet
        mDisplayIntent = new Intent(this,DisplayActivity.class);
    }

    //Handle clicks for ALL buttons here
    @Override
    public void onClick(View view) {
        switch(view.getId()) {

            case R.id.button_submit: {
                //First, get the string from the EditText
                mEtFullName = (EditText) findViewById(R.id.et_name);
                mFullName = mEtFullName.getText().toString();

                //Check if the EditText string is empty
                if (mFullName.matches("")) {
                    //Complain that there's no text
                    Toast.makeText(MainActivity.this, "Enter a name first!", Toast.LENGTH_SHORT).show();
                } else {
                    //Reward them for submitting their names
                    Toast.makeText(MainActivity.this, "Good job!", Toast.LENGTH_SHORT).show();

                    //Remove any leading spaces or tabs
                    mFullName = mFullName.replaceAll("^\\s+","");

                    //Separate the string into first and last name using simple Java stuff
                    String[] splitStrings = mFullName.split("\\s+");

                    if(splitStrings.length==1){
                        Toast.makeText(MainActivity.this, "Enter both first and last name!", Toast.LENGTH_SHORT).show();
                    }
                    else if(splitStrings.length==2) {
                        mFirstName = splitStrings[0];
                        mLastName = splitStrings[1];

                        //Start a new activity and pass the strings to them
                        mDisplayIntent.putExtra("FN_DATA",mFirstName);
                        mDisplayIntent.putExtra("LN_DATA",mLastName);
                        startActivity(mDisplayIntent);

                    }
                    else{
                        Toast.makeText(MainActivity.this, "Enter only first and last name!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            }
            case R.id.button_pic: {
                //The button press should open a camera
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(cameraIntent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            //Get the bitmap
            Bundle extras = data.getExtras();
            mThumbnailImage = (Bitmap) extras.get("data");

            //Open a file and write to it
            if(isExternalStorageWritable()){
                String filePathString = saveImage(mThumbnailImage);
                mDisplayIntent.putExtra("imagePath",filePathString);
            }
            else{
                Toast.makeText(this,"External storage not writable.",Toast.LENGTH_SHORT).show();
            }

        }
    }


    private String saveImage(Bitmap finalBitmap) {

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fname = "Thumbnail_"+ timeStamp +".jpg";

        File file = new File(myDir, fname);
        if (file.exists()) file.delete ();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(this,"file saved!",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file.getAbsolutePath();
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}


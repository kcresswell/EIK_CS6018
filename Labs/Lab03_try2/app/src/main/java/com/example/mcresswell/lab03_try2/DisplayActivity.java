package com.example.mcresswell.lab03_try2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class DisplayActivity extends AppCompatActivity {

    TextView mTvFirstName,mTvLastName;
    ImageView mIvThumbnail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        //Get the text views
        mTvFirstName = (TextView) findViewById(R.id.tv_fn_data);
        mTvLastName = (TextView) findViewById(R.id.tv_ln_data);

        //Get the image view
        mIvThumbnail = (ImageView) findViewById(R.id.iv_pic);

        //Get the starter intent
        Intent receivedIntent = getIntent();

        //Set the text views
        mTvFirstName.setText(""+receivedIntent.getStringExtra("FN_DATA"));
        mTvLastName.setText(""+receivedIntent.getStringExtra("LN_DATA"));


        //Set the image view
        String imagePath = receivedIntent.getStringExtra("imagePath");
        Bitmap thumbnailImage = BitmapFactory.decodeFile(imagePath);
        if(thumbnailImage!=null){
            mIvThumbnail.setImageBitmap(thumbnailImage);
        }
    }
}


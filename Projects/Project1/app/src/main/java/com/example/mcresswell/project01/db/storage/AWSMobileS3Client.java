package com.example.mcresswell.project01.db.storage;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.AWSStartupHandler;
import com.amazonaws.mobile.client.AWSStartupResult;
//import com.amazonaws.mobile.auth.ui.SignInUI;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;


import java.io.File;
import java.sql.Date;
import java.time.Instant;
import java.util.Locale;

import javax.inject.Named;
import javax.inject.Singleton;


@Named
@Singleton
public class AWSMobileS3Client {

    private static final String LOG_TAG = AWSMobileClient.class.getSimpleName();
    private static final String KEY = "";
    private static final String SECRET = "";
    private static final String KEY_PATH = "";

    private static AmazonS3Client s3Client;


    public static void initializeAwsClient(Context context) {
        AWSMobileClient.getInstance().initialize(context, new AWSStartupHandler() {
            @Override
            public void onComplete(AWSStartupResult awsStartupResult) {
                Log.d(LOG_TAG, "AWSMobileClient successfully initialized and connected to AWS.");

//                SignInUI signin =
//                        (SignInUI) AWSMobileClient.getInstance().getClient(context, SignInUI.class);
//
//                signin.login(
//                        MainActivity.this,
//                        NextActivity.class).execute();
//            }
//        });

                BasicAWSCredentials credentials = new BasicAWSCredentials(KEY, SECRET);
                s3Client = new AmazonS3Client(credentials);
            }
        });


    }

    private void uploadFileToS3(String filePath, Context context) {

        TransferUtility transferUtility = buildTransferUtility(context);


        TransferObserver uploadObserver =
                transferUtility.upload( KEY_PATH, new File(filePath));

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    Log.d(LOG_TAG, "onStateChanged() completed transfer");
                    //TODO: Add logic in here
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                //TODO: Add logic in here

                float percentDonef = ((float)bytesCurrent/(float)bytesTotal) * 100;
                int percentDone = (int)percentDonef;
            }

            @Override
            public void onError(int id, Exception ex) {
                //TODO: Add logic in here
            }

        });

        if (TransferState.COMPLETED == uploadObserver.getState()) {
            Log.d(LOG_TAG, String.format(

                    Locale.US,
                    "TransferUtility Upload Completed Successfully at %s",
                    Date.from(Instant.now()).toString()));
        }
    }

    private static TransferUtility buildTransferUtility(Context context) {
        return TransferUtility.builder()
                .context(context.getApplicationContext())
                .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                .s3Client(s3Client)
                .build();
    }

    private void downloadFileFromS3(String destFilePath, Context context) {
        //TODO: Add logic here
    }
}
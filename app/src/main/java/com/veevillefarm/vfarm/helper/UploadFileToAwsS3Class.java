package com.veevillefarm.vfarm.helper;

import android.content.Context;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;

import java.io.File;

/**
 * Created by Prashant C on 15/01/19.
 */
public class UploadFileToAwsS3Class {

    private Context context;
    private FileUploadStatus status;
    private final String KEY = "AKIAIY45HAS2S2I45YIA",SECRET = "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";
    public UploadFileToAwsS3Class(Context context,FileUploadStatus status){
        this.context = context;
        this.status = status;
    }

    public void uploadImageFile(String fileKey, File file){
        BasicAWSCredentials credentials = new BasicAWSCredentials(KEY, SECRET);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(context)
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();

// "jsaS3" will be the folder that contains the file
        TransferObserver uploadObserver = transferUtility.upload(fileKey,file);

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                status.fileuploafStatus(TransferState.COMPLETED == state);
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float)bytesCurrent/(float)bytesTotal) * 100;
                int percentDone = (int)percentDonef;
                status.fileUploadProgressStatus(percentDone);
            }

            @Override
            public void onError(int id, Exception ex) {
                status.fileuploafStatus(false);
                // Handle errors
            }

        });

// If your upload does not trigger the onStateChanged method inside your
// TransferListener, you can directly check the transfer state as shown here.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }
        if(TransferState.CANCELED == uploadObserver.getState()){
            status.isUploadCancleled(true);
        }

    }

    public interface FileUploadStatus{
        void fileuploafStatus(boolean isUploaded);
        void fileUploadProgressStatus(int progress);
        void isUploadCancleled(boolean trueOfFalse);

    }
}

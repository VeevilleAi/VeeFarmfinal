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
public class DownloadFileFromAwsS3 {

    private final String KEY = "AKIAIY45HAS2S2I45YIA",SECRET = "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";
    private Context context;
    private String fileName;
    private final String TAG = DownloadFileFromAwsS3.class.getSimpleName();
    private long imgId;
    private UpdatesFowDownload updatesFowDownload;
    public DownloadFileFromAwsS3(Context context,String fileName,long imgId,UpdatesFowDownload updatesFowDownload){
        this.context = context;
        this.imgId = imgId;
        this.updatesFowDownload = updatesFowDownload;
        this.fileName = fileName;

    }
    public void downLoadFile(String fileKey, File file) {
        BasicAWSCredentials credentials = new BasicAWSCredentials(KEY, SECRET);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);
        logMessage("aws id:"+fileKey);
        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(context)
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();

        TransferObserver downloadObserver = transferUtility.download(fileKey+".jpg",file);

        downloadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                    logMessage("download success");
                    storeDownloadedImageFilePathToDb();
                    updatesFowDownload.isDownloadSuccess(true);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;
                logMessage("progress:"+percentDone);
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
                logErrorMessage("error while downloading:"+id);
            }

        });
    }

    private void storeDownloadedImageFilePathToDb(){
        ChatMessageDatabase database = new ChatMessageDatabase(context);
        database.updateNewFileDownloedpath(imgId,fileName);
    }
    private void logMessage(String message){
        AppSingletonClass.logMessage(TAG,message);
    }
    private void logErrorMessage(String message){
        AppSingletonClass.logErrorMessage(TAG,message);
    }
    public interface UpdatesFowDownload{
        void isDownloadSuccess(boolean isDownloaded);
    }
}

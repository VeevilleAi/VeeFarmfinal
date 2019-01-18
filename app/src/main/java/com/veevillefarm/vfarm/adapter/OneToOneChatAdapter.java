package com.veevillefarm.vfarm.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.veevillefarm.vfarm.R;
import com.veevillefarm.vfarm.helper.AppSingletonClass;
import com.veevillefarm.vfarm.helper.ChatmessageDataClasses;
import com.veevillefarm.vfarm.helper.DownloadFileFromAwsS3;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Prashant C on 10/12/18.
 * this adapter maintain one to one chat
 */
public class OneToOneChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DownloadFileFromAwsS3.UpdatesFowDownload {


    private List<Object> messages;
    private final String TAG = OneToOneChatAdapter.class.getSimpleName();
    private Context context;
    private UpdateDownloadedImageInRecyclerview download;
    DownloadFileFromAwsS3.UpdatesFowDownload updatesFowDownload;
    public OneToOneChatAdapter(Context context,List<Object> messages,UpdateDownloadedImageInRecyclerview download){
        this.messages = messages;
        this.context = context;
        this.download = download;
    }
    @Override
    public int getItemViewType(int position) {
        if(messages.get(position) instanceof ChatmessageDataClasses.InputTextMessage){
            return 0;
        }else if(messages.get(position) instanceof ChatmessageDataClasses.ResponseTextMessage){
            return 1;
        }else if(messages.get(position) instanceof ChatmessageDataClasses.DateInMessage) {
            return 2;
        }else if(messages.get(position) instanceof ChatmessageDataClasses.InputBitMapImage) {
            return 3;
        }else if(messages.get(position) instanceof ChatmessageDataClasses.ResponseBitMapImage) {
            return 4;
        }else {
            return -1;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case 0:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simpletextcardinput, parent, false);
                viewHolder = new InputTextMessageHolder(view);
                break;
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simpletextresponse, parent, false);
                viewHolder = new ResponseTextMessageHolder(view);
                break;
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dateview_card, parent, false);
                viewHolder = new DateInMessageHolder(view);
                break;
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inputimagecard, parent, false);
                viewHolder = new InputImageHolder(view);
                break;
            case 4:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.response_images_card, parent, false);
                viewHolder = new ResponseImageHolder(view);
                break;

            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simpletextresponse, parent, false);
                viewHolder = new ResponseTextMessageHolder(view);
                break;

        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){
            case 0:
                handleInputTextMessage((InputTextMessageHolder) holder,position);
                break;
            case 1:
                handleOverviewOutPutData((ResponseTextMessageHolder) holder,position);
                break;
            case 2:
                handleDateInMessage((DateInMessageHolder) holder,position);
                break;
            case 3:
                handleInputImage((InputImageHolder) holder,position);
                break;
            case 4:
                handleResponseImage((ResponseImageHolder) holder,position);
                break;
        }
    }

    private void handleResponseImage(ResponseImageHolder holder,int position){
        final OneToOneChatAdapter  temp = this;
        final ChatmessageDataClasses.ResponseBitMapImage bitMapImage = (ChatmessageDataClasses.ResponseBitMapImage) messages.get(position);
        if(bitMapImage.isDownloaded){
            holder.download_image.setVisibility(View.GONE);
            File imgFile = new  File(AppSingletonClass.folderPath+File.separator+bitMapImage.filePath);
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.input_imageview.setImageBitmap(myBitmap);

            }
        }else {
            holder.download_image.setVisibility(View.VISIBLE);
            holder.input_imageview.setImageBitmap(bitMapImage.bitmap);
            holder.download_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "image start to download", Toast.LENGTH_SHORT).show();

                    String fileName = System.currentTimeMillis()+".JPG";
                    File file = new File(AppSingletonClass.folderPath,fileName);
                    try {
                        boolean isFileCreated = file.createNewFile();
                        logMessage("isFileCreated:"+isFileCreated);
                        DownloadFileFromAwsS3 fileFromAwsS3 = new DownloadFileFromAwsS3(context,fileName,bitMapImage.imgId,temp);
                        logMessage("awsId:"+bitMapImage.awsImageId);
                        fileFromAwsS3.downLoadFile(bitMapImage.awsImageId,file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    }

    private void handleInputImage(InputImageHolder holder,int position){
        ChatmessageDataClasses.InputBitMapImage bitMapImage = (ChatmessageDataClasses.InputBitMapImage) messages.get(position);
        File imgFile = new  File(AppSingletonClass.folderPath+File.separator+bitMapImage.filePath);
        logMessage("image file path:"+imgFile.getPath());
        if(imgFile.exists()){
            logMessage("file exists");
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            int byteCount = myBitmap.getByteCount();
            logMessage("byte count:"+byteCount);
            holder.input_imageview.setImageBitmap(myBitmap);

        }else {
            logMessage("file does not exists");
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private void handleDateInMessage(DateInMessageHolder holder,int position){

        ChatmessageDataClasses.DateInMessage date = (ChatmessageDataClasses.DateInMessage) messages.get(position);
        holder.textView.setText(date.date);

    }

    private void handleInputTextMessage(final InputTextMessageHolder myholderInput, int position) {

        ChatmessageDataClasses.InputTextMessage inputData = (ChatmessageDataClasses.InputTextMessage) messages.get(position);
        myholderInput.singlemesssage.setText(inputData.inputTextMessage);
    }
    private void handleOverviewOutPutData(ResponseTextMessageHolder myholderOutput, int position) {

        ChatmessageDataClasses.ResponseTextMessage responseData = (ChatmessageDataClasses.ResponseTextMessage) messages.get(position);
        myholderOutput.singlemesssage.setText(responseData.responseTextMessage);
    }

    @Override
    public void isDownloadSuccess(boolean isDownloaded) {
        logMessage("download status:"+isDownloaded);
        if(isDownloaded){
            //dummy img id
            download.updateRecyclerview(12345);
        }
    }

    class InputTextMessageHolder extends RecyclerView.ViewHolder {

        private TextView singlemesssage;

        InputTextMessageHolder(View view) {
            super(view);
            singlemesssage = view.findViewById(R.id.singletextmessage);
        }
    }

    class ResponseTextMessageHolder extends RecyclerView.ViewHolder {

        private TextView singlemesssage;

        ResponseTextMessageHolder(View view) {
            super(view);
            singlemesssage = view.findViewById(R.id.singletextmessage);
        }
    }


    class DateInMessageHolder extends RecyclerView.ViewHolder {
        TextView textView;

        DateInMessageHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.date);
        }
    }
    class InputImageHolder extends RecyclerView.ViewHolder {
        ImageView input_imageview;
        ProgressBar imageUploadProgressbar;
        TextView time;

        InputImageHolder(View view) {
            super(view);
            input_imageview = view.findViewById(R.id.imageinput);
            imageUploadProgressbar = view.findViewById(R.id.image_upload_progressbar);
            time = view.findViewById(R.id.time);
        }
    }
    class ResponseImageHolder extends RecyclerView.ViewHolder {
        ImageView input_imageview,download_image;
        ProgressBar imageUploadProgressbar;
        TextView time;
        ResponseImageHolder(View view) {
            super(view);
            input_imageview = view.findViewById(R.id.imageinput);
            imageUploadProgressbar = view.findViewById(R.id.image_upload_progressbar);
            time = view.findViewById(R.id.time);
            download_image = view.findViewById(R.id.download_image);
        }
    }
    private void logMessage(String logMessage) {
        AppSingletonClass.logMessage(TAG, logMessage);
    }

    public interface UpdateDownloadedImageInRecyclerview{
        void updateRecyclerview(long imgId);
    }
}

package com.veeville.farm.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.veeville.farm.R;
import com.veeville.farm.helper.ChatmessageDataClasses;

import java.util.List;

/**
 * Created by Prashant C on 10/12/18.
 */
public class OneToOneChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private List<Object> messages;
    public OneToOneChatAdapter(List<Object> messages){
        this.messages = messages;
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

        }
    }

    private void handleInputImage(InputImageHolder holder,int position){
        ChatmessageDataClasses.InputBitMapImage bitMapImage = (ChatmessageDataClasses.InputBitMapImage) messages.get(position);
        holder.input_imageview.setImageBitmap(bitMapImage.bitmap);
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
}

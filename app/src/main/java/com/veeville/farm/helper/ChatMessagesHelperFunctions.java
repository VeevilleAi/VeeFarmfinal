package com.veeville.farm.helper;

import android.content.Context;

/**
 * Created by Prashant C on 25/09/18.
 * these functions used in ChatDatabse operation functions
 */
public class ChatMessagesHelperFunctions {

    private Context context;

    public ChatMessagesHelperFunctions(Context context) {
        this.context = context;
    }

    public void insertInputTextMessage(String message) {

        ChatBotDatabase database = new ChatBotDatabase(context);
        database.insertChats("inputtext", message);
    }

    public void insertResponseTextMessage(String message) {

        ChatBotDatabase database = new ChatBotDatabase(context);
        database.insertChats("responsetext", message);

    }

    public void insertInputImage(String imageData) {
        ChatBotDatabase database = new ChatBotDatabase(context);
        database.insertChats("inputimage", imageData);

    }

    public void insertinputImageUrl(String imgUrl) {

        ChatBotDatabase database = new ChatBotDatabase(context);
        database.insertChats("inputimagelink", imgUrl);

    }

    public void insertResponseYoutubeVideoIds(String videoIds) {

        ChatBotDatabase database = new ChatBotDatabase(context);
        database.insertChats("videolink", videoIds);

    }

    public void insertPriceData(String message) {

        ChatBotDatabase database = new ChatBotDatabase(context);
        database.insertChats("vegfruitpricecard", message);
    }
}

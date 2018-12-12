package com.veeville.farm.helper;

/**
 * Created by Prashant C on 10/12/18.
 */
public class ChatMessage {
    public String messageId,from,to,message;
    public long timestamp;
    public ChatMessage(String messageId,String from,String to,String message,long timestamp){
        this.messageId = messageId;
        this.from = from;
        this.to = to;
        this.message = message;
        this.timestamp = timestamp;
    }
}

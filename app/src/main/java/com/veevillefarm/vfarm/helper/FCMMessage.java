package com.veevillefarm.vfarm.helper;

/**
 * Created by Prashant C on 10/01/19.
 */
public class FCMMessage {
    public String title, body, fcmToken, from, to, messageType,fcmTokenOfOther;
    public long imageId;
    public FCMMessage(String title, String body,String fcmToken,String from,String to,String messageType,String fcmTokenOfOther,long imageId){
        this.title = title;
        this.body = body;
        this.fcmToken = fcmToken;
        this.from = from;
        this.to = to;
        this.fcmTokenOfOther = fcmTokenOfOther;
        this.messageType = messageType;
        this.imageId = imageId;
    }

}

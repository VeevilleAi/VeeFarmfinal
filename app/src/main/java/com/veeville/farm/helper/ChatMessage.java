package com.veeville.farm.helper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Prashant C on 10/12/18.
 *
 * ChatMessage in OneToOneChatMessage help and implements Parcelable helps to tranfere object over the network
 */
public class ChatMessage implements Parcelable {
    public String from,to,messageType;
    public long timestamp,messageValueId,messageId;
    public String messageValueOrUri;
    public ChatMessage(long messageId,String messageType,String from,String to,long messageStorageid,long timestamp,String messageValueOrUri){
        this.messageId = messageId;
        this.messageValueOrUri = messageValueOrUri;
        this.messageType = messageType;
        this.from = from;
        this.to = to;
        this.messageValueId = messageStorageid;
        this.timestamp = timestamp;
    }

    private ChatMessage(Parcel in) {
        from = in.readString();
        to = in.readString();
        messageType = in.readString();
        timestamp = in.readLong();
        messageValueId = in.readLong();
        messageId = in.readLong();
        messageValueOrUri = in.readString();
    }

    public static final Creator<ChatMessage> CREATOR = new Creator<ChatMessage>() {
        @Override
        public ChatMessage createFromParcel(Parcel in) {
            return new ChatMessage(in);
        }

        @Override
        public ChatMessage[] newArray(int size) {
            return new ChatMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(from);
        parcel.writeString(to);
        parcel.writeString(messageType);
        parcel.writeLong(timestamp);
        parcel.writeLong(messageValueId);
        parcel.writeLong(messageId);
        parcel.writeString(messageValueOrUri);
    }
}

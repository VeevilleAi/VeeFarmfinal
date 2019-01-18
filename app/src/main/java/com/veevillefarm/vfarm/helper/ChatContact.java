package com.veevillefarm.vfarm.helper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Prashant C on 11/12/18.
 * helper class for FriendsFragment
 */
public class ChatContact implements Parcelable{
    public String name,email,picUrl,recentMessage,fcmToken;
    public ChatContact(String name,String email,String picUrl,String recentMessage,String fcmToken){
        this.name = name;
        this.email = email;
        this.picUrl = picUrl;
        this.recentMessage = recentMessage;
        this.fcmToken = fcmToken;
    }

    protected ChatContact(Parcel in) {
        name = in.readString();
        email = in.readString();
        picUrl = in.readString();
        recentMessage = in.readString();
        fcmToken = in.readString();
    }

    public static final Creator<ChatContact> CREATOR = new Creator<ChatContact>() {
        @Override
        public ChatContact createFromParcel(Parcel in) {
            return new ChatContact(in);
        }

        @Override
        public ChatContact[] newArray(int size) {
            return new ChatContact[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(email);
        parcel.writeString(picUrl);
        parcel.writeString(recentMessage);
        parcel.writeString(fcmToken);
    }
    public static final Parcelable.Creator<ChatContact> CREATORTemp = new Parcelable.Creator<ChatContact>()
    {
        public ChatContact createFromParcel(Parcel in)
        {
            return new ChatContact(in);
        }
        public ChatContact[] newArray(int size)
        {
            return new ChatContact[size];
        }
    };
}

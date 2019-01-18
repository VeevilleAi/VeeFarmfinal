package com.veevillefarm.vfarm.helper;

/**
 * Created by Prashant C on 07/12/18.
 */
public class FarmerContact {
    public String name, number, profilePic,recentMessage;

    public FarmerContact(String name, String number, String profilePic,String recentMessage) {
        this.name = name;
        this.recentMessage = recentMessage;
        this.number = number;
        this.profilePic = profilePic;

    }
}

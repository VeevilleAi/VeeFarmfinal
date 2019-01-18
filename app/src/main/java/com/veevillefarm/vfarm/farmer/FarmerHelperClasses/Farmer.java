package com.veevillefarm.vfarm.farmer.FarmerHelperClasses;

/**
 * Created by Prashant C on 11/10/18.
 */
public class Farmer {

    public String name, address, mobileNumber, emailId, profilePicUrl;

    public Farmer(String name, String address, String mobileNumber, String emailId, String profilePicUrl) {
        this.address = address;
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.emailId = emailId;
        this.profilePicUrl = profilePicUrl;
    }
}

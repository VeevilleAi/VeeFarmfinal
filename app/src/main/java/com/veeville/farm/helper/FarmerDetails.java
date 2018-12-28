package com.veeville.farm.helper;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Prashant C on 20/12/18.
 * this helper class used to store Farmer details like name mob number etc......
 */
public class FarmerDetails implements Parcelable{

    public String firstName,lastName,email,mobileNumber,address;
    public FarmerDetails(String firstName,String lastName,String email,String mobileNumber,String address){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.address = address;
    }

    protected FarmerDetails(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        mobileNumber = in.readString();
        address = in.readString();
    }

    public static final Creator<FarmerDetails> CREATOR = new Creator<FarmerDetails>() {
        @Override
        public FarmerDetails createFromParcel(Parcel in) {
            return new FarmerDetails(in);
        }

        @Override
        public FarmerDetails[] newArray(int size) {
            return new FarmerDetails[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(email);
        parcel.writeString(mobileNumber);
        parcel.writeString(address);
    }
}

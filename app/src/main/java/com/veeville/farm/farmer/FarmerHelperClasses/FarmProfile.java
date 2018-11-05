package com.veeville.farm.farmer.FarmerHelperClasses;

/**
 * Created by Prashant C on 18/10/18.
 */
public class FarmProfile {
    public String farmName, farmPicture, farmArea, farmCrop, cropStatus, yield;

    public FarmProfile(String farmName, String farmPicture, String farmArea, String farmCrop, String cropStatus, String yield) {
        this.farmName = farmName;
        this.farmPicture = farmPicture;
        this.farmArea = farmArea;
        this.farmCrop = farmCrop;
        this.cropStatus = cropStatus;
        this.yield = yield;
    }

}

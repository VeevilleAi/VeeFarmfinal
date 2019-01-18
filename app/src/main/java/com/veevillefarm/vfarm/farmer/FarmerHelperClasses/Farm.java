package com.veevillefarm.vfarm.farmer.FarmerHelperClasses;

/**
 * Created by Prashant C on 11/10/18.
 */
public class Farm {
    public String farmName, cropName, cropImageLink, farmStatus;

    public Farm(String farmName, String cropName, String cropImageLink, String farmStatus) {
        this.farmName = farmName;
        this.cropName = cropName;
        this.cropImageLink = cropImageLink;
        this.farmStatus = farmStatus;
    }
}

package com.veevillefarm.vfarm.farmer.FarmerHelperClasses;

/**
 * Created by Prashant C on 23/10/18.
 */
public class MarketPlace {
    public String imgicon, cropName, currentPrice, farmerPrice, city, cropYield, totalMoney, description;

    public MarketPlace(String imgicon, String cropName, String currentPrice, String farmerPrice, String city, String cropYield, String totalMoney, String description) {
        this.imgicon = imgicon;
        this.cropName = cropName;
        this.currentPrice = currentPrice;
        this.cropYield = cropYield;
        this.city = city;
        this.totalMoney = totalMoney;
        this.description = description;
        this.farmerPrice = farmerPrice;
    }
}

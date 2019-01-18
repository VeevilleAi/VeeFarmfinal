package com.veevillefarm.vfarm.farmer.FarmerHelperClasses;

import java.util.List;

/**
 * Created by Prashant C on 25/10/18.
 */
public class CropMarketplace {
    public static class CropMarketPlaceCropDesc {
        String cropName, cropQuantity, harvestDate;

        public CropMarketPlaceCropDesc(String cropName, String cropQuantity, String harvestDate) {
            this.cropName = cropName;
            this.cropQuantity = cropQuantity;
            this.harvestDate = harvestDate;
        }

    }

    public static class CropMarketPlaceDescImage {
        public String imageLink;

        public CropMarketPlaceDescImage(String imageLink) {
            this.imageLink = imageLink;
        }
    }

    public static class CropMarketPlacePrice {
        public List<SingleMarketPlacePrices> list;

        public CropMarketPlacePrice(List<SingleMarketPlacePrices> list) {
            this.list = list;
        }

        public static class SingleMarketPlacePrices {
            public String place, distance, price;

            public SingleMarketPlacePrices(String place, String distance, String price) {
                this.place = place;
                this.distance = distance;
                this.price = price;
            }
        }
    }

}

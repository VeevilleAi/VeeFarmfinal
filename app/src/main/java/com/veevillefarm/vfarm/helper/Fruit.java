package com.veevillefarm.vfarm.helper;

/**
 * Created by Prashant C on 03/09/18.
 * this helper class used to store Fruit details lik price, name,image link etc.....
 */
public class Fruit {
    public String name, price, pieceOrKg, imageLink;
    public boolean isFruit;
    public long updatedTime;

    public Fruit(String name, String price, String pieceOrKg, String imageLink, boolean isFruit, long updatedTime) {
        this.name = name;
        this.imageLink = imageLink;
        this.isFruit = isFruit;
        this.price = price;
        this.pieceOrKg = pieceOrKg;
        this.updatedTime = updatedTime;
    }

    public String toString() {
        return this.name + ":" + this.price + ":" + this.pieceOrKg;
    }
}

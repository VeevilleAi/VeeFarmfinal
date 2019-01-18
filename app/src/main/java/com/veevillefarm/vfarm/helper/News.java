package com.veevillefarm.vfarm.helper;

/**
 * Created by Prashant C on 07/12/18.
 * this helper class used  news Activity
 */
public class News {
    public String shortDesc, imageUrl, newsUrl;

    public News(String shortDesc, String imageUrl, String newsUrl) {
        this.imageUrl = imageUrl;
        this.newsUrl = newsUrl;
        this.shortDesc = shortDesc;
    }
}

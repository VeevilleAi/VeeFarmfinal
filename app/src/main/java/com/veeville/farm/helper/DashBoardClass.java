package com.veeville.farm.helper;

/**
 * Created by Prashant C on 25/10/18.
 * this class contain App DashBoard data which are used in DashBoardActivty
 */
public class DashBoardClass {
    public String title, subTitle, cardType;
    public int icon;

    public DashBoardClass(String cardType, String title, String subTitle, int icon) {
        this.title = title;
        this.subTitle = subTitle;
        this.icon = icon;
        this.cardType = cardType;
    }
}

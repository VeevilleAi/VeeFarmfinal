package com.veevillefarm.vfarm.helper;



/**
 * Created by Prashant C on 25/10/18.
 * this class contain App DashBoard data which are used in DashBoardActivty
 */
public class DashBoardClass {
    public String title, subTitle, cardType;
    public int icon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public DashBoardClass(String cardType, String title, String subTitle, int icon) {
        this.title = title;
        this.subTitle = subTitle;
        this.icon = icon;
        this.cardType = cardType;
    }

}

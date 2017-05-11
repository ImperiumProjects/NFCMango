package com.imperium.power.nfcmango;

public class NFCMListItem {

    private String itemTitle;

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public NFCMListItem(String title){
        this.itemTitle = title;
    }
}
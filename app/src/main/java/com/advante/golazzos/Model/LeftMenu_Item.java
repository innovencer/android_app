package com.advante.golazzos.Model;

/**
 * Created by Ruben Flores on 1/23/2016.
 */
public class LeftMenu_Item {
    int icon;
    String text_item;

    public LeftMenu_Item() {
    }

    public LeftMenu_Item(int icon, String text_item) {
        this.icon = icon;
        this.text_item = text_item;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getText_item() {
        return text_item;
    }

    public void setText_item(String text_item) {
        this.text_item = text_item;
    }
}

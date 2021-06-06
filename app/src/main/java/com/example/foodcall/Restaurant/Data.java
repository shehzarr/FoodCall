package com.example.foodcall.Restaurant;

import android.graphics.Bitmap;

public class Data {
    private String image_Recycle;
    private String name;
    private String price;
    private String vendor_UID;

    public Data() {

    }

    public Data(String image_Recycle, String name, String price) {
        this.image_Recycle = image_Recycle;
        this.name = name;
        this.price = price;
    }

    public String getVendor_UID() {
        return vendor_UID;
    }

    public void setVendor_UID(String vendor_UID) {
        this.vendor_UID = vendor_UID;
    }

    public String getImage_Recycle() {
        return image_Recycle;
    }

    public void setImage_Recycle(String image_Recycle) {
        this.image_Recycle = image_Recycle;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

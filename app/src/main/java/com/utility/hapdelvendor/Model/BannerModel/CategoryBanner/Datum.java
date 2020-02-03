
package com.utility.hapdelvendor.Model.BannerModel.CategoryBanner;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.utility.hapdelvendor.Model.ProducModel.Product;

import java.util.ArrayList;

public class Datum {

    @SerializedName("banner")
    @Expose
    private Banner banner;
    @SerializedName("items")
    @Expose
    private ArrayList<Product> items = null;

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

    public ArrayList<Product> getItems() {
        return items;
    }

    public void setItems(ArrayList<Product> items) {
        this.items = items;
    }

}

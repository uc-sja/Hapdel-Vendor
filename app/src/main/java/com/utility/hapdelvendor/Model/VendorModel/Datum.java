
package com.utility.hapdelvendor.Model.VendorModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("seller_id")
    @Expose
    private String sellerId;
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("store_name")
    @Expose
    private String storeName;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("is_favorite")
    @Expose
    private String isFav;

    public String getSellerId() {
        return sellerId;
    }

    public String getIsFav() {
        return isFav;
    }

    public void setIsFav(String isFav) {
        this.isFav = isFav;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}

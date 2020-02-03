
package com.utility.hapdelvendor.Model.CartModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Cart {

    @SerializedName("master_category_id")
    @Expose
    private String masterCategoryId;
    @SerializedName("seller_id")
    @Expose
    private String sellerId;
    @SerializedName("seller_name")
    @Expose
    private String sellerName;
    @SerializedName("distance")
    @Expose
    private String distance;
    @SerializedName("sub_total")
    @Expose
    private String subTotal;
    @SerializedName("discount")
    @Expose
    private Double discount;
    @SerializedName("grand_total")
    @Expose
    private Double grandTotal;
    @SerializedName("delivery_charges")
    @Expose
    private Double deliveryCharges;

    public String getMasterCategoryId() {
        return masterCategoryId;
    }

    public void setMasterCategoryId(String masterCategoryId) {
        this.masterCategoryId = masterCategoryId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(Double grandTotal) {
        this.grandTotal = grandTotal;
    }

    public Double getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(Double deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

}

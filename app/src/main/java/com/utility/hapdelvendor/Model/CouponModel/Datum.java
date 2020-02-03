
package com.utility.hapdelvendor.Model.CouponModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("coupon_id")
    @Expose
    private String couponId;
    @SerializedName("coupon")
    @Expose
    private String coupon;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("discount")
    @Expose
    private String discount;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("max_discount")
    @Expose
    private String maxDiscount;
    @SerializedName("min_order")
    @Expose
    private String minOrder;
    @SerializedName("per_user")
    @Expose
    private String perUser;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(String maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public String getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(String minOrder) {
        this.minOrder = minOrder;
    }

    public String getPerUser() {
        return perUser;
    }

    public void setPerUser(String perUser) {
        this.perUser = perUser;
    }

}

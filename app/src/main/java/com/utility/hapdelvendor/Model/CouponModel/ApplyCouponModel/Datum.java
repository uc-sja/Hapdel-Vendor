
package com.utility.hapdelvendor.Model.CouponModel.ApplyCouponModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("old_grand_amount")
    @Expose
    private Double oldGrandAmount;
    @SerializedName("coupon_discount")
    @Expose
    private Double couponDiscount;
    @SerializedName("grand_amount")
    @Expose
    private Double grandAmount;

    public Double getOldGrandAmount() {
        return oldGrandAmount;
    }

    public void setOldGrandAmount(Double oldGrandAmount) {
        this.oldGrandAmount = oldGrandAmount;
    }

    public Double getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(Double couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public Double getGrandAmount() {
        return grandAmount;
    }

    public void setGrandAmount(Double grandAmount) {
        this.grandAmount = grandAmount;
    }

}

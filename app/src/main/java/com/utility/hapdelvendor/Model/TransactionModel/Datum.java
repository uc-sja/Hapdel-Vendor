
package com.utility.hapdelvendor.Model.TransactionModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("coupon_discount")
    @Expose
    private String couponDiscount;
    @SerializedName("commission_amount")
    @Expose
    private String commissionAmount;
    @SerializedName("discount_vendor")
    @Expose
    private String discountVendor;
    @SerializedName("discount_admin")
    @Expose
    private String discountAdmin;
    @SerializedName("paid")
    @Expose
    private String paid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(String couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public String getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(String commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getDiscountVendor() {
        return discountVendor;
    }

    public void setDiscountVendor(String discountVendor) {
        this.discountVendor = discountVendor;
    }

    public String getDiscountAdmin() {
        return discountAdmin;
    }

    public void setDiscountAdmin(String discountAdmin) {
        this.discountAdmin = discountAdmin;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

}

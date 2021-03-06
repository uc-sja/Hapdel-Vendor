
package com.utility.hapdelvendor.Model.UserOrderModel.OrderDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("total_discount")
    @Expose
    private String totalDiscount;
    @SerializedName("coupon_discount")
    @Expose
    private String couponDiscount;
    @SerializedName("discount_vendor")
    @Expose
    private String discountVendor;
    @SerializedName("commission")
    @Expose
    private String commission;
    @SerializedName("discount_admin")
    @Expose
    private String discountAdmin;
    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("modified_date")
    @Expose
    private String modifiedDate;
    @SerializedName("txn_date")
    @Expose
    private String txnDate;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("txn_id")
    @Expose
    private String txnId;

    @SerializedName("image")
    @Expose
    private String product_image;

    @SerializedName("payment_method")
    @Expose
    private String payment_method;

    @SerializedName("service_time")
    @Expose
    private String service_time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @SerializedName("type")
    @Expose
    private String type;

    public String getService_time() {
        return service_time;
    }

    public void setService_time(String service_time) {
        this.service_time = service_time;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    private boolean expanded;

    public String getProduct_image() {
        return product_image;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getProductImage() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(String totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(String couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public String getDiscountVendor() {
        return discountVendor;
    }

    public void setDiscountVendor(String discountVendor) {
        this.discountVendor = discountVendor;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getDiscountAdmin() {
        return discountAdmin;
    }

    public void setDiscountAdmin(String discountAdmin) {
        this.discountAdmin = discountAdmin;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(String txnDate) {
        this.txnDate = txnDate;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

}

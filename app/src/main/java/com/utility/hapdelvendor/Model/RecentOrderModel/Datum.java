
package com.utility.hapdelvendor.Model.RecentOrderModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("modified_date")
    @Expose
    private String modifiedDate;
    @SerializedName("store_name")
    @Expose
    private Object storeName;
    @SerializedName("txn_date")
    @Expose
    private String txnDate;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("name")
    @Expose
    private String name;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Object getStoreName() {
        return storeName;
    }

    public void setStoreName(Object storeName) {
        this.storeName = storeName;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

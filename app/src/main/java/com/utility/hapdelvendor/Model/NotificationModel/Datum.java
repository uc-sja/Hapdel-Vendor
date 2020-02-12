
package com.utility.hapdelvendor.Model.NotificationModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_type")
    @Expose
    private String orderType;
    @SerializedName("txn_id")
    @Expose
    private String txnId;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("summary")
    @Expose
    private String summary;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("seen")
    @Expose
    private String seen;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

}

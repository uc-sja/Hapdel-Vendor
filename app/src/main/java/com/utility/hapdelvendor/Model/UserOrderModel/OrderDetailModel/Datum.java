
package com.utility.hapdelvendor.Model.UserOrderModel.OrderDetailModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {

    @SerializedName("items")
    @Expose
    private List<Item> items = null;
    @SerializedName("customer")
    @Expose
    private List<Customer> customer = null;

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public List<Customer> getCustomer() {
        return customer;
    }

    public void setCustomer(List<Customer> customer) {
        this.customer = customer;
    }

}

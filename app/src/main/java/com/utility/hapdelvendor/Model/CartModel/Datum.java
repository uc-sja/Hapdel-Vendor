
package com.utility.hapdelvendor.Model.CartModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {

    @SerializedName("products")
    @Expose
    private List<Product> products = null;
    @SerializedName("sellers_list")
    @Expose
    private List<SellersList> sellersList = null;
    @SerializedName("cart")
    @Expose
    private Cart cart;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<SellersList> getSellersList() {
        return sellersList;
    }

    public void setSellersList(List<SellersList> sellersList) {
        this.sellersList = sellersList;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

}

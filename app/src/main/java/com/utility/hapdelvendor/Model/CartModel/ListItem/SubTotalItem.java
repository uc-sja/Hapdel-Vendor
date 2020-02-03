package com.utility.hapdelvendor.Model.CartModel.ListItem;


import com.utility.hapdelvendor.Model.CartModel.Cart;

public class SubTotalItem extends ListItem {

    private Cart subTotal;

    public Cart getItem() {
        return subTotal;
    }

    public void setItem(Cart subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public int getItemType() {
        return TYPE_SUBTOTAL;
    }
}

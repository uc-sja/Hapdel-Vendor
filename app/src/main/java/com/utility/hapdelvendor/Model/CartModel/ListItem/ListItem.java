package com.utility.hapdelvendor.Model.CartModel.ListItem;

public abstract class ListItem {
    public static final int TYPE_SELLER = 0;
    public static final int TYPE_PRODUCT = 1;
    public static final int TYPE_SUBTOTAL = 2;
    abstract public int getItemType();
}

package com.utility.hapdelvendor.Model.CartModel.ListItem;



import com.utility.hapdelvendor.Model.CartModel.SellersList;

import java.util.List;

public class SellerItem extends ListItem {

    private List<SellersList> item;

    public List<SellersList> getItem() {
        return item;
    }

    public void setItem(List<SellersList> item) {
        this.item = item;
    }

    @Override
    public int getItemType() {
        return TYPE_SELLER;
    }
}

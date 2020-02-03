package com.utility.hapdelvendor.Model.BannerModel.CategoryBanner.ListItem;


import com.utility.hapdelvendor.Model.CartModel.Product;

public class ProductItem extends ListItem {
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public int getItemType() {
        return ListItem.TYPE_PRODUCT;
    }
}

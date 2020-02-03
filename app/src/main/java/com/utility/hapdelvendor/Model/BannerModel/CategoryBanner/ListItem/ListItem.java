package com.utility.hapdelvendor.Model.BannerModel.CategoryBanner.ListItem;

public abstract class ListItem {
    public static final int TYPE_CATEGORY = 0;
    public static final int TYPE_PRODUCT = 1;
    abstract public int getItemType();
}
